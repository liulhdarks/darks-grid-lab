package darks.grid.kernel.network.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public class CGPacket
{
	private DatagramPacket packet = null;
	private byte[] buffer = new byte[8192];

	public CGPacket()
	{
		packet = new DatagramPacket(buffer, buffer.length);
	}

	public CGPacket(DatagramPacket packet)
	{
		packet.setData(buffer);
		this.packet = packet;
	}

	public CGPacket(SocketAddress address) throws SocketException
	{
		packet = new DatagramPacket(buffer, buffer.length, address);
	}

	public CGPacket(InetAddress address, int port)
	{
		packet = new DatagramPacket(buffer, buffer.length, address, port);
	}

	public CGPacket(String host, int port) throws SocketException
	{
		InetSocketAddress isa = new InetSocketAddress(host, port);
		packet = new DatagramPacket(buffer, buffer.length, isa);
	}

	public CGPacket(String host, int port, byte[] data) throws SocketException
	{
		InetSocketAddress isa = new InetSocketAddress(host, port);
		packet = new DatagramPacket(buffer, buffer.length, isa);
		packet.setData(data);
	}

	public CGPacket(String addr) throws SocketException
	{
		String[] data = addr.split(":");
		InetSocketAddress isa = new InetSocketAddress(data[0], Integer.parseInt(data[1]));
		packet = new DatagramPacket(buffer, buffer.length, isa);
	}

	public InetAddress getAddress()
	{
		return packet.getAddress();
	}

	public int getPort()
	{
		return packet.getPort();
	}

	public byte[] getData()
	{
		return packet.getData();
	}

	public int getOffset()
	{
		return packet.getOffset();
	}

	public int getLength()
	{
		return packet.getLength();
	}

	public synchronized void setData(byte[] buf, int offset, int length)
	{
		packet.setData(buf, offset, length);
	}

	public synchronized void setAddress(InetAddress iaddr)
	{
		packet.setAddress(iaddr);
	}

	public synchronized void setPort(int iport)
	{
		packet.setPort(iport);
	}

	public synchronized void setSocketAddress(SocketAddress address)
	{
		packet.setSocketAddress(address);
	}

	public SocketAddress getSocketAddress()
	{
		return new InetSocketAddress(getAddress(), getPort());
	}

	public synchronized void setData(byte[] buf)
	{
		packet.setData(buf);
	}

	public synchronized void setLength(int length)
	{
		packet.setLength(length);
	}

	public DatagramPacket getPacket()
	{
		return packet;
	}

	public void setPacket(DatagramPacket packet)
	{
		this.packet = packet;
	}
}
