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
		System.out.println("��������Ϣ����...");
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
			System.out.println("����Ϣ��ʼ������");
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
				System.out.println("�ƶ���Ϣ�˿ڰ�" + p);
				CGDataStore.initInfoPort = p;
				break;
			}
			catch (BindException e)
			{
				System.out.println("�ƶ���Ϣ�����˿ڴ�" + p + "�л���" + (p + 1));
			}
			catch (Exception e)
			{
				System.out.println("�ƶ���Ϣ�˿ڴ���");
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
			System.out.print("<����Ϣ����" + (i + 1) + "������>");
		}
		System.out.print("\n");

		// ExecutorService
		// infoqexec=Executors.newFixedThreadPool(CGDataStore.INFO_QUEUE_THREAD_MAXNUM);
		for (int i = 0; i < CGDataStore.INFO_QUEUE_THREAD_MAXNUM; i++)
		{
			CGInfoQueueThread infoq = new CGInfoQueueThread(this);
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.INFO_QUEUE_THREAD)
					.execute(infoq);
			System.out.print("<����Ϣ������" + (i + 1) + "������>");
		}
		System.out.print("\n");
		CGPrintCenter.printObject("���ص�ַ>" + CGDataStore.initInfoHost + ":"
				+ CGDataStore.initInfoPort);
		CGPrintCenter.printObject("���ӵ�ַ>" + CGDataStore.connectInfoHost + ":"
				+ CGDataStore.connectInfoPort);
		System.out.print("�������������ڽڵ�..");
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
			System.out.println("�ҵ������ƽڵ�,��������...");
			CGDataStore.getNetInfo().response(CGDataStore.connectInfoHost, cp,
					CGMessageFactory.createMessage(CGMessageFactory.JOIN_NODE));// "checkconn_"+CGDataStore.getUUID()+"_"+CGDataStore.getNetInfo().getAddress()+":"+CGDataStore.getNetInfo().getPort()

		}
		else
		{
			if (!CGDataStore.getNetInfo().getAddress().equals(CGDataStore.connectInfoHost))
			{
				n = 0;
				cp = -1;
				System.out.print("�������������ڽڵ�..");
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
					System.out.println("�ҵ������ƽڵ�,��������...");
					CGDataStore.getNetInfo().response(CGDataStore.initInfoHost, cp,
							CGMessageFactory.createMessage(CGMessageFactory.JOIN_NODE));// "checkconn_"+CGDataStore.getUUID()+"_"+CGDataStore.getNetInfo().getAddress()+":"+CGDataStore.getNetInfo().getPort()

				}
				else
				{
					System.out.println("δ�ҵ������ƽڵ�,�ȴ������ڵ�����...");
				}
			}

			System.out.println("δ�ҵ������ƽڵ�,�ȴ������ڵ�����...");
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
