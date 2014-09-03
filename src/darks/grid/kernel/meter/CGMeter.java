package darks.grid.kernel.meter;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.util.CGUtil;

public abstract class CGMeter implements ICGMeter
{

	protected String uuid = CGUtil.getUUID();

	protected int type;

	protected Map<String, ICGJob> jobs = new HashMap<String, ICGJob>();

	protected String ipaddr;

	protected int port;

	protected int infoport;

	protected String taskid;

	protected String nodeid = CGDataStore.getUUID();

	public CGMeter()
	{
	}

	public CGMeter(int type)
	{
		this.type = type;
	}

	public CGMeter(int type, String taskid, ArrayList<ICGJob> jobslist, String ipaddr, int port,
			int infoport)
	{
		this.type = type;
		this.taskid = taskid;
		this.ipaddr = ipaddr;
		this.port = port;
		this.infoport = infoport;
		setJobs(jobslist);
	}

	public String getMeterId()
	{
		return uuid;
	}

	public String getNodeid()
	{
		return nodeid;
	}

	public void setNodeid(String nodeid)
	{
		this.nodeid = nodeid;
	}

	public String getJobId()
	{
		String tmp = null;
		for (String s : jobs.keySet())
		{
			tmp = s;
			break;
		}
		return tmp;
	}

	public ICGJob getJob()
	{
		ICGJob tmp = null;
		for (ICGJob job : jobs.values())
		{
			tmp = job;
			break;
		}
		return tmp;
	}

	public Map<String, ICGJob> getJobs()
	{
		return jobs;
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

	public void setJobs(ArrayList<ICGJob> jobslist)
	{
		jobs.clear();

		for (ICGJob job : jobslist)
		{
			jobs.put(job.getJobId(), job);
		}
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public void setTaskid(String taskid)
	{
		this.taskid = taskid;
	}

	public int getInfoPort()
	{
		return infoport;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		uuid = (String) in.readObject();
		nodeid = (String) in.readObject();
		type = in.readInt();
		taskid = (String) in.readObject();
		ipaddr = (String) in.readObject();
		port = in.readInt();
		infoport = in.readInt();
		jobs = CGUtil.readMap(in);
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(uuid);
		out.writeObject(nodeid);
		out.writeInt(type);
		out.writeObject(taskid);
		out.writeObject(ipaddr);
		out.writeInt(port);
		out.writeInt(infoport);
		CGUtil.writeMap(out, jobs);

	}

}
