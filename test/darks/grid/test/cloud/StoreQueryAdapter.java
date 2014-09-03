package darks.grid.test.cloud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.job.CGJobAdapter;
import darks.grid.kernel.task.CGTaskLinkAdapter;

public class StoreQueryAdapter extends CGTaskLinkAdapter<List<String[]>>
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

	public List<String[]> reduce(List<ICGJobResult> results)
	{
		try
		{
			List<String[]> list = new ArrayList<String[]>();
			for (ICGJobResult ret : results)
			{
				Object obj = ret.getData();
				if (obj instanceof List)
				{
					List<String[]> val = (List<String[]>) ret.getData();
					list.addAll(val);
				}
				else
				{
					System.out.println("[ERROR]reduce:" + obj);
					return null;
				}

			}
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
