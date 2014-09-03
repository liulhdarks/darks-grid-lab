package darks.grid.kernel.meter;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import darks.grid.util.CGBytesPackage;
import darks.grid.util.CGUtil;

public class CGJobResultApartMeter implements ICGJobResultApartMeter
{

	private static final long serialVersionUID = -1840961928560792160L;

	protected String uuid = CGUtil.getUUID();

	protected String meteretrid;

	protected String meterid;

	protected int type;

	protected String ipaddr;

	protected int port;

	protected String taskid;

	protected String nodeid;

	protected String jobid;

	protected boolean isfail;

	protected boolean iscanceled;

	protected String error;

	protected CGBytesPackage data;

	protected int number;

	protected int index;

	protected String finishtime;

	public CGJobResultApartMeter()
	{
	}

	public CGJobResultApartMeter(ICGJobResultMeter retmeter, CGBytesPackage data, int index, int num)
	{
		this.type = retmeter.getType();
		this.taskid = retmeter.getTaskId();
		this.ipaddr = retmeter.getIPAddress();
		this.port = retmeter.getPort();
		this.meterid = retmeter.getMeterId();
		this.jobid = retmeter.getJobId();
		this.isfail = retmeter.isFail();
		this.iscanceled = retmeter.isCanceled();
		this.error = retmeter.getError();
		this.meteretrid = retmeter.getId();
		this.finishtime = retmeter.getFinishtime();
		this.data = data;
		this.index = index;
		this.number = num;
		this.nodeid = retmeter.getNodeid();
	}

	public String getId()
	{
		return uuid;
	}

	public String getMeterId()
	{
		return meterid;
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

	public int getIndex()
	{
		return index;
	}

	public int getNumber()
	{
		return number;
	}

	public CGBytesPackage getPartData()
	{
		return data;
	}

	public String getResultMeterId()
	{
		return meteretrid;
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
		meteretrid = (String) in.readObject();
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
		data = (CGBytesPackage) in.readObject();
		index = in.readInt();
		number = in.readInt();
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(uuid);
		out.writeObject(meteretrid);
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
		out.writeObject(data);
		out.writeInt(index);
		out.writeInt(number);

	}

}
