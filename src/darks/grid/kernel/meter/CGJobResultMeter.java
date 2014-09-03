package darks.grid.kernel.meter;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Map;

import darks.grid.kernel.Impl.CGFutureJobTask;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.util.CGUtil;

public class CGJobResultMeter implements ICGJobResultMeter
{

	private static final long serialVersionUID = -7293870628427660977L;

	protected String uuid = CGUtil.getUUID();

	protected String meterid;

	protected int type;

	protected String ipaddr;

	protected int port;

	protected String taskid;

	protected String nodeid = CGDataStore.getUUID();

	protected String jobid;

	protected boolean isfail;

	protected boolean iscanceled;

	protected String error;

	protected Object result;

	protected String finishtime = CGUtil.getTime();

	public CGJobResultMeter()
	{
	}

	public CGJobResultMeter(CGFutureJobTask fut, Object obj, boolean isfail, String strerr)
	{
		this.type = fut.getType();
		this.taskid = fut.getTaskId();
		this.ipaddr = CGDataStore.initInfoHost;
		this.port = CGDataStore.initObjPort;
		this.meterid = fut.getMeterId();
		this.jobid = fut.getJobid();
		this.isfail = isfail;
		this.iscanceled = fut.isCancelled();
		this.error = strerr;
		this.result = obj;
	}

	public String getId()
	{
		return uuid;
	}

	public String getMeterId()
	{
		return meterid;
	}

	public Object getResult()
	{
		return result;
	}

	public void setResult(Object result)
	{
		this.result = result;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public String getFinishtime()
	{
		return finishtime;
	}

	public void setFinishtime(String finishtime)
	{
		this.finishtime = finishtime;
	}

	public void setMeterId(String meterid)
	{
		this.meterid = meterid;
	}

	public String getNodeid()
	{
		return nodeid;
	}

	public String getJobId()
	{
		return jobid;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getIPAddress()
	{
		return ipaddr;
	}

	public int getPort()
	{
		return port;
	}

	public String getTaskId()
	{
		return taskid;
	}

	public int getType()
	{
		return type;
	}

	public void setIpaddr(String ipaddr)
	{
		this.ipaddr = ipaddr;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public void setTaskid(String taskid)
	{
		this.taskid = taskid;
	}

	public boolean isFail()
	{
		return isfail;
	}

	public boolean isCanceled()
	{
		return iscanceled;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		uuid = (String) in.readObject();
		meterid = (String) in.readObject();
		type = in.readInt();
		nodeid = (String) in.readObject();
		jobid = (String) in.readObject();
		taskid = (String) in.readObject();
		ipaddr = (String) in.readObject();
		port = in.readInt();
		isfail = in.readBoolean();
		iscanceled = in.readBoolean();
		error = (String) in.readObject();
		finishtime = (String) in.readObject();

		int type = in.readInt();
		if (type == 1)
		{
			result = CGUtil.readCollection(in);
		}
		else if (type == 2)
		{
			result = CGUtil.readMap(in);
		}
		else if (type == 3)
		{
			result = CGUtil.readByteArray(in);
		}
		else if (type == 4)
		{
			result = CGUtil.readArray(in);
		}
		else
		{
			result = in.readObject();
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(uuid);
		out.writeObject(meterid);
		out.writeInt(type);
		out.writeObject(nodeid);
		out.writeObject(jobid);
		out.writeObject(taskid);
		out.writeObject(ipaddr);
		out.writeInt(port);
		out.writeBoolean(isfail);
		out.writeBoolean(iscanceled);
		out.writeObject(error);
		out.writeObject(finishtime);

		if (result instanceof Collection)
		{
			out.writeInt(1);
			CGUtil.writeCollection(out, (Collection) result);
		}
		else if (result instanceof Map)
		{
			out.writeInt(2);
			CGUtil.writeMap(out, (Map) result);
		}
		else if (result instanceof byte[])
		{
			out.writeInt(3);
			CGUtil.writeByteArray(out, (byte[]) result);
		}
		else if (result instanceof Object[])
		{
			out.writeInt(4);
			CGUtil.writeArray(out, (Object[]) result);
		}
		else
		{
			out.writeInt(5);
			out.writeObject(result);
		}
	}

}
