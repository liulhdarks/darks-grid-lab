package darks.grid.kernel.service.thread;

import darks.grid.kernel.network.ICGNet;
import darks.grid.kernel.service.CGExecutorResultService;
import darks.grid.kernel.store.CGDataStore;

public class CGExecutorResultThread extends Thread
{
	private long execid = this.getId();
	private CGExecutorResultService service;

	public CGExecutorResultThread()
	{
	}

	public CGExecutorResultThread(CGExecutorResultService service)
	{
		this.service = service;
	}

	public void run()
	{

		ICGNet net = CGDataStore.getNetResult();
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
				e.printStackTrace();
			}
		}

	}
}
