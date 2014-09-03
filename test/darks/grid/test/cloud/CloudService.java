package darks.grid.test.cloud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import darks.grid.CloudGrid;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGFutureTask;
import darks.grid.kernel.factory.CGNodeFactory;

public class CloudService
{

	public static List<String[]> executeQuery(String sql, String[] param) throws Exception
	{
		if (!CloudGrid.isStart())
			CloudGrid.Start();
		Object[] s = null;
		s = new Object[] { sql, "query", param };
		Object[] objs = new Object[] { s };
		List<ICGNode> list = CGNodeFactory.getInstance().getNodes();
		Map<String, ICGNode> map = new HashMap<String, ICGNode>();
		ICGNode local = null;
		for (ICGNode node : list)
		{
			if (map.containsKey(node.getAddress()))
				continue;
			if (node.getUUId().equals(CGNodeFactory.getInstance().getLocalNode().getUUId()))
			{
				local = node;
				continue;
			}
			map.put(node.getAddress(), node);
		}
		list.clear();
		list.addAll(map.values());
		if (local == null)
			local = CGNodeFactory.getInstance().getLocalNode();
		if (local != null)
		{
			if (!map.containsKey(local.getAddress()))
			{
				list.add(local);
			}
		}
		CGFutureTask<List<String[]>> fut = CloudGrid.executeArray(list, StoreQueryAdapter.class,
				StoreQueryTask.class, false, objs);
		long st = System.currentTimeMillis();
		List<String[]> ret = fut.get();
		long et = System.currentTimeMillis();
		System.out.println("CloudService executeQuery time��" + (et - st));
		return ret;
	}

	public static List<String[]> executeQueryAll(String sql, String[] param) throws Exception
	{
		if (!CloudGrid.isStart())
			CloudGrid.Start(CloudService.class.getResource("/cloudgrid.xml").getFile());
		Object[] s = null;
		s = new Object[] { sql, "query", param };
		Object[] objs = new Object[] { s };
		List<ICGNode> list = CGNodeFactory.getInstance().getNodes();
		Map<String, ICGNode> map = new HashMap<String, ICGNode>();
		for (ICGNode node : list)
		{
			if (map.containsKey(node.getAddress()))
				continue;
			map.put(node.getAddress(), node);
		}
		list.clear();
		list.addAll(map.values());
		CGFutureTask<List<String[]>> fut = CloudGrid.executeArray(list, StoreQueryAllAdapter.class,
				StoreQueryTask.class, true, objs);
		long st = System.currentTimeMillis();
		List<String[]> ret = fut.get();
		long et = System.currentTimeMillis();
		System.out.println("CloudService executeQuery time��" + (et - st));
		return ret;
	}

	public static void executeUpdate(String sql) throws Exception
	{
		if (!CloudGrid.isStart())
			CloudGrid.Start(CloudService.class.getResource("/cloudgrid.xml").getFile());
		Object[] s = null;
		s = new Object[] { sql, "update" };
		Object[] objs = new Object[] { s };
		List<ICGNode> list = CGNodeFactory.getInstance().getNodes();
		Map<String, ICGNode> map = new HashMap<String, ICGNode>();
		for (ICGNode node : list)
		{
			if (map.containsKey(node.getAddress()))
				continue;
			map.put(node.getAddress(), node);
		}
		list.clear();
		list.addAll(map.values());
		CGFutureTask<String> fut = CloudGrid.executeArray(list, StoreUpdateAdapter.class,
				StoreUpdateTask.class, false, objs);
		long st = System.currentTimeMillis();
		String ret = fut.get();
		long et = System.currentTimeMillis();
		System.out.println("CloudService executeUpdate time��" + (et - st) + " " + ret);
	}

	public static void main(String[] args) throws Exception
	{
		if (!CloudGrid.isStart())
			CloudGrid.Start();
		// CloudGrid.setLinkTimeout(1000, ActionEnum.ACTION_TIMEOUT_RECALL);
		List<String[]> list = CloudService.executeQuery(
				"select * from t_energy_data order by EDATA_ID", new String[] { "EDATA_ID",
						"EDATA_VALUE" });
		System.out.println(list.size());
		// Thread t1=new Thread(){
		// public void run(){
		// while(true){
		// try{
		// List<String[]>
		// list=CloudService.executeQuery("select * from t_energy_data order by EDATA_ID",new
		// String[]{"EDATA_ID","EDATA_VALUE"});
		// System.out.println(list.size());
		// Thread.sleep(3000);
		// }catch(Exception e){e.printStackTrace();}
		//
		// }
		// }
		// };
		// Thread t2=new Thread(){
		// public void run(){
		// try{
		// List<String[]>
		// list=CloudService.executeQuery("select * from t_energy_data order by EDATA_ID limit 0,900",new
		// String[]{"EDATA_ID","EDATA_VALUE"});
		// System.out.println(list.size());
		// Thread.sleep(3000);
		// }catch(Exception e){e.printStackTrace();}
		// }
		// };
		// t1.start();
		// t2.start();
	}
}
