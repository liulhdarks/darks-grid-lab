package darks.grid.kernel.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.job.CGJobAdapter;

public abstract class CGTaskLinkAdapter<R> extends CGTaskAdapter<Object[], R>
{

	public Collection<? extends ICGJob> split(int csize, Object[] arg)
	{
		List<ICGJob> list = new ArrayList<ICGJob>();
		for (Object obj : arg)
		{
			list.add(new CGJobAdapter<Object>(obj)
			{
				public Object execute()
				{
					return getParameter(0);
				}
			});
		}
		return list;
	}

	public R reduce(List<ICGJobResult> results)
	{
		return null;
	}

}
