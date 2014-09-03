package darks.grid.kernel.store.job;

import java.util.concurrent.ConcurrentSkipListMap;

import darks.grid.kernel.meter.ICGObjectMeter;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.util.CGUtil;

public class CGJobReResponseStorePackage
{

	private String uuid = CGUtil.getUUID();

	private String meterretId;

	private ConcurrentSkipListMap<Integer, ICGObjectMeter> apartList = new ConcurrentSkipListMap<Integer, ICGObjectMeter>();

	private boolean isfinsh = false;

	private Object mutex = new Object();

	public CGJobReResponseStorePackage(String meterretId)
	{
		this.meterretId = meterretId;
	}

	public boolean addApartJobResult(int index, ICGObjectMeter jobResult) throws Exception
	{
		// synchronized (mutex) {
		if (!apartList.containsKey(index))
		{
			apartList.put(index, jobResult);
			return true;
		}
		return false;
		// }

	}

	public ICGObjectMeter getObjectMeter(int index)
	{
		// synchronized (mutex) {
		if (apartList.containsKey(index))
		{
			return apartList.get(index);
		}
		return null;
		// }
	}

	public void reResponse(int index)
	{
		// synchronized (mutex) {
		if (apartList.containsKey(index))
		{
			ICGObjectMeter meter = apartList.get(index);
			CGDataStore.getNetObj().response(meter.getToHost(), meter.getToObjRetPort(), meter);
		}
		// }
	}

	public String getPackageId()
	{
		return uuid;
	}

	public String getResultMeterId()
	{
		return meterretId;
	}

	public boolean isFinshed()
	{
		synchronized (mutex)
		{
			return isfinsh;
		}
	}

}
