package darks.grid.kernel.service.thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentMap;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGNode;
import darks.grid.kernel.loadbalance.roundrobin.CGRoundRobinLoadBalance;
import darks.grid.kernel.meter.ICGMeter;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;

public class CGRecallThread implements Runnable
{

	private LinkedList<ICGMeter> meters = null;

	public CGRecallThread()
	{

	}

	public CGRecallThread(ICGMeter meter)
	{
		meters = new LinkedList<ICGMeter>();
		meters.add(meter);
	}

	public CGRecallThread(LinkedList<ICGMeter> meters)
	{
		this.meters = meters;
	}

	public void setMeter(LinkedList<ICGMeter> meters)
	{
		this.meters = meters;
	}

	public void run()
	{
		if (meters.size() == 0)
			return;
		ConcurrentMap<String, ICGNode> nodemap = CGDataCenter.getInstance().getNodeMap();
		ArrayList<ICGNode> list = new ArrayList<ICGNode>();
		list.addAll(nodemap.values());
		list.add(CGDataStore.getLocalNode());
		CGRoundRobinLoadBalance loadbalance = new CGRoundRobinLoadBalance(list);

		while (meters.size() > 0)
		{
			try
			{
				ICGMeter meter = meters.poll();
				System.out.println("数据续传:" + meter.getJobId());
				ICGNode node = loadbalance.getBalancedNode();
				String addr = (String) node.getNodeInfo(CGNode.HOSTIP);
				int port = (Integer) node.getNodeInfo(CGNode.OBJPORT);
				CGDataStore.getNetResult().response(addr, port, meter);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

}
