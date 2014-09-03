package darks.grid.kernel.network.http;

import java.net.BindException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import darks.grid.kernel.network.ICGHttpNet;
import darks.grid.kernel.store.CGDataStore;

public class CGNetHttp implements ICGHttpNet
{

	public String WEB_ROOT;

	private int port;

	private boolean isinit = false;

	private boolean isclosed = false;

	private ServerSocket serverSocket;

	private ExecutorService execservice = Executors
			.newFixedThreadPool(CGDataStore.HTTP_THREAD_MAXNUM);
	private ExecutorService execcache = Executors.newCachedThreadPool();

	public CGNetHttp()
	{
		WEB_ROOT = CGDataStore.httpRoot;
		this.port = CGDataStore.initHttpPort;
	}

	public CGNetHttp(String root)
	{
		WEB_ROOT = root;
		this.port = CGDataStore.initHttpPort;
	}

	public CGNetHttp(String root, int port)
	{
		WEB_ROOT = root;
		this.port = port;
	}

	public void addThreadCache(CGNetHttpProcessThread thd)
	{
		execcache.execute(thd);
	}

	public void init() throws Exception
	{
		int p = port;
		for (int i = p; i < p + CGDataStore.PORT_MAX_OFFSET; i++)
		{
			try
			{
				serverSocket = new ServerSocket(i);
				port = i;
				System.out.println("ÔÆ¶ËÖ´ÐÐÍøÂç¶Ë¿Ú°ó¶¨" + port);
				break;
			}
			catch (BindException e)
			{
				System.out.println("ÔÆ¶ËÖ´ÐÐÍøÂç¼àÌý¶Ë¿Ú´Ó" + i + "ÇÐ»»ÖÁ" + (i + 1));
				continue;
			}
			catch (Exception e)
			{
				System.out.println("ÔÆ¶ËÖ´ÐÐÍøÂç¶Ë¿Ú´íÎó£¡");
				e.printStackTrace();
				return;
			}
		}
		for (int i = 0; i < CGDataStore.HTTP_THREAD_MAXNUM; i++)
		{
			execservice.execute(new CGNetHttpThread(this));
		}
		isinit = true;
	}

	public void init(int port) throws Exception
	{
		this.port = port;
		init();
	}

	public ExecutorService getExecservice()
	{
		return execservice;
	}

	public void close() throws Exception
	{
		execservice.shutdown();
		serverSocket.close();
		isclosed = true;
	}

	public boolean isInited()
	{
		return isinit;
	}

	public boolean isClosed()
	{
		return isclosed;
	}

	public String getWebRoot()
	{
		return WEB_ROOT;
	}

	public void setWebRoot(String webroot)
	{
		WEB_ROOT = webroot;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getAddress()
	{
		return serverSocket.getInetAddress().getHostAddress();
	}

	public ServerSocket getServerSocket()
	{
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket)
	{
		this.serverSocket = serverSocket;
	}

	public static void main(String args[]) throws Exception
	{
		CGNetHttp server = new CGNetHttp("e:/demo", 8080);
		server.init();
	}

}
