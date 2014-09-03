package darks.grid.kernel.service;

import java.net.BindException;

import darks.grid.kernel.ICGService;
import darks.grid.kernel.network.ICGNetFactory;
import darks.grid.kernel.network.factory.CGMessageFactory;
import darks.grid.kernel.network.factory.CGNetFactory;
import darks.grid.kernel.service.listener.CGInfoListener;
import darks.grid.kernel.service.thread.CGInfoQueueThread;
import darks.grid.kernel.service.thread.CGInfoThread;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.store.CGPrintCenter;
import darks.grid.kernel.store.CGThreadStore;

public class CGInfoService implements ICGService
{
	private boolean started = false;

	@Override
	public void Close() throws Exception
	{
		if (CGDataStore.getNetInfo() == null)
			throw new Exception("CGDataStore.getNetInfo() null");
		CGDataStore.getNetInfo().close();
		started = false;
	}

	@Override
	public void Start(int type) throws Exception
	{
		System.out.println("启动云信息服务...");
		ICGNetFactory fty = CGNetFactory.getInstance();
		CGDataStore.setNetInfo(fty.createCGNet(type));
		if (CGDataStore.isInfoNull())
		{
			throw new Exception("CGDataStore.isInfoNull() is null");
		}
		initService();
		runProcess();
		started = true;
	}

	private void initService()
	{
		try
		{
			bind(CGDataStore.initInfoHost, CGDataStore.initInfoPort);
		}
		catch (Exception e)
		{
			System.out.println("云信息初始化错误！");
			e.printStackTrace();
		}
	}

	private void bind(String addr, int port)
	{
		for (int p = port; p < port + CGDataStore.PORT_MAX_OFFSET; p++)
		{
			try
			{
				CGDataStore.getNetInfo().init(addr, p);
				System.out.println("云端消息端口绑定" + p);
				CGDataStore.initInfoPort = p;
				break;
			}
			catch (BindException e)
			{
				System.out.println("云端消息监听端口从" + p + "切换至" + (p + 1));
			}
			catch (Exception e)
			{
				System.out.println("云端消息端口错误！");
				e.printStackTrace();
			}
		}
	}

	private void runProcess()
	{

		CGDataStore.initInfoHost = CGDataStore.getNetInfo().getAddress();
		CGDataStore.getNetInfo().addRecevieListener(new CGInfoListener(this));
		// ExecutorService
		// infoexec=Executors.newFixedThreadPool(CGDataStore.INFO_THREAD_MAXNUM);
		for (int i = 0; i < CGDataStore.INFO_THREAD_MAXNUM; i++)
		{
			CGInfoThread info = new CGInfoThread(this);
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.INFO_THREAD).execute(info);
			System.out.print("<云信息服务" + (i + 1) + "线启动>");
		}
		System.out.print("\n");

		// ExecutorService
		// infoqexec=Executors.newFixedThreadPool(CGDataStore.INFO_QUEUE_THREAD_MAXNUM);
		for (int i = 0; i < CGDataStore.INFO_QUEUE_THREAD_MAXNUM; i++)
		{
			CGInfoQueueThread infoq = new CGInfoQueueThread(this);
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.INFO_QUEUE_THREAD)
					.execute(infoq);
			System.out.print("<云信息服务辅助" + (i + 1) + "线启动>");
		}
		System.out.print("\n");
		CGPrintCenter.printObject("本地地址>" + CGDataStore.initInfoHost + ":"
				+ CGDataStore.initInfoPort);
		CGPrintCenter.printObject("链接地址>" + CGDataStore.connectInfoHost + ":"
				+ CGDataStore.connectInfoPort);
		System.out.print("搜索区域云相邻节点..");
		int n = 0;
		int cp = -1;
		for (int p = CGDataStore.connectInfoPort; p < CGDataStore.connectInfoPort
				+ CGDataStore.PORT_MAX_OFFSET; p++)
		{
			if (CGDataStore.getNetInfo().getAddress().equals(CGDataStore.connectInfoHost)
					&& CGDataStore.getNetInfo().getPort() == p)
				continue;
			System.out.print(".");
			if (CGCheckService.checkJoinConnection(CGDataStore.connectInfoHost, p))
			{
				cp = p;
				break;
			}
			n++;
			if (n > 4)
				break;
		}
		System.out.print("\n");
		if (cp > 0)
		{
			System.out.println("找到区域云节点,请求连接...");
			CGDataStore.getNetInfo().response(CGDataStore.connectInfoHost, cp,
					CGMessageFactory.createMessage(CGMessageFactory.JOIN_NODE));// "checkconn_"+CGDataStore.getUUID()+"_"+CGDataStore.getNetInfo().getAddress()+":"+CGDataStore.getNetInfo().getPort()

		}
		else
		{
			if (!CGDataStore.getNetInfo().getAddress().equals(CGDataStore.connectInfoHost))
			{
				n = 0;
				cp = -1;
				System.out.print("检索本地云相邻节点..");
				for (int p = CGDataStore.connectInfoPort; p < CGDataStore.connectInfoPort
						+ CGDataStore.PORT_MAX_OFFSET; p++)
				{
					if (CGDataStore.getNetInfo().getPort() == p)
						continue;
					System.out.print(".");
					if (CGCheckService.checkJoinConnection(CGDataStore.initInfoHost, p))
					{
						cp = p;
						break;
					}
					n++;
					if (n > 4)
						break;
				}
				System.out.print("\n");
				if (cp > 0)
				{
					System.out.println("找到本地云节点,请求连接...");
					CGDataStore.getNetInfo().response(CGDataStore.initInfoHost, cp,
							CGMessageFactory.createMessage(CGMessageFactory.JOIN_NODE));// "checkconn_"+CGDataStore.getUUID()+"_"+CGDataStore.getNetInfo().getAddress()+":"+CGDataStore.getNetInfo().getPort()

				}
				else
				{
					System.out.println("未找到本地云节点,等待其他节点上线...");
				}
			}

			System.out.println("未找到区域云节点,等待其他节点上线...");
		}
	}

	@Override
	public boolean isClose()
	{
		return CGDataStore.getNetInfo().isClosed();
	}

	@Override
	public boolean isStart()
	{
		return started;
	}

}
