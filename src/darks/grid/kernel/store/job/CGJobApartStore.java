package darks.grid.kernel.store.job;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import darks.grid.kernel.job.CGApartJobPackage;
import darks.grid.kernel.meter.ICGJobResultApartMeter;

public class CGJobApartStore
{
	private ConcurrentSkipListMap<String, CGApartJobPackage> map = new ConcurrentSkipListMap<String, CGApartJobPackage>();

	private static CGJobApartStore instance = null;

	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock read = readWriteLock.readLock();
	private final Lock write = readWriteLock.writeLock();

	public CGApartJobPackage getApartJobPackage(String meterretID)
	{
		read.lock();
		try
		{
			// synchronized (map) {
			if (!map.containsKey(meterretID))
				return null;
			return map.get(meterretID);

			// }
		}
		finally
		{
			read.unlock();
		}
	}

	public CGApartJobPackage addJobResultApartMeter(ICGJobResultApartMeter meter, String host,
			int infoport, int objport) throws Exception
	{
		write.lock();
		try
		{
			// synchronized (map) {
			if (!map.containsKey(meter.getResultMeterId()))
			{
				CGApartJobPackage jobpack = new CGApartJobPackage(meter.getResultMeterId(),
						meter.getNumber(), host, infoport, objport);
				jobpack.addApartJobResult(meter);
				map.put(meter.getResultMeterId(), jobpack);
				return jobpack;
			}
			else
			{
				CGApartJobPackage jobpack = map.get(meter.getResultMeterId());
				jobpack.addApartJobResult(meter);
				return jobpack;
			}
			// }
		}
		finally
		{
			write.unlock();
		}
	}

	public void delApartJobPackage(String meterretID)
	{
		write.lock();
		try
		{
			synchronized (map)
			{
				if (!map.containsKey(meterretID))
					return;
				map.remove(meterretID);
			}
		}
		finally
		{
			write.unlock();
		}
	}

	public static synchronized CGJobApartStore getInstance()
	{
		if (instance == null)
		{
			instance = new CGJobApartStore();
		}
		return instance;
	}
}
