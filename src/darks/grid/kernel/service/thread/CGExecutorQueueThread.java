package darks.grid.kernel.service.thread;

import java.util.Map;
import java.util.Map.Entry;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGFutureJobTask;
import darks.grid.kernel.factory.CGNodeFactory;
import darks.grid.kernel.job.CGJobLinkExecutor;
import darks.grid.kernel.meter.ICGMeter;
import darks.grid.kernel.network.ICGEvent;
import darks.grid.kernel.network.factory.CGMessageFactory;
import darks.grid.kernel.service.CGExecutorService;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.store.CGThreadStore;
import darks.grid.util.CGUtil;

public class CGExecutorQueueThread extends Thread
{
	private long execqid = this.getId();
	private CGExecutorService service = null;
	private int buf_index = 0;

	public CGExecutorQueueThread()
	{
	}

	public CGExecutorQueueThread(CGExecutorService service)
	{
		this.service = service;
	}

	public CGExecutorQueueThread(CGExecutorService service, int bufferIndex)
	{
		this.service = service;
		buf_index = bufferIndex;
	}

	public void run()
	{
		while (!service.isClose())
		{
			try
			{
				while (!CGDataCenter.getInstance().isExecQueueEmpty(buf_index))
				{
					ICGEvent e = CGDataCenter.getInstance().deExecQueue(buf_index);
					byte[] data = e.getData();
					// String info=new String(data);
					process(e.getData(), e.getAddress(), e.getPort());
				}
				sleep(10);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void process(byte[] btinfo, String addr, int port)
	{

		try
		{
			ICGMeter meter = (ICGMeter) CGUtil.ByteToObject(btinfo);
			if (!CGDataCenter.getInstance().containsCGNode(meter.getNodeid())
					&& !CGDataStore.getLocalNode().getUUId().equals(meter.getNodeid()))
			{
				if (CGNodeFactory.getInstance().containsHistoryNode(meter.getNodeid()))
				{
					ICGNode node = CGNodeFactory.getInstance().getHistoryNode(meter.getNodeid());
					CGDataCenter.getInstance().addCGNode(node.getUUId(), node);
					CGNodeFactory.getInstance().removeHistoryNode(node.getUUId());
					System.out.println("历史节点" + node.getUUId() + "加入列表");
				}
				else
				{
					System.out.println("请求与断开节点" + meter.getNodeid() + "链接");
					CGDataStore.getNetInfo().response(meter.getIPAddress(), meter.getInfoPort(),
							CGMessageFactory.createMessage(CGMessageFactory.JOIN_NODE));
				}
			}
			if (meter.getType() == ICGMeter.LISTMETER)
			{
				Map<String, ICGJob> map = meter.getJobs();
				for (Entry<String, ICGJob> entry : map.entrySet())
				{
					// String key=entry.getKey();
					ICGJob job = entry.getValue();
					CGFutureJobTask fut = new CGFutureJobTask(meter, job);
					CGThreadStore.getInstance().getThreadExecutorService(buf_index).submit(fut);
					CGDataCenter.getInstance().enExecProcQueue(fut);
				}
			}
			else if (meter.getType() == ICGMeter.LINKMETER)
			{
				// System.out.println(meter.getClassName()+" "+meter.getLinkPath()+" "+meter.getIPAddress()+":"+meter.getPort());
				CGFutureJobTask fut = new CGFutureJobTask(meter, meter.getJob(),
						new CGJobLinkExecutor(meter));
				CGThreadStore.getInstance().getThreadExecutorService(buf_index).submit(fut);
				CGDataCenter.getInstance().enExecProcQueue(fut);
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

}
