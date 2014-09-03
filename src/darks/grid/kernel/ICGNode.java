package darks.grid.kernel;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import darks.grid.kernel.meter.ICGMeter;

public interface ICGNode extends Cloneable
{
	public String getUUId();

	public Object getNodeInfo(Object key);

	public void setNodeInfo(Object key, Object obj);

	public Map<Object, Object> getStatus();

	public int getJobSize();

	public String getAddress();

	public int getInfoPort();

	public ConcurrentSkipListMap<String, ICGMeter> getJobs();

	public Object clone();
}
