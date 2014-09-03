package darks.grid.kernel.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.meter.ICGMeter;
import darks.grid.util.CGUtil;

public class CGJobPackage
{
	private String uuid = CGUtil.getUUID();

	private String taskId;

	private ConcurrentMap<String, ICGJob> jobMap = new ConcurrentHashMap<String, ICGJob>();
	private ConcurrentMap<String, ICGMeter> meterMap = new ConcurrentHashMap<String, ICGMeter>();
	// private volatile ConcurrentMap<String,ICGJobResult> resultMap=new
	// ConcurrentHashMap<String,ICGJobResult>();

	private ConcurrentSkipListMap<String, ICGJobResult> resultList = new ConcurrentSkipListMap<String, ICGJobResult>();

	private boolean isfinsh = false;

	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock read = readWriteLock.readLock();
	private final Lock write = readWriteLock.writeLock();

	public CGJobPackage(String taskId)
	{
		this.taskId = taskId;
	}

	public boolean addJob(ICGJob job)
	{
		if (!jobMap.containsKey(job.getJobId()))
		{
			jobMap.put(job.getJobId(), job);
			return true;
		}
		return false;
	}

	public boolean addJob(ICGJob job, ICGMeter meter)
	{
		if (!jobMap.containsKey(job.getJobId()))
		{
			jobMap.put(job.getJobId(), job);
			meterMap.put(job.getJobId(), meter);
			return true;
		}
		return false;
	}

	public boolean addJobResult(ICGJobResult jobResult) throws Exception
	{
		if (isFinshed())
			return false;
		write.lock();
		try
		{
			if (jobMap.containsKey(jobResult.getJobId()))
			{
				resultList.put(jobResult.getJobId(), jobResult);
				ConcurrentSkipListMap<String, ICGMeter> map = jobResult.getNode().getJobs();
				if (resultList.size() == jobMap.size())
					isfinsh = true;
				// System.out.println("task ID:"+jobResult.getTaskId()+" data:"+jobResult.getData()+" "+resultList.size()+"/"+jobMap.size()+" node job:"+jobResult.getNode().getJobSize());
				System.out.println("任务" + jobResult.getTaskId() + "数据完成" + resultList.size() + "/"
						+ jobMap.size());
				if (map.containsKey(jobResult.getJobId()))
					map.remove(jobResult.getJobId());

				return true;
			}
			return false;
		}
		finally
		{
			write.unlock();
		}
	}

	public String getPackageId()
	{
		return uuid;
	}

	public boolean isFinshed()
	{
		read.lock();
		try
		{
			return isfinsh;
		}
		finally
		{
			read.unlock();
		}
	}

	public ConcurrentMap<String, ICGJob> getJobMap()
	{
		return jobMap;
	}

	public ConcurrentMap<String, ICGJobResult> getJobResultMap()
	{
		return resultList;
	}

	public ICGMeter getMeterByJobId(String jobID)
	{
		return meterMap.get(jobID);
	}

	public List<ICGJobResult> getResultList()
	{
		read.lock();
		try
		{
			List<ICGJobResult> list = new ArrayList<ICGJobResult>();
			list.addAll(resultList.values());

			return list;
		}
		finally
		{
			read.unlock();
		}
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
}
