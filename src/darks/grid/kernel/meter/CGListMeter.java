package darks.grid.kernel.meter;

import java.util.ArrayList;

import darks.grid.kernel.ICGJob;

public class CGListMeter extends CGMeter
{

	private static final long serialVersionUID = -5714248672672664354L;

	public CGListMeter()
	{
		super(LISTMETER);
	}

	public CGListMeter(String taskid, ArrayList<ICGJob> jobslist, String ipaddr, int port,
			int infoport)
	{
		super(LISTMETER, taskid, jobslist, ipaddr, port, infoport);
	}

	public String getClassName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getLinkPath()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] getObjectlist()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isUpdateClass()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
