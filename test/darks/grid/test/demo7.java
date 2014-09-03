package darks.grid.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import darks.grid.CloudGrid;
import darks.grid.ICGLinkTask;
import darks.grid.kernel.Impl.CGFutureTask;
import darks.grid.test.task.task3;

public class demo7 implements ICGLinkTask
{
	Connection con;
	Statement stmt;

	public Object execute(Object objs)
	{
		try
		{
			String[] s = (String[]) objs;
			for (String st : s)
			{
				System.out.println(st);
			}
			initDB(s[0], s[1], s[2]);
			ResultSet rs = stmt.executeQuery(s[3]);
			float sum = 0;
			while (rs.next())
			{
				float f = rs.getFloat("EDATA_VALUE");
				sum += f;
			}
			rs.close();
			stmt.close();
			con.close();
			return sum;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	private void initDB(String cstr, String uname, String upwd) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(cstr, uname, upwd);
		stmt = con.createStatement();
	}

	public static void main(String[] arg) throws Exception
	{
		long st = System.currentTimeMillis();
		demo7 d = new demo7();
		String[] s0 = new String[] {
				"jdbc:mysql://127.0.0.1:3306/energy?user=root&password=123&useUnicode=true&characterEncoding=utf-8",
				"root", "123", "select EDATA_VALUE from t_energy_data order by EDATA_ID" };
		String[] s = new String[] {
				"jdbc:mysql://127.0.0.1:3306/energy?user=root&password=123&useUnicode=true&characterEncoding=utf-8",
				"root", "123",
				"select EDATA_VALUE from t_energy_data where EDATA_ID>0 and EDATA_ID<2000 order by EDATA_ID" };
		String[] s1 = new String[] {
				"jdbc:mysql://127.0.0.1:3306/energy?user=root&password=123&useUnicode=true&characterEncoding=utf-8",
				"root", "123",
				"select EDATA_VALUE from t_energy_data where EDATA_ID>=2000 and EDATA_ID<4000 order by EDATA_ID" };
		String[] s2 = new String[] {
				"jdbc:mysql://127.0.0.1:3306/energy?user=root&password=123&useUnicode=true&characterEncoding=utf-8",
				"root", "123",
				"select EDATA_VALUE from t_energy_data order by EDATA_ID limit 0,900" };
		// Float ret=(Float)d.execute(s0);
		// long et=System.currentTimeMillis();
		// System.out.println("finnal:"+ret+" time��"+(et-st));
		CloudGrid.Start();
		while (true)
		{
			CGFutureTask<String> fut = CloudGrid.execute(task3.class, "darks.grid.test.demo7",
					true, s1, s2);
			st = System.currentTimeMillis();
			String sret = fut.get();
			long et = System.currentTimeMillis();
			System.out.println("sfinnal:" + sret + " time��" + (et - st));
			Thread.sleep(3000);
		}
	}

}
