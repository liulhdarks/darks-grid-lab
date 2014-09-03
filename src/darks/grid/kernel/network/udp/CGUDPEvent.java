package darks.grid.kernel.network.udp;

import darks.grid.kernel.network.ICGEvent;

public class CGUDPEvent implements ICGEvent
{

	private byte[] data = null;
	private CGPacket info = null;

	public CGUDPEvent()
	{
	}

	public CGUDPEvent(CGPacket packet)
	{
		setEventInfo(packet);
	}

	public CGUDPEvent(CGPacket packet, byte[] data)
	{
		setEventInfo(packet);
		setData(data);
	}

	public byte[] getData()
	{
		return data;
	}

	public CGPacket getEventInfo()
	{
		return info;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public void setEventInfo(Object obj)
	{
		info = (CGPacket) obj;
	}

	public String getAddress()
	{
		return new String(info.getAddress().getHostAddress());
	}

	public int getPort()
	{
		return info.getPort();
	}
}
