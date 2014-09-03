package darks.grid.kernel.service;

import java.net.SocketTimeoutException;

import darks.grid.kernel.ICGService;
import darks.grid.kernel.network.ICGNetFactory;
import darks.grid.kernel.network.factory.CGMessageFactory;
import darks.grid.kernel.network.factory.CGNetFactory;
import darks.grid.kernel.service.thread.CGCheckThread;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.store.CGThreadStore;

public class CGCheckService implements ICGService
{
	private boolean started = false;

	@Override
	public void Close() throws Exception
	{
		if (CGDataStore.getNetCheck() == null)
			throw new Exception("CGDataStore.getNetCheck() null");
		CGDataStore.getNetCheck().close();
		started = false;
	}

	@Override
	public void Start(int type) throws Exception
	{
		System.out.println("Æô¶¯ÔÆÑ²¼ì·þÎñ...");
		ICGNetFactory fty = CGNetFactory.getInstance();
		CGDataStore.setNetCheck(fty.createCGNet(type));
		if (CGDataStore.isCheckNull())
		{
			throw new Exception("CGDataStore.isCheckNull() is null");
		}
		initService();
		runProcess();
		started = true;
	}

	public static boolean checkJoinConnection(String IP, int port)
	{
		return checkJoinConnection(IP, port, 500);
	}

	public static boolean checkJoinConnection(String IP, int port, int timeout)
	{
		if (CGDataStore.isCheckNull())
			return false;
		try
		{
			CGDataStore.getNetCheck().response(IP, port,
					CGMessageFactory.createMessage(CGMessageFactory.CONN_CHK));
			byte[] data = CGDataStore.getNetCheck().request(timeout);
			String info = new String(data);
			String[] arg = info.split("_");
			if (arg.length < 2)
				return false;
			int mtype = Integer.parseInt(arg[0]);
			if (mtype != CGMessageFactory.CONN_CHK_BACK)
			{
				throw new SocketTimeoutException();
			}
			return true;
		}
		catch (SocketTimeoutException et)
		{
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private void initService()
	{
		try
		{
			CGDataStore.getNetCheck().init();
		}
		catch (Exception e)
		{
			System.out.println("ÔÆÑ²¼ì³õÊ¼»¯´íÎó£¡");
			e.printStackTrace();
		}
	}

	private void runProcess()
	{
		for (int i = 0; i < CGDataStore.CHK_THREAD_MAXNUM; i++)
		{
			CGCheckThread chk = new CGCheckThread(this);
			CGThreadStore.getInstance().getExecutorService(CGThreadStore.CHK_THREAD).execute(chk);
			System.out.print("<ÔÆÑ²¼ì·þÎñ" + (i + 1) + "ÏßÆô¶¯>");
		}
		System.out.print("\n");

	}

	@Override
	public boolean isClose()
	{
		return CGDataStore.getNetCheck().isClosed();
	}

	@Override
	public boolean isStart()
	{
		return started;
	}

}
