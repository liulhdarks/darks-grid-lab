package darks.grid.kernel.network.http;

import java.net.Socket;

public class CGNetHttpThread extends Thread
{

	private CGNetHttp httpnet;

	public CGNetHttpThread(CGNetHttp httpnet)
	{
		this.httpnet = httpnet;
	}

	public void run()
	{
		if (httpnet == null)
		{
			System.out.println("CGNetHttpThread>httpnet is null");
			return;
		}
		if (httpnet.getServerSocket() == null)
		{
			System.out.println("CGNetHttpThread>httpnet.getServerSocket is null");
			return;
		}
		while (true)
		{

			try
			{
				Socket socket = httpnet.getServerSocket().accept();
				httpnet.addThreadCache(new CGNetHttpProcessThread(httpnet, socket));
				sleep(10);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
