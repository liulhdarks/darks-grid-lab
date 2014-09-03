package darks.grid.test.cloud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.job.CGJobAdapter;
import darks.grid.kernel.task.CGTaskLinkAdapter;

public class StoreUpdateAdapter extends CGTaskLinkAdapter<String>
{

	public Collection<? extends ICGJob> split(int csize, Object[] arg)
	{
		List<ICGJob> list = new ArrayList<ICGJob>();
		for (Object obj : arg)
		{
			for (int i = 0; i < csize; i++)
			{
				list.add(new CGJobAdapter<Object>(obj)
				{
					public Object execute()
					{
						return getParameter(0);
					}
				});
			}
		}
		return list;
	}

	public String reduce(List<ICGJobResult> results)
	{
		try
		{
			StringBuffer strs = new StringBuffer();
			for (ICGJobResult ret : results)
			{
				String val = (String) ret.getData();
				strs.append(val);
			}
			return strs.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
