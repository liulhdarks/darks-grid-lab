package darks.grid.kernel.service.thread;

import darks.grid.kernel.network.ICGNet;
import darks.grid.kernel.service.CGExecutorService;
import darks.grid.kernel.store.CGDataStore;

public class CGExecutorThread extends Thread
{
	private long execid = this.getId();
	private CGExecutorService service;

	public CGExecutorThread()
	{
	}

	public CGExecutorThread(CGExecutorService service)
	{
		this.service = service;
	}

	public void run()
	{

		ICGNet net = CGDataStore.getNetObj();
		while (!service.isClose())
		{
			try
			{
				net.request();
				sleep(10);
			}
			catch (Exception e)
			{
				if (e.getMessage().compareToIgnoreCase("socket closed") == 0)
					return;
				System.out.println(e.getMessage() + "|" + e.getLocalizedMessage());
			}
		}

	}
}
