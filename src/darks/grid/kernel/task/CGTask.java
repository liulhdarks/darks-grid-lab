package darks.grid.kernel.task;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.ICGTask;
import darks.grid.util.CGUtil;

public abstract class CGTask<T, R> implements ICGTask<T, R>
{

	protected String taskId = CGUtil.getUUID();

	public Map<? extends ICGJob, ICGNode> map(List<ICGNode> subgrid, T arg)
	{

		return null;
	}

	public Collection<? extends ICGJob> split(int csize, T arg)
	{
		return null;
	}

	public R reduce(List<ICGJobResult> results)
	{
		return null;
	}

	public String getTaskId()
	{
		return taskId;
	}

}
