package darks.grid.kernel.network;

public interface ICGEvent
{
	public Object getEventInfo();

	public void setEventInfo(Object obj);

	public void setData(byte[] data);

	public byte[] getData();

	public int getPort();

	public String getAddress();
}
