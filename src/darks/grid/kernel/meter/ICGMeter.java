package darks.grid.kernel.meter;

import java.io.Externalizable;
import java.util.Map;

import darks.grid.kernel.ICGJob;

public interface ICGMeter extends Externalizable
{

	public static final int LISTMETER = 1;

	public static final int OBJECTMETER = 2;

	public static final int LINKMETER = 3;

	public String getMeterId();

	public String getJobId();

	public ICGJob getJob();

	public Map<String, ICGJob> getJobs();

	public void setType(int type);

	public String getIPAddress();

	public int getPort();

	public int getInfoPort();

	public String getTaskId();

	public int getType();

	public String getNodeid();

	public String getClassName();

	public String getLinkPath();

	public Object[] getObjectlist();

	public boolean isUpdateClass();

}
