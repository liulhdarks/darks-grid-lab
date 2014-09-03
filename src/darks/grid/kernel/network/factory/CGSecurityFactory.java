package darks.grid.kernel.network.factory;

import darks.grid.kernel.network.ICGSecurityFactory;

public class CGSecurityFactory implements ICGSecurityFactory
{

	private static CGSecurityFactory instance = null;

	private CGSecurityFactory()
	{
	}

	public byte[] dencryptNetData(byte[] data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] dencryptNetData(String data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] encryptNetData(byte[] data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] encryptNetData(String data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String procRevData(String data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] procRevData(byte[] data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String procSendData(String data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] procSendData(byte[] data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public static CGSecurityFactory getInstance()
	{
		if (instance == null)
		{
			instance = new CGSecurityFactory();
		}
		return instance;
	}
}
