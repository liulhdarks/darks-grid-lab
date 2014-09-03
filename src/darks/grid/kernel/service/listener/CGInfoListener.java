package darks.grid.kernel.service.listener;

import darks.grid.kernel.network.ICGEvent;
import darks.grid.kernel.network.ICGListener;
import darks.grid.kernel.service.CGInfoService;
import darks.grid.kernel.store.CGDataCenter;

public class CGInfoListener implements ICGListener
{
	private CGInfoService service = null;

	public CGInfoListener(CGInfoService service)
	{
		this.service = service;
	}

	@Override
	public void execute(ICGEvent e)
	{
		// byte[] data=e.getData();
		// String info=new String(data);
		// System.out.println(">>"+e.getAddress()+":"+e.getPort()+" "+info);
		try
		{
			CGDataCenter.getInstance().enInfoQueue(e);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
