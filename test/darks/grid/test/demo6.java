package darks.grid.test;

import darks.grid.ICGLinkTask;

public class demo6 implements ICGLinkTask
{

	private static final long serialVersionUID = 7622738103754344793L;

	public Object execute(Object objs)
	{
		Object[] st = (Object[]) objs;
		int s = Integer.parseInt((String) st[0]);
		int e = Integer.parseInt((String) st[1]);

		long sum = 0;
		for (int i = s; i <= e; i++)
		{
			long t = f(i);
			System.out.println(">>" + t + " (" + i + ")");
			sum += t;
		}
		return sum;
	}

	public int f(Integer a)
	{
		if (a == 1 | a == 2)
			return 1;
		return f(a - 1) + f(a - 2);
	}

}
