package darks.grid.test;

import darks.grid.CloudGrid;
import darks.grid.kernel.Impl.CGFutureTask;
import darks.grid.kernel.task.CGTaskAdapter;
import darks.grid.test.task.task2;

public class demo4
{

	public static void test(Class<? extends CGTaskAdapter> task) throws Exception
	{

	}

	private static long f(long a)
	{
		if (a == 1 || a == 2)
			return 1;
		return f(a - 1) + f(a - 2);
	}

	public static void main(String[] args)
	{
		try
		{

			CloudGrid.Start();
			long st = System.currentTimeMillis();
			CGFutureTask<String> fut = CloudGrid.execute(task2.class, "10");
			String ret = fut.get();
			long et = System.currentTimeMillis();
			System.out.println("finnal:" + ret + " time��" + (et - st));

			st = System.currentTimeMillis();
			int sum = 0;
			for (int i = 1; i <= 10; i++)
			{
				sum += f(i);
				// System.out.println(i+" "+sum);
			}
			et = System.currentTimeMillis();
			System.out.println("finnal:" + sum + " time��" + (et - st));
			Runtime.getRuntime().addShutdownHook(new Thread()
			{

				public void run()
				{
					try
					{
						System.out.println("���ڹر�..");
						CloudGrid.Stop();
						sleep(1000);
						System.out.println("�ر�");

					}
					catch (Exception e)
					{
						// e.printStackTrace();
					}
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
