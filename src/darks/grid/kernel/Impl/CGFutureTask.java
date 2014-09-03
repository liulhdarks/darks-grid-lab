package darks.grid.kernel.Impl;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import darks.grid.util.CGUtil;

public class CGFutureTask<V> extends FutureTask<V>
{

	protected String uuid = CGUtil.getUUID();

	public CGFutureTask(Callable<V> callable)
	{
		super(callable);
	}

	public CGFutureTask(Runnable runnable, V result)
	{
		super(runnable, result);
	}

	String getUUID()
	{
		return uuid;
	}

	public void await() throws InterruptedException
	{
		finishLatch.await();
	}

	public void await(long timeout, TimeUnit unit) throws InterruptedException
	{
		finishLatch.await(timeout, unit);
	}

	public boolean isDead()
	{
		return finishLatch.getCount() == 0;
	}

	@Override
	public void run()
	{
		try
		{
			super.run();
		}
		finally
		{
			finishLatch.countDown();
		}
	}

	private final CountDownLatch finishLatch = new CountDownLatch(1);

}
