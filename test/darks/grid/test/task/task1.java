package darks.grid.test.task;

import java.util.List;

import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.task.CGTaskLinkAdapter;

public class task1 extends CGTaskLinkAdapter<String>
{

	public String reduce(List<ICGJobResult> results)
	{
		try
		{
			long sum = 0;
			for (ICGJobResult ret : results)
			{
				long val = ret.getData();
				sum += val;
			}
			return String.valueOf(sum);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
