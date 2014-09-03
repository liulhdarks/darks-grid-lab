package darks.grid.kernel.service;

import java.net.BindException;

import darks.grid.kernel.ICGService;
import darks.grid.kernel.network.ICGNetFactory;
import darks.grid.kernel.network.factory.CGNetFactory;
import darks.grid.kernel.service.listener.CGExecutorListener;
import darks.grid.kernel.service.thread.CGExecutorQueueProcessThread;
import darks.grid.kernel.service.thread.CGExecutorQueueThread;
import darks.grid.kernel.service.thread.CGExecutorThread;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.store.CGThreadStore;

public class CGExecutorService implements ICGService
{

	private boolean started = false;

	@Override
	public void Close() throws Exception
	{
		if (CGDataStore.getNetObj() == null)
			throw new Exception("CGDataStore.getNetObj() null");
		CGDataStore.getNetObj().close();
		if (CGDataStore.isHttpNull())
			throw new Exception("CGDataStore.isHttpNull() null");
		CGDataStore.getNetHttp().close();
		started = false;
	}

	@Override
	public void Start(int type) throws Exception
	{
		System.out.println("启动云执行服务...");
		ICGNetFactory fty = CGNetFactory.getInstance();
		CGDataStore.setNetObj(fty.createCGNet(type));
		if (CGDataStore.isObjNull())
		{
			throw new Exception("CGDataStore.isObjNull() is null in Start");
		}

		// http-------------
		CGDataStore.setNetHttp(fty.createCGHttpNet());
		if (CGDataStore.isHttpNull())
		{
			throw new Exception("CGDataStore.isHttpNull() is null in Start");
		}
		// -----------------
		initService();
		runProcess();
		started = true;
	}

	private void initService()
	{
		try
		{
			bind(CGDataStore.initInfoHost, CGDataStore.initObjPort);
		}
		catch (Exception e)
		{
			System.out.println("云执行初始化错误！");
			e.printStackTrace();
		}
		try
		{
			CGDataStore.getNetHttp().init();
			CGDataStore.initHttpPort = CGDataStore.getNetHttp().getPort();
		}
		catch (Exception e)
		{
			System.out.println("云执行HTTP初始化错误！");
			e.printStackTrace();
		}
	}

	private void bind(String addr, int port)
	{
		for (int p = port; p < port + CGDataStore.PORT_MAX_OFFSET; p++)
		{
			try
			{
				CGDataStore.getNetObj().init(addr, p);
				System.out.println("云端执行端口绑定" + p);
				CGDataStore.initObjPort = p;
				break;
			}
			catch (BindException e)
			{
				System.out.println("云端执行监听端口从" + p + "切换至" + (p + 1));
			}
			catch (Exception e)
			{
				System.out.println("云端执行端口错误！");
				e.printStackTrace();
			}
		}
	}

	private void runProcess()
	{

		CGDataStore.getNetObj().addRecevieListener(new CGExecutorListener(this));
		// ExecutorService
		// infoexec=Executors.newFixedThreadPool(CGDataStore.EXEC_THREAD_MAXNUM);
		for (int i = 0; i < CGDataStore.EXEC_THREAD_MAXNUM; i++)
		{
			CGExecutorThread exec = new CGExecutorThread(this);
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.EXEC_THREAD).execute(exec);
			System.out.print("<云执行服务" + (i + 1) + "线启动>");
		}
		System.out.print("\n");

		// ExecutorService
		// infoexecq=Executors.newFixedThreadPool(CGDataStore.EXEC_QUEUE_THREAD_MAXNUM);
		int ie = 0;
		for (int i = 0; i < CGDataStore.EXEC_QUEUE_THREAD_MAXNUM; i++)
		{
			CGExecutorQueueThread exec = new CGExecutorQueueThread(this, ie++);
			if (ie >= CGDataStore.EXEC_BUFFER_MAXNUM)
				ie = 0;
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.EXEC_QUEUE_THREAD)
					.execute(exec);
			System.out.print("<云执行辅助服务" + (i + 1) + "线启动>");
		}
		System.out.print("\n");

		ie = 0;
		// ExecutorService
		// execqp=Executors.newFixedThreadPool(CGDataStore.EXEC_QUEUE_PROC_THREAD_MAXNUM);
		for (int i = 0; i < CGDataStore.EXEC_QUEUE_PROC_THREAD_MAXNUM; i++)
		{
			CGExecutorQueueProcessThread exec = new CGExecutorQueueProcessThread(this, ie++);
			if (ie >= CGDataStore.EXEC_PROC_BUFFER_MAXNUM)
				ie = 0;
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.EXEC_QUEUE_PROC_THREAD)
					.execute(exec);
			System.out.print("<云执行处理服务" + (i + 1) + "线启动>");
		}
		System.out.print("\n");
	}

	@Override
	public boolean isClose()
	{
		return CGDataStore.getNetObj().isClosed();
	}

	@Override
	public boolean isStart()
	{
		return started;
	}

}
