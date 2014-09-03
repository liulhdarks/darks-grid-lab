package darks.grid.kernel.network.udp;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

import darks.grid.kernel.network.ACGNet;
import darks.grid.kernel.network.ICGEvent;
import darks.grid.kernel.network.ICGListener;
import darks.grid.util.CGUtil;

public class CGUDPNet extends ACGNet
{
	private DatagramSocket socket = null;
	private int serverPort = 9213;
	private String serverHost = "127.0.0.1";
	private SocketAddress socketAddress;
	private boolean inited = false;
	private boolean closed = true;
	private int buffer_size = 0;

	@Override
	public void bind(String host, int port) throws BindException, Exception
	{
		InetAddress ipv4 = CGUtil.getLocalHost();
		host = ipv4.getHostAddress();
		host = host.replace("/", "");
		socketAddress = new InetSocketAddress(host, port);
		socket = new DatagramSocket(socketAddress);
		serverHost = host;
		serverPort = port;
		buffer_size = socket.getSendBufferSize();
		// System.out.println(buffer_size);
	}

	@Override
	public void close()
	{
		try
		{
			closed = true;
			socket.close();

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

	@Override
	public void init() throws Exception
	{
		socket = new DatagramSocket();
		closed = false;
		inited = true;
	}

	@Override
	public void init(String host, int port) throws BindException, Exception
	{
		bind(host, port);
		closed = false;
		inited = true;
	}

	@Override
	public boolean isClosed()
	{
		return closed;
	}

	@Override
	public boolean isInited()
	{
		return inited;
	}

	@Override
	public byte[] request() throws Exception
	{
		try
		{

			CGPacket receivePacket = new CGPacket();
			DatagramPacket pack = receivePacket.getPacket();
			// System.out.println("wait....");
			socket.receive(pack);
			// System.out.println("rev...."+new String(pack.getData()));
			receivePacket.setPacket(pack);
			List<ICGListener> list = getListenersList();
			ICGEvent e = new CGUDPEvent(receivePacket, pack.getData());
			switch (list.size())
			{
			case 0:
				break;
			case 1:
				list.get(0).execute(e);
				break;
			default:
				for (int i = 0; i < list.size(); i++)
				{
					list.get(i).execute(e);
				}
				break;
			}
			return receivePacket.getData();
		}
		catch (Exception e)
		{
			throw new Exception("CGUDPNet.request:" + e.getMessage());
		}
	}

	@Override
	public byte[] request(int timeout) throws InterruptedException, SocketTimeoutException,
			SocketException, IOException
	{
		CGPacket receivePacket = new CGPacket();
		DatagramPacket pack = receivePacket.getPacket();
		socket.setSoTimeout(timeout);
		// System.out.println("wait2....");
		socket.receive(pack);
		// System.out.println("rev2...."+new String(pack.getData()));
		receivePacket.setPacket(pack);
		List<ICGListener> list = getListenersList();
		ICGEvent e = new CGUDPEvent(receivePacket, pack.getData());
		switch (list.size())
		{
		case 0:
			break;
		case 1:
			list.get(0).execute(e);
			break;
		default:
			for (int i = 0; i < list.size(); i++)
			{
				list.get(i).execute(e);
			}
			break;
		}
		return receivePacket.getData();
	}

	@Override
	public void response(String addr, String data)
	{
		String[] arg = addr.split(":");
		response(arg[0], Integer.parseInt(arg[1]), data.getBytes());
	}

	@Override
	public void response(String addr, byte[] data)
	{
		String[] arg = addr.split(":");
		response(arg[0], Integer.parseInt(arg[1]), data);
	}

	@Override
	public void response(String host, int port, byte[] data)
	{
		try
		{
			InetSocketAddress isa = new InetSocketAddress(host, port);
			CGPacket sendPacket = new CGPacket(isa);
			sendPacket.setData(data);
			socket.send(sendPacket.getPacket());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void response(String host, int port, String data)
	{
		try
		{
			// System.out.println("send...."+data+" to "+host+":"+port);
			InetSocketAddress isa = new InetSocketAddress(host, port);
			CGPacket sendPacket = new CGPacket(isa);
			sendPacket.setData(data.getBytes());
			socket.send(sendPacket.getPacket());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void response(String host, int port, Object obj)
	{
		byte[] data = CGUtil.ObjectToByte(obj);
		response(host, port, data);
	}

	@Override
	public void setAddress(String addr)
	{
		serverHost = addr;
	}

	@Override
	public void setPort(int port)
	{
		serverPort = port;
	}

	@Override
	public void setTimeout(int timeOut)
	{
		try
		{
			socket.setSoTimeout(timeOut);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public int getSendBufferSize() throws Exception
	{
		return buffer_size;
	}

}
