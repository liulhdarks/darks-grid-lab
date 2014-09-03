package darks.grid.kernel.Impl;

import java.util.concurrent.ConcurrentSkipListMap;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.meter.ICGMeter;

public class CGNode implements ICGNode
{

	public static final String HOSTIP = "HOSTIP";

	public static final String INFOPORT = "INFOPORT";

	public static final String OBJPORT = "OBJPORT";

	public static final String RLTPORT = "RLTPORT";

	public static final String HTTPPORT = "HTTPPORT";

	private String uuid;
	private ConcurrentSkipListMap<Object, Object> info = new ConcurrentSkipListMap<Object, Object>();
	private ConcurrentSkipListMap<String, ICGMeter> meter_map = new ConcurrentSkipListMap<String, ICGMeter>();

	public CGNode(String uuid)
	{
		this.uuid = uuid;
	}

	public Object getNodeInfo(Object key)
	{
		return info.get(key);
	}

	public ConcurrentSkipListMap<Object, Object> getStatus()
	{
		return info;
	}

	public String getUUId()
	{
		return uuid;
	}

	public String getAddress()
	{
		return (String) getNodeInfo(HOSTIP);
	}

	public int getInfoPort()
	{
		return (Integer) getNodeInfo(INFOPORT);
	}

	public void setNodeInfo(Object key, Object obj)
	{
		info.put(key, obj);
	}

	public int getJobSize()
	{
		synchronized (meter_map)
		{
			return meter_map.size();
		}
	}

	public synchronized ConcurrentSkipListMap<String, ICGMeter> getJobs()
	{
		return meter_map;
	}

	public Object clone()
	{
		CGNode node = null;
		try
		{
			node = (CGNode) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return node;
	}

}
