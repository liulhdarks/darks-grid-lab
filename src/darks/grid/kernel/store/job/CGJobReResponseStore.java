package darks.grid.kernel.store.job;

import java.util.concurrent.ConcurrentSkipListMap;

import darks.grid.kernel.meter.ICGObjectMeter;

public class CGJobReResponseStore
{

	private static ConcurrentSkipListMap<String, CGJobReResponseStorePackage> pack_map = new ConcurrentSkipListMap<String, CGJobReResponseStorePackage>();

	public static void addObjectMeter(ICGObjectMeter meter, int index, String meterretId)
			throws Exception
	{
		// synchronized (pack_map) {
		if (!pack_map.containsKey(meterretId))
		{
			CGJobReResponseStorePackage jobpack = new CGJobReResponseStorePackage(meterretId);
			jobpack.addApartJobResult(index, meter);
			pack_map.put(meterretId, jobpack);
			// System.out.println("创建ObjectMeter："+meterretId);
		}
		else
		{
			CGJobReResponseStorePackage jobpack = pack_map.get(meterretId);
			jobpack.addApartJobResult(index, meter);
			// System.out.println("添加至ObjectMeter："+meterretId+" 编号："+index);
		}
		// }
	}

	public static void delReResponseStorePackage(String meterretId)
	{
		if (pack_map.containsKey(meterretId))
			pack_map.remove(meterretId);
	}

	public static ICGObjectMeter getObjectMeter(int index, String meterretId)
	{
		// synchronized (pack_map) {
		if (pack_map.containsKey(meterretId))
		{
			CGJobReResponseStorePackage jobpack = pack_map.get(meterretId);
			return jobpack.getObjectMeter(index);
		}
		return null;
		// }
	}

	public static void reResponse(int index, String meterretId)
	{
		// synchronized (pack_map) {
		if (pack_map.containsKey(meterretId))
		{
			CGJobReResponseStorePackage jobpack = pack_map.get(meterretId);
			jobpack.reResponse(index);
		}
		// }
	}

	public static void processReresponseMsg(String[] args)
	{
		try
		{
			String meterid = args[2];
			if (meterid == null || meterid.equals(""))
				return;
			String[] m = args;
			// String[] m=msg.split("_");
			for (int i = 3; i < m.length; i++)
			{
				// System.out.println(m[i]);
				String mt = m[i];
				if (m[i].equals(""))
					continue;
				String[] d = mt.split("-");
				int s = Integer.parseInt(d[0]);
				int e = Integer.parseInt(d[1]);
				for (int j = s; j <= e; j++)
				{
					// System.out.print(j+" ");
					reResponse(j, meterid);
				}
				// System.out.print("\n");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
