package darks.grid.kernel.service;

import java.net.BindException;

import darks.grid.kernel.ICGService;
import darks.grid.kernel.network.ICGNetFactory;
import darks.grid.kernel.network.factory.CGNetFactory;
import darks.grid.kernel.service.listener.CGExecutorResultListener;
import darks.grid.kernel.service.thread.CGExecutorResultThread;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.store.CGThreadStore;

public class CGExecutorResultService implements ICGService
{
	private boolean started = false;

	@Override
	public void Close() throws Exception
	{
		if (CGDataStore.getNetResult() == null)
			throw new Exception("CGDataStore.getNetResult() null");
		CGDataStore.getNetResult().close();
		started = false;
	}

	@Override
	public void Start(int type) throws Exception
	{
		System.out.println("启动云执行反馈服务...");
		ICGNetFactory fty = CGNetFactory.getInstance();
		CGDataStore.setNetResult(fty.createCGNet(type));
		if (CGDataStore.isObjRltNull())
		{
			throw new Exception("CGDataStore.isObjRltNull() is null");
		}
		initService();
		runProcess();
		started = true;
	}

	private void initService()
	{
		try
		{
			bind(CGDataStore.initInfoHost, CGDataStore.initObjRltPort);
		}
		catch (Exception e)
		{
			System.out.println("云执行反馈初始化错误！");
			e.printStackTrace();
		}
	}

	private void bind(String addr, int port)
	{
		for (int p = port; p < port + CGDataStore.PORT_MAX_OFFSET; p++)
		{
			try
			{
				CGDataStore.getNetResult().init(addr, p);
				System.out.println("云端执行反馈端口绑定" + p);
				CGDataStore.initObjRltPort = p;
				break;
			}
			catch (BindException e)
			{
				System.out.println("云端执行反馈监听端口从" + p + "切换至" + (p + 1));
			}
			catch (Exception e)
			{
				System.out.println("云端执行反馈端口错误！");
				e.printStackTrace();
			}
		}
	}

	private void runProcess()
	{

		CGDataStore.getNetResult().addRecevieListener(new CGExecutorResultListener(this));
		// ExecutorService
		// infoexec=Executors.newFixedThreadPool(CGDataStore.EXEC_RET_THREAD_MAXNUM);
		for (int i = 0; i < CGDataStore.EXEC_RET_THREAD_MAXNUM; i++)
		{
			CGExecutorResultThread exec = new CGExecutorResultThread(this);
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.EXEC_RET_THREAD)
					.execute(exec);
			System.out.print("<云执行反馈辅助服务" + (i + 1) + "线启动>");
		}
		System.out.print("\n");

	}

	@Override
	public boolean isClose()
	{
		return CGDataStore.getNetResult().isClosed();
	}

	@Override
	public boolean isStart()
	{
		return started;
	}

}
