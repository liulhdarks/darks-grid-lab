package darks.grid.kernel.Impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.store.CGThreadStore;
import darks.grid.kernel.task.CGAbstractTaskRunner;
import darks.grid.kernel.task.CGTaskAdapter;
import darks.grid.kernel.task.CGTaskLinkAdapter;
import darks.grid.kernel.task.CGTaskLinkRunner;
import darks.grid.kernel.task.CGTaskRunner;
import darks.grid.util.CGUtil;

public class CGTaskProcessor
{

	private ConcurrentHashMap<String, CGAbstractTaskRunner> taskmap = null;

	public CGTaskProcessor()
	{
		taskmap = new ConcurrentHashMap<String, CGAbstractTaskRunner>();
	}

	public <T, R> CGFutureTask<R> execute(Class<? extends CGTaskAdapter<T, R>> taskCls, T arg,
			long timout)
	{
		try
		{
			CGTaskAdapter<T, R> task = CGUtil.newInstance(taskCls);
			CGTaskRunner<T, R> runner = new CGTaskRunner<T, R>(task, arg);
			taskmap.put(runner.getUUID(), runner);
			CGFutureTask<R> ftask = new CGFutureTask<R>(runner);
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.TASK_THREAD).submit(ftask);
			return ftask;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public <T, R> CGFutureTask<R> execute(CGTaskAdapter<T, R> task, T arg, long timout)
	{
		try
		{
			CGTaskRunner<T, R> runner = new CGTaskRunner<T, R>(task, arg, timout);
			taskmap.put(runner.getUUID(), runner);
			CGFutureTask<R> ftask = new CGFutureTask<R>(runner);
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.TASK_THREAD).submit(ftask);
			return ftask;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public <R> CGFutureTask<R> execute(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			String clsName, String linkPath, boolean updateCls, Object... objs)
	{

		return executeArray(taskCls, clsName, linkPath, updateCls, objs);
	}

	public <R> CGFutureTask<R> execute(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			String clsName, boolean updateCls, Object... objs)
	{

		return executeArray(taskCls, clsName, updateCls, objs);
	}

	public <R> CGFutureTask<R> execute(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, String clsName, String linkPath,
			boolean updateCls, Object... objs)
	{

		return executeArray(list, taskCls, clsName, linkPath, updateCls, objs);
	}

	public <R> CGFutureTask<R> execute(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, String clsName, boolean updateCls,
			Object... objs)
	{

		return executeArray(list, taskCls, clsName, updateCls, objs);
	}

	public <R> CGFutureTask<R> executeArray(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, String clsName, String linkPath,
			boolean updateCls, Object[] objs)
	{

		try
		{

			CGTaskLinkAdapter<R> task = CGUtil.newInstance(taskCls);
			CGTaskLinkRunner<R> runner = new CGTaskLinkRunner<R>(list, task, clsName, linkPath,
					updateCls, objs);
			taskmap.put(runner.getUUID(), runner);
			CGFutureTask<R> ftask = new CGFutureTask<R>(runner);
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.TASK_THREAD).submit(ftask);
			return ftask;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public <R> CGFutureTask<R> executeArray(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, String clsName, boolean updateCls,
			Object[] objs)
	{

		try
		{
			String path = clsName.replace(".", "/");
			String linkPath = "http://" + CGDataStore.initInfoHost + ":" + CGDataStore.initHttpPort;
			if (!linkPath.endsWith("/"))
				linkPath += "/";
			linkPath = linkPath + CGDataStore.classRoot;
			if (!linkPath.endsWith("/"))
				linkPath += "/";
			linkPath += path;
			if (!linkPath.endsWith(".class"))
				linkPath += ".class";

			return executeArray(list, taskCls, clsName, linkPath, updateCls, objs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public <R> CGFutureTask<R> executeArray(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			String clsName, String linkPath, boolean updateCls, Object[] objs)
	{

		try
		{
			return executeArray(CGDataCenter.getInstance().getNodeList(), taskCls, clsName,
					linkPath, updateCls, objs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public <R> CGFutureTask<R> executeArray(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			String clsName, boolean updateCls, Object[] objs)
	{

		try
		{
			return executeArray(CGDataCenter.getInstance().getNodeList(), taskCls, clsName,
					updateCls, objs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
