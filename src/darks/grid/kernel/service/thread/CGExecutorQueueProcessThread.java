package darks.grid.kernel.service.thread;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import darks.grid.kernel.Impl.CGFutureJobTask;
import darks.grid.kernel.meter.CGJobResultApartMeter;
import darks.grid.kernel.meter.CGJobResultMeter;
import darks.grid.kernel.meter.CGObjectMeter;
import darks.grid.kernel.meter.ICGJobResultApartMeter;
import darks.grid.kernel.meter.ICGJobResultMeter;
import darks.grid.kernel.meter.ICGObjectMeter;
import darks.grid.kernel.service.CGExecutorService;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.store.CGPrintCenter;
import darks.grid.kernel.store.job.CGJobReResponseStore;
import darks.grid.util.CGBytesPackage;
import darks.grid.util.CGUtil;

public class CGExecutorQueueProcessThread extends Thread
{
	@SuppressWarnings("unused")
	private long execqid = this.getId();
	private CGExecutorService service = null;
	private int index = 0;

	public CGExecutorQueueProcessThread()
	{
	}

	public CGExecutorQueueProcessThread(CGExecutorService service)
	{
		this.service = service;
	}

	public CGExecutorQueueProcessThread(CGExecutorService service, int bufferIndex)
	{
		this.service = service;
		index = bufferIndex;
	}

	public void run()
	{
		while (!service.isClose())
		{
			try
			{
				while (!CGDataCenter.getInstance().isExecProcQueueEmpty(index))
				{
					CGFutureJobTask fut = CGDataCenter.getInstance().deExecProcQueue(index);
					if (fut == null)
						continue;
					if (!fut.isDead())
					{
						CGDataCenter.getInstance().enExecProcQueue(fut);
					}
					else
					{
						Object ret = null;
						try
						{
							ret = fut.get(50, TimeUnit.MILLISECONDS);
							process(fut, ret, false, "");
						}
						catch (TimeoutException e)
						{
							e.printStackTrace();
							CGDataCenter.getInstance().enExecProcQueue(fut);
						}
						catch (ExecutionException e)
						{
							e.printStackTrace();
							process(fut, ret, true, e.toString());
						}

					}
					sleep(10);
				}
				sleep(10);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void process(CGFutureJobTask fut, Object obj, boolean isfail, String strerr)
	{

		try
		{
			ICGJobResultMeter rltmeter = new CGJobResultMeter(fut, obj, isfail, strerr);
			byte[] data = CGUtil.ObjectToByte(rltmeter);
			int bufsize = CGDataStore.getNetObj().getSendBufferSize();
			if (data.length <= bufsize)
			{
				ICGObjectMeter objmeter = new CGObjectMeter(1, rltmeter, fut.getIpaddress(),
						fut.getPort());
				CGDataStore.getNetObj().response(fut.getIpaddress(), fut.getPort(), objmeter);
			}
			else
			{
				if (CGDataStore.PACKAGE_SIZE > bufsize)
					CGDataStore.PACKAGE_SIZE = bufsize / 2;
				List<CGBytesPackage> list = CGUtil.apartByteArray(data, CGDataStore.PACKAGE_SIZE);
				CGPrintCenter.printObject("数据包大小为" + data.length + "，超过最大限制"
						+ CGDataStore.PACKAGE_SIZE + ",拆分为" + list.size() + "个包");
				int index = 1;
				for (CGBytesPackage bts : list)
				{
					ICGJobResultApartMeter jobmeter = new CGJobResultApartMeter(rltmeter, bts,
							index, list.size());
					ICGObjectMeter objmeter = new CGObjectMeter(2, jobmeter, fut.getIpaddress(),
							fut.getPort());
					CGDataStore.getNetObj().response(fut.getIpaddress(), fut.getPort(), objmeter);
					CGJobReResponseStore.addObjectMeter(objmeter, index,
							jobmeter.getResultMeterId());
					index++;
					// sleep(10);
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

}
