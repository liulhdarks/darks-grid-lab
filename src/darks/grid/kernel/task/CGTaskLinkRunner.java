package darks.grid.kernel.task;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGNode;
import darks.grid.kernel.job.CGJobPackage;
import darks.grid.kernel.meter.CGLinkMeter;
import darks.grid.kernel.meter.ICGMeter;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.thread.CGJobPackageThread;
import darks.grid.kernel.typedef.ActionEnum;
import darks.grid.kernel.typedef.DS;
import darks.grid.kernel.typedef.TS;

public class CGTaskLinkRunner<R> extends CGAbstractTaskRunner<R>
{

	private String clsName;
	private String linkPath;
	private boolean updateCls;
	private Object[] objlist;
	private CGTaskLinkAdapter<R> task = null;
	private long timeout = 0;
	private ActionEnum timeoutAction = ActionEnum.ACTION_TIMEOUT_STOP;
	private static final Object mutex = new Object();
	private List<ICGNode> list = null;

	public CGTaskLinkRunner(List<ICGNode> list, CGTaskLinkAdapter<R> task, String clsName,
			String linkPath, boolean updateCls, Object... objs)
	{
		this.clsName = clsName;
		this.linkPath = linkPath;
		this.updateCls = updateCls;
		objlist = objs;
		this.task = task;
		this.timeout = DS.getLinkTimeout();
		this.timeoutAction = DS.getLinkTimeoutAction();
		this.list = list;
	}

	public CGTaskLinkRunner(List<ICGNode> list, CGTaskLinkAdapter<R> task, String clsName,
			String linkPath, boolean updateCls, int timeout, Object... objs)
	{
		this.clsName = clsName;
		this.linkPath = linkPath;
		this.updateCls = updateCls;
		objlist = objs;
		this.task = task;
		this.timeout = timeout;
		this.list = list;
	}

	public R execute() throws Exception
	{
		try
		{
			System.out.println("开始任务:" + task.getTaskId());
			Map<ICGJob, ICGNode> map = null;
			if (list == null)
			{
				list = CGDataCenter.getInstance().getNodeList();
			}
			map = (Map<ICGJob, ICGNode>) task.map(list, objlist);
			responseListMap(map);
			return requestResult();
		}
		catch (TimeoutException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void responseListMap(Map<ICGJob, ICGNode> mlist)
	{
		CGJobPackage jpack = CGDataCenter.getInstance().addTaskToJobPackage(task.getTaskId());
		for (Entry<ICGJob, ICGNode> entry : mlist.entrySet())
		{
			ICGJob job = entry.getKey();
			ICGNode node = entry.getValue();
			System.out.println("向" + node.getNodeInfo(CGNode.HOSTIP) + ":"
					+ node.getNodeInfo(CGNode.INFOPORT) + "发送任务" + job.getJobId());
			ICGMeter meter = new CGLinkMeter(task.getTaskId(), job, clsName, linkPath, updateCls,
					job.getParameterList().toArray());
			responseMeter(node, meter);
			jpack.addJob(job, meter);
			node.getJobs().put(job.getJobId(), meter);
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
			throw new Exception("[ERROR]CGTaskLinkRunner.requestResult:JobPackage is null");
		}
		long st = System.currentTimeMillis();
		long et = 0;
		while (!jpack.isFinshed())
		{
			if (timeout > 0)
			{
				et = System.currentTimeMillis();
				if ((et - st) >= timeout)
				{
					if (timeoutAction == ActionEnum.ACTION_TIMEOUT_STOP)
					{
						throw new TimeoutException("CGTaskLinkRunner.requestResult:time out");
					}
					else if (timeoutAction == ActionEnum.ACTION_TIMEOUT_RECALL)
					{
						TS.getInstance().getExecutorService(TS.TASK_THREAD)
								.execute(new CGJobPackageThread(jpack));
						st = et;
					}
				}
			}
			Thread.sleep(20);
		}

		return task.reduce(jpack.getResultList());
	}

	Object[] getValue()
	{
		return objlist;
	}

	CGTask<Object[], R> getTask()
	{
		return task;
	}
}
