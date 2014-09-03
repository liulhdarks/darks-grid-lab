package darks.grid.kernel.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGNode;
import darks.grid.kernel.job.CGJobPackage;
import darks.grid.kernel.loadbalance.roundrobin.CGRoundRobinLoadBalance;
import darks.grid.kernel.meter.ICGMeter;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.typedef.P;

public class CGJobPackageThread implements Runnable
{
	private CGJobPackage pack = null;

	public CGJobPackageThread(CGJobPackage pack)
	{
		this.pack = pack;
	}

	public void run()
	{

		ConcurrentMap<String, ICGNode> nodemap = CGDataCenter.getInstance().getNodeMap();
		ArrayList<ICGNode> list = new ArrayList<ICGNode>();
		list.addAll(nodemap.values());
		list.add(CGDataStore.getLocalNode());
		CGRoundRobinLoadBalance loadbalance = new CGRoundRobinLoadBalance(list);

		ConcurrentMap<String, ICGJob> map = pack.getJobMap();
		ConcurrentMap<String, ICGJobResult> rltmap = pack.getJobResultMap();
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();)
		{
			try
			{
				String jobid = it.next();
				if (!rltmap.containsKey(jobid))
				{
					ICGMeter meter = pack.getMeterByJobId(jobid);
					ICGNode node = loadbalance.getBalancedNode();
					responseMeter(node, meter);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void responseMeter(ICGNode node, ICGMeter meter)
	{
		P.printObject("超时重发分工,向" + node.getUUId() + "发送" + meter.getJobId());
		String addr = (String) node.getNodeInfo(CGNode.HOSTIP);
		int port = (Integer) node.getNodeInfo(CGNode.OBJPORT);
		CGDataStore.getNetResult().response(addr, port, meter);
	}

}
