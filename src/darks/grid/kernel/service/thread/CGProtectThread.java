package darks.grid.kernel.service.thread;

import darks.grid.kernel.network.factory.CGMessageFactory;
import darks.grid.kernel.service.CGCheckService;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;

public class CGProtectThread extends Thread
{

	public void run()
	{
		while (!CGDataStore.getNetInfo().isClosed())
		{
			try
			{
				int cp = -1;
				int n = 0;
				for (int p = CGDataStore.connectInfoPort; p < CGDataStore.connectInfoPort
						+ CGDataStore.PORT_MAX_OFFSET; p++)
				{
					if (CGDataStore.initInfoHost.equals(CGDataStore.connectInfoHost)
							&& CGDataStore.initInfoPort == p)
						continue;
					if (CGDataCenter.getInstance().containCGNode(CGDataStore.connectInfoHost, p))
						continue;
					if (CGCheckService.checkJoinConnection(CGDataStore.connectInfoHost, p, 200))
					{
						cp = p;
						break;
					}
					n++;
					if (n > 2)
						break;
				}
				if (cp > 0)
				{
					CGDataStore.getNetInfo().response(CGDataStore.connectInfoHost, cp,
							CGMessageFactory.createMessage(CGMessageFactory.JOIN_NODE));
				}
				sleep(3000);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
}
