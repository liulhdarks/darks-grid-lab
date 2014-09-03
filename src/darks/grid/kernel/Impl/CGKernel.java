package darks.grid.kernel.Impl;

import java.util.List;

import darks.grid.CloudGrid;
import darks.grid.ICGLinkTask;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.control.CGClassControl;
import darks.grid.kernel.factory.CGNodeFactory;
import darks.grid.kernel.network.factory.CGNetFactory;
import darks.grid.kernel.store.CGConfigure;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.store.CGPrintCenter;
import darks.grid.kernel.store.CGServiceCenter;
import darks.grid.kernel.store.CGThreadStore;
import darks.grid.kernel.task.CGTaskAdapter;
import darks.grid.kernel.task.CGTaskLinkAdapter;

public class CGKernel
{

	private static CGKernel instance = null;

	private CGTaskProcessor proc = null;

	private CGKernel()
	{
		CGPrintCenter.printHeader();
		proc = new CGTaskProcessor();
		CGServiceCenter.getInstance().initService();
	}

	public void Start() throws Exception
	{
		CGDataStore.loadConfig();
		CGServiceCenter.getInstance().StartService(CGNetFactory.NET_TYPE_UDP);
		ICGNode node = CGNodeFactory.getInstance().createLocalNode();
		CGPrintCenter.printObject("本地节点" + node.getUUId() + "被创建成功...");
		ShutdownHook();
	}

	public void Start(String cfg) throws Exception
	{
		CGDataStore.loadConfig(cfg);
		CGServiceCenter.getInstance().StartService(CGNetFactory.NET_TYPE_UDP);
		ICGNode node = CGNodeFactory.getInstance().createLocalNode();
		CGPrintCenter.printObject("本地节点" + node.getUUId() + "被创建成功...");
		ShutdownHook();
	}

	public void Start(CGConfigure pcfg) throws Exception
	{
		CGDataStore.loadConfig(pcfg);
		CGServiceCenter.getInstance().StartService(CGNetFactory.NET_TYPE_UDP);
		ICGNode node = CGNodeFactory.getInstance().createLocalNode();
		CGPrintCenter.printObject("本地节点" + node.getUUId() + "被创建成功...");
		ShutdownHook();
	}

	public void Stop() throws Exception
	{
		CGThreadStore.getInstance().shutdown();
		CGServiceCenter.getInstance().StopService();
	}

	@SuppressWarnings("unchecked")
	public <T, R> CGFutureTask<R> execute(String taskName, T arg)
	{
		return execute(CGClassControl.loadClass(taskName), arg);
	}

	public <T, R> CGFutureTask<R> execute(Class<? extends CGTaskAdapter<T, R>> taskCls, T arg)
	{
		return Runner().execute(taskCls, arg, 0);
	}

	public <T, R> CGFutureTask<R> execute(CGTaskAdapter<T, R> task, T arg)
	{
		return Runner().execute(task, arg, 0);
	}

	@SuppressWarnings("unchecked")
	public <T, R> CGFutureTask<R> execute(String taskName, T arg, int timout)
	{
		return execute(CGClassControl.loadClass(taskName), arg, timout);
	}

	public <T, R> CGFutureTask<R> execute(Class<? extends CGTaskAdapter<T, R>> taskCls, T arg,
			long timout)
	{
		return Runner().execute(taskCls, arg, timout);
	}

	public <T, R> CGFutureTask<R> execute(CGTaskAdapter<T, R> task, T arg, long timout)
	{
		return Runner().execute(task, arg, timout);
	}

	// ---------------------------------------------------------------------------------------------------------------

	public <T, R> CGFutureTask<R> execute(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			String clsName, String linkPath, boolean updateCls, Object... objs)
	{
		return Runner().execute(taskCls, clsName, linkPath, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> execute(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			String clsName, boolean updateCls, Object... objs)
	{
		return Runner().execute(taskCls, clsName, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> execute(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			Class<? extends ICGLinkTask> workCls, boolean updateCls, Object... objs)
	{
		String clsName = workCls.getCanonicalName();
		return Runner().execute(taskCls, clsName, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> executeArray(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			String clsName, String linkPath, boolean updateCls, Object[] objs)
	{
		return Runner().executeArray(taskCls, clsName, linkPath, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> executeArray(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			String clsName, boolean updateCls, Object[] objs)
	{
		return Runner().executeArray(taskCls, clsName, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> executeArray(Class<? extends CGTaskLinkAdapter<R>> taskCls,
			Class<? extends ICGLinkTask> workCls, boolean updateCls, Object[] objs)
	{
		String clsName = workCls.getCanonicalName();
		return Runner().executeArray(taskCls, clsName, updateCls, objs);
	}

	// ----------------------------------------------------------------------------------------------------

	public <T, R> CGFutureTask<R> execute(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, String clsName, String linkPath,
			boolean updateCls, Object... objs)
	{
		return Runner().execute(list, taskCls, clsName, linkPath, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> execute(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, String clsName, boolean updateCls,
			Object... objs)
	{
		return Runner().execute(list, taskCls, clsName, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> execute(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, Class<? extends ICGLinkTask> workCls,
			boolean updateCls, Object... objs)
	{
		String clsName = workCls.getCanonicalName();
		return Runner().execute(list, taskCls, clsName, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> executeArray(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, String clsName, String linkPath,
			boolean updateCls, Object[] objs)
	{
		return Runner().executeArray(list, taskCls, clsName, linkPath, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> executeArray(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, String clsName, boolean updateCls,
			Object[] objs)
	{
		return Runner().executeArray(list, taskCls, clsName, updateCls, objs);
	}

	public <T, R> CGFutureTask<R> executeArray(List<ICGNode> list,
			Class<? extends CGTaskLinkAdapter<R>> taskCls, Class<? extends ICGLinkTask> workCls,
			boolean updateCls, Object[] objs)
	{
		String clsName = workCls.getCanonicalName();
		return Runner().executeArray(list, taskCls, clsName, updateCls, objs);
	}

	// ------------------------------------------------------------------------------------------

	CGTaskProcessor Runner()
	{
		return proc;
	}

	public static CGKernel getInstance()
	{
		if (instance == null)
			instance = new CGKernel();
		return instance;
	}

	private void ShutdownHook() throws Exception
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{

			public void run()
			{
				try
				{
					CGPrintCenter.printObject("正在关闭CloudGrid..");
					CloudGrid.Stop();
					CGPrintCenter.printObject("关闭");

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		Thread.sleep(1000);
	}

}
