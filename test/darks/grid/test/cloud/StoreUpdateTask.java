package darks.grid.test.cloud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import darks.grid.ICGLinkTask;

public class StoreUpdateTask implements ICGLinkTask
{

	private Connection con;

	private Statement stmt;

	private static final long serialVersionUID = 3569809776881183461L;

	public Object execute(Object objs) throws Exception
	{
		Object[] st = (Object[]) objs;
		String ret = "";
		if (st.length < 2)
			return "fail";
		try
		{
			initDB();
			if (con == null)
				return "connection is null";
			if (stmt == null)
				return "Statement is null";
			if ("update".equals(st[1]))
			{
				System.out.println("ִ��UPDATE����" + st[0]);
				stmt.executeUpdate((String) st[0]);
			}
			closeDB();
			return "success";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	private void initDB() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager
				.getConnection(
						"jdbc:mysql://localhost:3306/energy?user=root&password=123&useUnicode=true&characterEncoding=utf-8",
						"root", "123");
		stmt = con.createStatement();
	}

	private void closeDB() throws Exception
	{
		if (stmt != null)
			stmt.close();
		if (con != null)
			con.close();
	}

}
