package darks.grid.kernel.job;

import java.util.concurrent.Callable;

import darks.grid.kernel.control.CGClassControl;
import darks.grid.kernel.meter.ICGMeter;

public class CGJobLinkExecutor implements Callable
{
	private ICGMeter meter;

	public CGJobLinkExecutor(ICGMeter meter)
	{
		this.meter = meter;
	}

	public Object call() throws Exception
	{
		try
		{
			Class c = CGClassControl.getClassLoader(meter.getClassName(), meter.isUpdateClass())
					.loadClass(meter.getClassName(), meter.getLinkPath(), true);
			Object o = meter.getObjectlist()[0];
			return CGClassControl.invokeClass(c, "execute", true, new Class[] { Object.class }, o);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception(e.toString());
		}
	}

}
