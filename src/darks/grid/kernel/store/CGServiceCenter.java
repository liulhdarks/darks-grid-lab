package darks.grid.kernel.store;

import darks.grid.kernel.service.CGCheckService;
import darks.grid.kernel.service.CGExecutorResultService;
import darks.grid.kernel.service.CGExecutorService;
import darks.grid.kernel.service.CGInfoService;
import darks.grid.kernel.service.thread.CGProtectThread;

public class CGServiceCenter
{
	private CGCheckService chksv = null;
	private CGInfoService infosv = null;
	private CGExecutorService execsv = null;
	private CGExecutorResultService rltsv = null;
	private static CGServiceCenter instance = null;

	private CGServiceCenter()
	{

	}

	public void initService()
	{
		chksv = new CGCheckService();
		infosv = new CGInfoService();
		execsv = new CGExecutorService();
		rltsv = new CGExecutorResultService();

	}

	public void StartService(int type) throws Exception
	{
		execsv.Start(type);
		rltsv.Start(type);
		chksv.Start(type);
		infosv.Start(type);
		CGThreadStore.getInstance().getExecutorService(CGThreadStore.TASK_THREAD)
				.execute(new CGProtectThread());
	}

	public void StopService() throws Exception
	{
		if (execsv != null)
			execsv.Close();
		execsv = null;
		if (rltsv != null)
			rltsv.Close();
		rltsv = null;
		if (chksv != null)
			chksv.Close();
		chksv = null;
		if (infosv != null)
			infosv.Close();
		infosv = null;
	}

	public static CGServiceCenter getInstance()
	{
		if (instance == null)
			instance = new CGServiceCenter();
		return instance;
	}
}
