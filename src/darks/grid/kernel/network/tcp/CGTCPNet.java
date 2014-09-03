package darks.grid.kernel.network.tcp;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import darks.grid.kernel.network.ACGNet;
import darks.grid.util.CGUtil;

public class CGTCPNet extends ACGNet
{
	private ServerSocket server = null;
	private Socket client = null;

	private int serverPort = 9213;
	private String serverHost = "127.0.0.1";
	private boolean inited = false;
	private boolean closed = true;

	public void bind(String host, int port) throws BindException, Exception
	{
		server = new ServerSocket(port);
	}

	public void close()
	{
		try
		{
			closed = true;
			if (inited)
			{
				if (server != null)
					server.close();
				if (client != null)
					client.close();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	@Override
	public String getAddress()
	{
		return serverHost;
	}

	@Override
	public int getPort()
	{
		return serverPort;
	}

	public void init() throws Exception
	{
		client = new Socket();
	}

	public void init(String host, int port) throws BindException, Exception
	{
		// TODO Auto-generated method stub

	}

	public boolean isClosed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isInited()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public byte[] request() throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] request(int timeout) throws InterruptedException, SocketTimeoutException,
			SocketException, IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void response(String addr, String data)
	{
		// TODO Auto-generated method stub

	}

	public void response(String addr, byte[] data)
	{
		// TODO Auto-generated method stub

	}

	public void response(String host, int port, byte[] data)
	{
		// TODO Auto-generated method stub

	}

	public void response(String host, int port, String data)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void response(String host, int port, Object obj)
	{
		byte[] data = CGUtil.ObjectToByte(obj);
		response(host, port, data);
	}

	public void setAddress(String addr)
	{
		// TODO Auto-generated method stub

	}

	public void setPort(int port)
	{
		// TODO Auto-generated method stub

	}

	public void setTimeout(int timeOut)
	{
		// TODO Auto-generated method stub

	}

	public int getSendBufferSize() throws Exception
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
