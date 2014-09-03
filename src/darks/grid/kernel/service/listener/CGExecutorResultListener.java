package darks.grid.kernel.service.listener;

import darks.grid.kernel.network.ICGEvent;
import darks.grid.kernel.network.ICGListener;
import darks.grid.kernel.service.CGExecutorResultService;
import darks.grid.kernel.service.thread.CGJobResultProcessThread;
import darks.grid.kernel.store.CGThreadStore;

public class CGExecutorResultListener implements ICGListener
{

	@SuppressWarnings("unused")
	private CGExecutorResultService service = null;

	public CGExecutorResultListener(CGExecutorResultService service)
	{
		this.service = service;
	}

	@Override
	public void execute(ICGEvent e)
	{
		try
		{

			CGThreadStore.getInstance().getExecutorService(CGThreadStore.TASK_THREAD)
					.execute(new CGJobResultProcessThread(e));
		}
		catch (Exception ex)
		{
			System.out.println(ex.toString());
		}
	}

}
