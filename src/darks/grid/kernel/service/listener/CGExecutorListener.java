package darks.grid.kernel.service.listener;

import darks.grid.kernel.network.ICGEvent;
import darks.grid.kernel.network.ICGListener;
import darks.grid.kernel.service.CGExecutorService;
import darks.grid.kernel.store.CGDataCenter;

public class CGExecutorListener implements ICGListener
{
	private CGExecutorService service = null;

	public CGExecutorListener(CGExecutorService service)
	{
		this.service = service;
	}

	@Override
	public void execute(ICGEvent e)
	{
		try
		{
			CGDataCenter.getInstance().enExecQueue(e);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
