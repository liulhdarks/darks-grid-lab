package darks.grid.kernel.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import darks.grid.kernel.meter.ICGJobResultApartMeter;
import darks.grid.kernel.meter.ICGJobResultMeter;
import darks.grid.kernel.store.CGThreadStore;
import darks.grid.kernel.thread.CGApartJobPackageListener;
import darks.grid.util.CGBytesPackage;
import darks.grid.util.CGUtil;

public class CGApartJobPackage
{
	private String uuid = CGUtil.getUUID();

	private String meterretId;

	private int allnum;

	private ConcurrentSkipListMap<Integer, ICGJobResultApartMeter> apartList = new ConcurrentSkipListMap<Integer, ICGJobResultApartMeter>();

	private boolean isfinsh = false;

	private boolean isComplete = false;

	private Object mutex = new Object();

	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock read = readWriteLock.readLock();
	private final Lock write = readWriteLock.writeLock();

	private final Lock lock = new ReentrantLock();

	private List<String> indexrs = new ArrayList<String>(allnum);

	private long curtime;

	private String host;

	private int infoport;

	private int objport;

	public CGApartJobPackage(String meterretId, int allnum, String host, int infoport, int objport)
	{
		this.meterretId = meterretId;
		this.allnum = allnum;
		this.host = host;
		this.infoport = infoport;
		this.objport = objport;
		for (int i = 1; i <= allnum; i++)
		{
			indexrs.add(String.valueOf(i));
		}
		curtime = System.currentTimeMillis();
		CGThreadStore.getInstance().getExecutorService(CGThreadStore.TASK_THREAD)
				.execute(new CGApartJobPackageListener(this));
		// System.out.println("创建包："+meterretId+" 数量："+allnum);
	}

	public boolean addApartJobResult(ICGJobResultApartMeter jobResult) throws Exception
	{
		write.lock();
		try
		{
			// synchronized (mutex) {
			if (!apartList.containsKey(jobResult.getIndex())
					&& meterretId.equals(jobResult.getResultMeterId()))
			{
				apartList.put(jobResult.getIndex(), jobResult);
				indexrs.remove(String.valueOf(jobResult.getIndex()));
				curtime = System.currentTimeMillis();
				// System.out.println(meterretId+"包："+apartList.size()+" 数量："+allnum);
				if (allnum == apartList.size())
					isfinsh = true;
				return true;
			}
			return false;
			// }
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
			// synchronized (mutex) {
			return isfinsh;
			// }
		}
		finally
		{
			read.unlock();
		}
	}

	public long getCurtime()
	{
		return curtime;
	}

	public List<String> getIndexrs()
	{
		read.lock();
		try
		{
			// synchronized (mutex) {
			return indexrs;
			// }
		}
		finally
		{
			read.unlock();
		}
	}

	public List<ICGJobResultApartMeter> getResultList()
	{
		List<ICGJobResultApartMeter> list = new ArrayList<ICGJobResultApartMeter>(allnum);
		for (int i = 1; i <= allnum; i++)
		{
			ICGJobResultApartMeter meter = apartList.get(i);
			if (meter == null)
				continue;
			list.add(meter);
		}

		return list;
	}

	public List<CGBytesPackage> getApartDataList()
	{
		List<CGBytesPackage> list = new ArrayList<CGBytesPackage>(allnum);
		for (int i = 1; i <= allnum; i++)
		{
			ICGJobResultApartMeter meter = apartList.get(i);
			if (meter == null)
				continue;
			list.add(meter.getPartData());
		}
		return list;
	}

	public ICGJobResultMeter getResultMeter()
	{
		lock.lock();
		try
		{
			isComplete = true;
			List<CGBytesPackage> list = getApartDataList();
			if (list.size() == 0)
				return null;
			if (list.size() < allnum)
				return null;
			byte[] bytes = CGUtil.makeupByteArray(list);
			ICGJobResultMeter ret = (ICGJobResultMeter) CGUtil.ByteToObject(bytes);
			return ret;
		}
		finally
		{
			lock.unlock();
		}
	}

	public boolean isComplete()
	{
		lock.lock();
		try
		{
			return isComplete;
		}
		finally
		{
			lock.unlock();
		}
	}

	public String getMeterResultId()
	{
		return meterretId;
	}

	public void setMeterResultId(String meterretId)
	{
		this.meterretId = meterretId;
	}

	public String getHost()
	{
		return host;
	}

	public int getInfoport()
	{
		return infoport;
	}

	public int getObjport()
	{
		return objport;
	}
}
