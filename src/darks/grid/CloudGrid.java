package darks.grid;

import java.util.List;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGFutureTask;
import darks.grid.kernel.Impl.CGKernel;
import darks.grid.kernel.factory.CGNodeFactory;
import darks.grid.kernel.store.CGConfigure;
import darks.grid.kernel.task.CGTaskAdapter;
import darks.grid.kernel.typedef.ActionEnum;
import darks.grid.kernel.typedef.DS;

public class CloudGrid
{

	private static boolean status = false;

	private CloudGrid()
	{

	}

	public static void Start() throws Exception
	{
		if (status)
			throw new Exception("[ERROR]the cloudgrid is running,don't double run it");
		CGKernel.getInstance().Start();
		status = true;
	}

	public static void Start(String cfgPath) throws Exception
	{
		if (status)
			throw new Exception("[ERROR]the cloudgrid is running,don't double run it");
		CGKernel.getInstance().Start(cfgPath);
		status = true;
	}

	public static void Start(CGConfigure cfg) throws Exception
	{
		if (status)
			throw new Exception("[ERROR]the cloudgrid is running,don't double run it");
		CGKernel.getInstance().Start(cfg);
		status = true;
	}

	public static void Stop() throws Exception
	{
		if (!status)
			throw new Exception("[ERROR]the cloudgrid isn't be runned,cannot shut down it");
		CGKernel.getInstance().Stop();
		status = false;
	}

	// nomal
	// task---------------------------------------------------------------------
	public static <T, R> CGFutureTask<R> execute(String taskName, T arg) throws Exception
	{
		return CGKernel.getInstance().execute(taskName, arg);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(Class taskCls, T arg) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(taskCls, arg);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(CGTaskAdapter task, T arg, int none)
			throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(task, arg);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(String taskName, T arg, int timeout)
			throws Exception
	{
		return CGKernel.getInstance().execute(taskName, arg, timeout);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(Class taskCls, T arg, int timeout)
			throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(taskCls, arg, timeout);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(CGTaskAdapter task, T arg, int none, int timeout)
			throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(task, arg, timeout);
	}

	// link
	// task------------------------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(Class taskCls, String clsName, String linkPath,
			boolean updateCls, Object... objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(taskCls, clsName, linkPath, updateCls, objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(Class taskCls, String clsName, boolean updateCls,
			Object... objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(taskCls, clsName, updateCls, objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(Class taskCls, Class workCls, boolean updateCls,
			Object... objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(taskCls, workCls, updateCls, objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(List<ICGNode> list, Class taskCls, String clsName,
			String linkPath, boolean updateCls, Object... objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(list, taskCls, clsName, linkPath, updateCls, objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(List<ICGNode> list, Class taskCls, String clsName,
			boolean updateCls, Object... objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(list, taskCls, clsName, updateCls, objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> execute(List<ICGNode> list, Class taskCls, Class workCls,
			boolean updateCls, Object... objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().execute(list, taskCls, workCls, updateCls, objs);
	}

	// link task by
	// array------------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> executeArray(Class taskCls, String clsName,
			String linkPath, boolean updateCls, Object[] objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().executeArray(taskCls, clsName, linkPath, updateCls, objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> executeArray(Class taskCls, String clsName,
			boolean updateCls, Object[] objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().executeArray(taskCls, clsName, updateCls, objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> executeArray(Class taskCls, Class workCls,
			boolean updateCls, Object[] objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().executeArray(taskCls, workCls, updateCls, objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> executeArray(List<ICGNode> list, Class taskCls,
			String clsName, String linkPath, boolean updateCls, Object[] objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().executeArray(list, taskCls, clsName, linkPath, updateCls,
				objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> executeArray(List<ICGNode> list, Class taskCls,
			String clsName, boolean updateCls, Object[] objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().executeArray(list, taskCls, clsName, updateCls, objs);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> CGFutureTask<R> executeArray(List<ICGNode> list, Class taskCls,
			Class workCls, boolean updateCls, Object[] objs) throws Exception
	{
		if (!status)
			throw new Exception("don't start the cloudgrid");
		return CGKernel.getInstance().executeArray(list, taskCls, workCls, updateCls, objs);
	}

	// ------------------------------------------------------------------------------------------------------

	public static void setLinkTimeout(int timeout, ActionEnum action)
	{
		DS.setLinkTimeout(timeout, action);
	}

	public static List<ICGNode> allClouds()
	{
		return CGNodeFactory.getInstance().getNodes();
	}

	public static boolean isStart()
	{
		return status;
	}
}
