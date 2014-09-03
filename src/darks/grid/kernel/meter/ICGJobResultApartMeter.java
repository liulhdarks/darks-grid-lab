package darks.grid.kernel.meter;

import java.io.Externalizable;

import darks.grid.util.CGBytesPackage;

public interface ICGJobResultApartMeter extends Externalizable
{

	public String getResultMeterId();

	public String getId();

	public String getMeterId();

	public CGBytesPackage getPartData();

	public int getIndex();

	public int getNumber();

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
