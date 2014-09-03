package darks.grid.kernel.job;

import java.util.concurrent.ExecutionException;

import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.meter.ICGJobResultMeter;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.util.CGUtil;

public class CGJobResult implements ICGJobResult
{

	private String uuid = CGUtil.getUUID();

	protected String meterid;

	protected int type;

	protected String taskid;

	protected String nodeid;

	protected String jobid;

	protected boolean isfail;

	protected boolean iscanceled;

	protected String error;

	protected Object result;

	protected String finishtime;

	public CGJobResult(ICGJobResultMeter meter)
	{
		this.type = meter.getType();
		this.taskid = meter.getTaskId();
		this.meterid = meter.getMeterId();
		this.jobid = meter.getJobId();
		this.isfail = meter.isFail();
		this.iscanceled = meter.isCanceled();
		this.error = meter.getError();
		this.result = meter.getResult();
		this.finishtime = meter.getFinishtime();
		this.nodeid = meter.getNodeid();
	}

	public <T> T getData() throws ExecutionException, InterruptedException
	{
		return (T) result;
	}

	public ICGNode getNode()
	{
		if (nodeid.equals(CGDataStore.getUUID()))
			return CGDataStore.getLocalNode();
		return CGDataCenter.getInstance().getCGNode(nodeid);
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

	public String getError()
	{
		return error;
	}

	public String getFinishtime()
	{
		return finishtime;
	}

	public String getNodeid()
	{
		return nodeid;
	}

	public String getJobId()
	{
		return jobid;
	}

	public String getTaskId()
	{
		return taskid;
	}

	public int getType()
	{
		return type;
	}

	public boolean isFail()
	{
		return isfail;
	}

	public boolean isCanceled()
	{
		return iscanceled;
	}

}
