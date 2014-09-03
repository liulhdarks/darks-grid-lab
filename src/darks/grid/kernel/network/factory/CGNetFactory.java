package darks.grid.kernel.network.factory;

import darks.grid.kernel.network.ACGNet;
import darks.grid.kernel.network.ICGHttpNet;
import darks.grid.kernel.network.ICGNetFactory;
import darks.grid.kernel.network.http.CGNetHttp;
import darks.grid.kernel.network.udp.CGUDPNet;

public class CGNetFactory implements ICGNetFactory
{

	private static CGNetFactory factory = null;

	private CGNetFactory()
	{
	}

	@Override
	public ACGNet createCGNet(int type)
	{
		ACGNet net = null;
		switch (type)
		{
		case 1:
			net = new CGUDPNet();
			break;

		}
		return net;
	}

	@Override
	public ICGHttpNet createCGHttpNet()
	{
		ICGHttpNet net = null;
		net = new CGNetHttp();
		return net;
	}

	public static synchronized CGNetFactory getInstance()
	{
		if (factory == null)
		{
			factory = new CGNetFactory();
		}
		return factory;
	}
}
