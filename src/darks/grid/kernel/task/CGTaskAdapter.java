package darks.grid.kernel.task;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.ICGNode;

public abstract class CGTaskAdapter<T, R> extends CGTask<T, R>
{

	public Map<? extends ICGJob, ICGNode> map(List<ICGNode> subgrid, T arg)
	{

		Collection<? extends ICGJob> jobs = split(subgrid.size(), arg);
		Map<ICGJob, ICGNode> tmpmap = new HashMap<ICGJob, ICGNode>();
		int index = 0;
		for (ICGJob job : jobs)
		{
			ICGNode node = subgrid.get(index++);
			tmpmap.put(job, node);
			if (index >= subgrid.size())
				index = 0;
		}

		return tmpmap;
	}

	public Collection<? extends ICGJob> split(int csize, T arg)
	{
		return null;
	}

	public R reduce(List<ICGJobResult> results)
	{
		return null;
	}

}
