package darks.grid.kernel.meter;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.util.CGUtil;

public class CGLinkMeter implements ICGMeter
{

	private static final long serialVersionUID = 6652557827143778733L;
	private String meterId = CGUtil.getUUID();
	private String clsName;
	private String linkPath;
	private boolean updateCls;
	private Object[] objlist;
	private int type;
	private String ipaddr;
	private int port;
	private int infoport;
	private String nodeid = CGDataStore.getUUID();;
	private String taskId;
	private String jobid;

	// private ICGJob job;
	public CGLinkMeter()
	{
	}

	public CGLinkMeter(String taskId, ICGJob job, String clsName, String linkPath,
			boolean updateCls, Object[] objs)
	{
		this.clsName = clsName;
		this.linkPath = linkPath;
		this.updateCls = updateCls;
		this.objlist = objs;
		type = LINKMETER;
		ipaddr = CGDataStore.initInfoHost;
		port = CGDataStore.initObjRltPort;
		infoport = CGDataStore.initInfoPort;
		this.taskId = taskId;
		this.jobid = job.getJobId();
		// this.job=job;
	}

	public int getInfoPort()
	{
		return infoport;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		meterId = (String) in.readObject();
		clsName = (String) in.readObject();
		linkPath = (String) in.readObject();
		updateCls = in.readBoolean();
		objlist = CGUtil.readArray(in);
		type = in.readInt();
		ipaddr = (String) in.readObject();
		port = in.readInt();
		infoport = in.readInt();
		nodeid = (String) in.readObject();
		taskId = (String) in.readObject();
		jobid = (String) in.readObject();
		// job=(ICGJob)in.readObject();
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(meterId);
		out.writeObject(clsName);
		out.writeObject(linkPath);
		out.writeBoolean(updateCls);
		CGUtil.writeArray(out, objlist);
		out.writeInt(type);
		out.writeObject(ipaddr);
		out.writeInt(port);
		out.writeInt(infoport);
		out.writeObject(nodeid);
		out.writeObject(taskId);
		out.writeObject(jobid);
		// out.writeObject(job);
	}

	public String getIPAddress()
	{
		return ipaddr;
	}

	public String getClassName()
	{
		return clsName;
	}

	public String getLinkPath()
	{
		return linkPath;
	}

	public Object[] getObjectlist()
	{
		return objlist;
	}

	public boolean isUpdateClass()
	{
		return updateCls;
	}

	public ICGJob getJob()
	{
		return null;
	}

	public String getJobId()
	{
		return jobid;
	}

	public Map<String, ICGJob> getJobs()
	{
		Map<String, ICGJob> tmp = new HashMap<String, ICGJob>();
		// //tmp.put(job.getJobId(), job);
		return tmp;
	}

	public String getMeterId()
	{
		return meterId;
	}

	public String getNodeid()
	{
		return nodeid;
	}

	public int getPort()
	{
		return port;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{

		this.type = type;
	}
}
