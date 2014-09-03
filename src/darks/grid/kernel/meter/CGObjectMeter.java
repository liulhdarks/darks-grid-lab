package darks.grid.kernel.meter;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import darks.grid.kernel.store.CGDataStore;
import darks.grid.util.CGUtil;

public class CGObjectMeter implements ICGObjectMeter
{

	private static final long serialVersionUID = 3815194486648109457L;

	private String uid = CGUtil.getUUID();

	private int type;

	private Object obj;

	private String fromNodeId = CGDataStore.getUUID();

	private String fromHost = CGDataStore.initInfoHost;

	private int fromInfoPort = CGDataStore.initInfoPort;

	private int fromObjPort = CGDataStore.initObjPort;

	private String toHost;

	private int toObjRetPort;

	public CGObjectMeter()
	{

	}

	public CGObjectMeter(int type, Object obj, String toHost, int toObjRetPort)
	{
		this.type = type;
		this.obj = obj;
		this.toHost = toHost;
		this.toObjRetPort = toObjRetPort;
	}

	public String getId()
	{
		return uid;
	}

	public Object getObject()
	{
		return obj;
	}

	public String getFromHost()
	{
		return fromHost;
	}

	public int getFromInfoPort()
	{
		return fromInfoPort;
	}

	public String getFromNodeId()
	{
		return fromNodeId;
	}

	public int getFromObjPort()
	{
		return fromObjPort;
	}

	public String getToHost()
	{
		return toHost;
	}

	public int getToObjRetPort()
	{
		return toObjRetPort;
	}

	public void setObject(Object obj)
	{
		this.obj = obj;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		type = in.readInt();
		obj = in.readObject();

		fromNodeId = (String) in.readObject();
		fromHost = (String) in.readObject();
		fromInfoPort = in.readInt();
		fromObjPort = in.readInt();
		// toHost=(String)in.readObject();
		// toObjRetPort=in.readInt();
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeInt(type);
		out.writeObject(obj);

		out.writeObject(fromNodeId);
		out.writeObject(fromHost);
		out.writeInt(fromInfoPort);
		out.writeInt(fromObjPort);
		// out.writeObject(toHost);
		// out.writeInt(toObjRetPort);
	}

}
