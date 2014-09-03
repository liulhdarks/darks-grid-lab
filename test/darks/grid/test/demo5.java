package darks.grid.test;

import java.util.List;

import darks.grid.CloudGrid;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGFutureTask;
import darks.grid.kernel.factory.CGNodeFactory;
import darks.grid.test.task.task1;

public class demo5
{

	public static void main(String[] args)
	{
		try
		{

			CloudGrid.Start();

			while (true)
			{

				List<ICGNode> list = CGNodeFactory.getInstance().getNodes();
				// Map<String,ICGNode> map=new HashMap<String,ICGNode>();
				// for(ICGNode node:list){
				// if(map.containsKey(node.getAddress()))continue;
				// map.put(node.getAddress(), node);
				// }
				//
				// list.clear();
				// list.addAll(map.values());

				// CGFutureTask<String>
				// fut=CloudGrid.execute(task1.class,"demo6",
				// "http://172.22.114.191:8686/bin/org/cloudgrid/test/demo6.class",
				// true, new String[]{"1","5"},new String[]{"6","10"},new
				// String[]{"11","15"});
				Object[] objs = new Object[] { new String[] { "1", "20" },
						new String[] { "21", "30" }, new String[] { "31", "40" } };
				CGFutureTask<String> fut = CloudGrid.executeArray(list, task1.class, demo6.class,
						true, objs);
				// CGFutureTask<String>
				// fut=CloudGrid.execute(task1.class,"demo6",
				// "darks.grid.test.demo6", true, "demo","kkk");
				long st = System.currentTimeMillis();
				String ret = fut.get();
				long et = System.currentTimeMillis();
				System.out.println("finnal:" + ret + " time��" + (et - st));
				Thread.sleep(1000);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
