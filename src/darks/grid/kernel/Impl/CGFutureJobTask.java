package darks.grid.kernel.Impl;

import java.util.concurrent.Callable;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.meter.ICGMeter;

public class CGFutureJobTask extends CGFutureTask<Object>
{

	private ICGJob job = null;

	private int type;

	private String ipaddress;

	private int port;

	private String taskId;

	private String jobid;

	private String meterId;

	private String nodeid;

	@SuppressWarnings("unchecked")
	public CGFutureJobTask(ICGJob callable)
	{
		super(callable);
		job = callable;
	}

	@SuppressWarnings("unchecked")
	public CGFutureJobTask(ICGMeter meter, ICGJob callable)
	{
		super(callable);
		job = callable;
		jobid = meter.getJobId();
		type = meter.getType();
		ipaddress = meter.getIPAddress();
		port = meter.getPort();
		taskId = meter.getTaskId();
		meterId = meter.getMeterId();
		nodeid = meter.getNodeid();
	}

	@SuppressWarnings("unchecked")
	public CGFutureJobTask(ICGMeter meter, ICGJob job, Callable<Object> callable)
	{
		super(callable);
		this.job = job;
		jobid = meter.getJobId();
		type = meter.getType();
		ipaddress = meter.getIPAddress();
		port = meter.getPort();
		taskId = meter.getTaskId();
		meterId = meter.getMeterId();
		nodeid = meter.getNodeid();
	}

	public ICGJob getJob()
	{
		return job;
	}

	public void setJob(ICGJob job)
	{
		this.job = job;
	}

	public String getJobid()
	{
		return jobid;
	}

	public void setJobid(String jobid)
	{
		this.jobid = jobid;
	}

	public String getMeterId()
	{
		return meterId;
	}

	public void setMeterId(String meterId)
	{
		this.meterId = meterId;
	}

	public String getNodeid()
	{
		return nodeid;
	}

	public void setNodeid(String nodeid)
	{
		this.nodeid = nodeid;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getIpaddress()
	{
		return ipaddress;
	}

	public void setIpaddress(String ipaddress)
	{
		this.ipaddress = ipaddress;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
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
