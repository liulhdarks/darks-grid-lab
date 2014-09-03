package darks.grid.kernel.meter;

import java.io.Externalizable;

public interface ICGJobResultMeter extends Externalizable
{
	public String getId();

	public String getMeterId();

	public Object getResult();

	public String getError();

	public String getFinishtime();

	public String getNodeid();

	public String getJobId();

	public String getIPAddress();

	public int getPort();

	public String getTaskId();

	public int getType();

	public boolean isFail();

	public boolean isCanceled();
}
