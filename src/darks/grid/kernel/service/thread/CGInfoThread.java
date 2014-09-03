package darks.grid.kernel.service.thread;

import darks.grid.kernel.network.ICGNet;
import darks.grid.kernel.service.CGInfoService;
import darks.grid.kernel.store.CGDataStore;

public class CGInfoThread extends Thread
{
	private long infoid = this.getId();
	private CGInfoService service;

	public CGInfoThread()
	{
	}

	public CGInfoThread(CGInfoService service)
	{
		this.service = service;
	}

	public void run()
	{

		ICGNet net = CGDataStore.getNetInfo();
		while (!service.isClose())
		{
			try
			{
				net.request();
				sleep(10);
			}
			catch (Exception e)
			{
				if (e.getMessage().equals("socket closed"))
					return;
				System.out.println(e.getMessage() + "|" + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}

	}

}
