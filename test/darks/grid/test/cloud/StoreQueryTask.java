package darks.grid.test.cloud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import darks.grid.ICGLinkTask;

public class StoreQueryTask implements ICGLinkTask
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
			if ("query".equals(st[1]))
			{
				if (st.length > 2)
				{
					List<String[]> list = new ArrayList<String[]>();
					String[] strs = (String[]) st[2];
					System.out.println("ִ��QUERY����" + st[0]);
					ResultSet rs = stmt.executeQuery((String) st[0]);
					while (rs.next())
					{
						String[] tmp = new String[strs.length];
						int i = 0;
						for (String s : strs)
						{
							String t = rs.getString(s);
							if (t == null)
								t = "";
							tmp[i++] = t;
						}
						list.add(tmp);
					}
					rs.close();
					closeDB();
					return list;
				}
			}
			closeDB();
			return "queryfail";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return e.toString();
		}
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
