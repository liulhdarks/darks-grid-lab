package darks.grid.test.cloud;

import java.util.List;

import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.task.CGTaskLinkAdapter;

public class ShowAdapter extends CGTaskLinkAdapter<String>
{

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
