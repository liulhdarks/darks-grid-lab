package darks.grid.kernel.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGNode;
import darks.grid.kernel.job.CGJobPackage;
import darks.grid.kernel.meter.CGListMeter;
import darks.grid.kernel.meter.ICGMeter;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.util.CGUtil;

public class CGTaskRunner<T, R> extends CGAbstractTaskRunner<R>
{

	public static int METERLIMIT = 20;

	private CGTaskAdapter<T, R> task = null;

	private T val;

	private long timeout;

	public CGTaskRunner(CGTaskAdapter<T, R> task, T val)
	{
		this.task = task;
		this.val = val;
		this.timeout = 0;
	}

	public CGTaskRunner(CGTaskAdapter<T, R> task, T val, long timeout)
	{
		this.task = task;
		this.val = val;
		this.timeout = timeout;
	}

	public R execute() throws Exception
	{
		// assert task==null;
		// try{
		ConcurrentMap<String, ICGNode> nodemap = CGDataCenter.getInstance().getNodeMap();
		System.out.println("start task " + task.getTaskId());
		ArrayList<ICGNode> list = new ArrayList<ICGNode>();
		list.addAll(nodemap.values());
		list.add(CGDataStore.getLocalNode());
		// Map<ICGJob,ICGNode> map=(Map<ICGJob,ICGNode>)task.map(list, val);
		// System.out.println("map:"+map.size());
		processListMap(task.map(list, val));
		return requestResult();
		// }catch(TimeoutException e){
		// e.printStackTrace();
		// return null;
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// return null;
	}

	public void processListMap(Map<? extends ICGJob, ICGNode> map)
	{

		CGJobPackage jpack = CGDataCenter.getInstance().addTaskToJobPackage(task.getTaskId());

		Map<ICGNode, ArrayList<ICGJob>> resmap = new HashMap<ICGNode, ArrayList<ICGJob>>();

		for (Entry<? extends ICGJob, ICGNode> entry : map.entrySet())
		{
			ICGJob job = entry.getKey();
			ICGNode node = entry.getValue();
			ArrayList<ICGJob> list = resmap.get(node);
			if (list == null)
			{
				list = new ArrayList<ICGJob>();
				resmap.put(node, list);
			}
			list.add(job);
			jpack.addJob(job);

		}

		Map<ICGNode, ICGMeter> mlist = new HashMap<ICGNode, ICGMeter>();
		for (Entry<ICGNode, ArrayList<ICGJob>> entry : resmap.entrySet())
		{
			ICGNode node = entry.getKey();
			ArrayList<ICGJob> list = entry.getValue();
			if (list.size() > METERLIMIT)
			{
				ArrayList<ICGJob>[] arys = CGUtil.splitArrayList(list, METERLIMIT);
				for (ArrayList<ICGJob> tmp : arys)
				{
					mlist.put(node, new CGListMeter(task.getTaskId(), tmp,
							CGDataStore.initInfoHost, CGDataStore.initObjRltPort,
							CGDataStore.initInfoPort));
				}
			}
			else
			{
				mlist.put(node, new CGListMeter(task.getTaskId(), list, CGDataStore.initInfoHost,
						CGDataStore.initObjRltPort, CGDataStore.initInfoPort));
			}
		}
		responseListMap(mlist);
	}

	public void responseListMap(Map<ICGNode, ICGMeter> mlist)
	{

		for (Entry<ICGNode, ICGMeter> entry : mlist.entrySet())
		{
			ICGNode node = entry.getKey();
			ICGMeter meter = entry.getValue();
			responseMeter(node, meter);
			node.getJobs().put(meter.getJobId(), meter);
		}
	}

	public void responseMeter(ICGNode node, ICGMeter meter)
	{
		String addr = (String) node.getNodeInfo(CGNode.HOSTIP);
		int port = (Integer) node.getNodeInfo(CGNode.OBJPORT);
		CGDataStore.getNetResult().response(addr, port, meter);
	}

	public R requestResult() throws Exception
	{
		CGJobPackage jpack = CGDataCenter.getInstance().getJobPackage(task.getTaskId());
		if (jpack == null)
		{
			throw new Exception("[ERROR]requestResult:JobPackage is null");
		}
		long st = System.currentTimeMillis();
		long et = 0;
		while (!jpack.isFinshed())
		{
			Thread.sleep(10);
			if (timeout > 0)
			{
				et = System.currentTimeMillis();
				if ((et - st) >= timeout)
				{
					throw new TimeoutException("CGTaskRunner.requestResult:time out");
				}
			}

		}

		return task.reduce(jpack.getResultList());
	}

	T getValue()
	{
		return val;
	}

	CGTask<T, R> getTask()
	{
		return task;
	}
}
