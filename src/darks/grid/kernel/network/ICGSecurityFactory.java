package darks.grid.kernel.network;

public interface ICGSecurityFactory
{
	public byte[] encryptNetData(byte[] data);

	public byte[] encryptNetData(String data);

	public byte[] dencryptNetData(byte[] data);

	public byte[] dencryptNetData(String data);

	public String procSendData(String data);

	public byte[] procSendData(byte[] data);

	public String procRevData(String data);

	public byte[] procRevData(byte[] data);
}
