package darks.grid.kernel.meter;

import java.io.Externalizable;

public interface ICGObjectMeter extends Externalizable
{
	public Object getObject();

	public void setObject(Object obj);

	public int getType();

	public void setType(int type);

	public String getId();

	public String getFromHost();

	public int getFromInfoPort();

	public String getFromNodeId();

	public int getFromObjPort();

	public String getToHost();

	public int getToObjRetPort();
}
