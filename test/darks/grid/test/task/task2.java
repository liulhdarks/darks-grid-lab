package darks.grid.test.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.job.CGJobAdapter;
import darks.grid.kernel.task.CGTaskAdapter;

public class task2 extends CGTaskAdapter<String, String>
{
	public Collection<? extends ICGJob> split(int csize, String num)
	{

		List<ICGJob> jobs = new ArrayList<ICGJob>();
		long n = Long.parseLong(num);
		for (int i = 0; i < 5; i++)
		{
			long s = i * (n / 5) + 1;
			long e = (i + 1) * (n / 5);
			jobs.add(new CGJobAdapter<Long>(s, e)
			{
				private long f(long a)
				{
					if (a == 1 || a == 2)
						return 1;
					return f(a - 1) + f(a - 2);
				}

				public Object execute()
				{
					long s = getParameter(0);
					long e = getParameter(1);
					long sum = 0;
					for (long i = s; i <= e; i++)
					{
						long r = f(i);
						System.out.println(r);
						sum += r;
					}
					System.out.println("out rlt(" + s + "-" + e + "):" + sum);
					return sum;
				}
			});
		}

		return jobs;
	}

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
