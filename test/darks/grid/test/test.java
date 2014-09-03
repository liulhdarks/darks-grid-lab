package darks.grid.test;

import java.io.Serializable;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.job.CGJobAdapter;
import darks.grid.kernel.network.ICGEvent;
import darks.grid.kernel.network.ICGListener;
import darks.grid.kernel.network.udp.CGUDPNet;
import darks.grid.util.CGUtil;

class jobtest extends CGJobAdapter<Long>
{
	public static final long serialVersionUID = 1L;

	public Serializable execute()
	{

		long ret = 0;
		System.out.println("test:" + this.getJobId());
		for (long i = 1; i < 5; i++)
		{
			System.out.println("show|" + i + "|");
			ret += i;
		}
		return ret;
	}
}

class jobtest2 extends CGJobAdapter<Long>
{
	public static final long serialVersionUID = 1L;

	public Serializable execute()
	{

		long ret = 0;
		System.out.println("test:" + this.getJobId());
		for (long i = 7; i < 10; i++)
		{
			System.out.println("show|" + i + "|");
			ret += i;
		}
		return ret;
	}
}

interface itest extends Serializable
{
	public void work();
}

class demotest implements itest
{
	public static final long serialVersionUID = 1L;

	public void work()
	{
		System.out.println("demo..");
	}
}

class demotest2 implements itest
{
	public static final long serialVersionUID = 1L;

	public void work()
	{
		System.out.println("demo2..");
	}
}

class client extends Thread implements Serializable
{
	CGUDPNet net;

	public client()
	{
		net = new CGUDPNet();
		try
		{
			net.init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		ICGJob job = new jobtest();
		byte[] data = CGUtil.ObjectToByte(job);
		net.response("127.0.0.1", 8900, data);
		// sleep(500);
		ICGJob job2 = new jobtest2();
		byte[] data2 = CGUtil.ObjectToByte(job2);
		net.response("127.0.0.1", 8900, data2);
		net.close();
	}

}

class server extends Thread implements ICGListener
{

	CGUDPNet net;

	public server()
	{
		net = new CGUDPNet();
		try
		{
			net.init("127.0.0.1", 8900);
			net.addRecevieListener(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void execute(ICGEvent e)
	{
		byte[] data = e.getData();
		ICGJob job = (ICGJob) CGUtil.ByteToObject(data);
		job.execute();

	}

	public void run()
	{
		while (true)
		{
			try
			{
				net.request();
				sleep(100);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}

public class test
{

	public static void main(String[] args) throws Exception
	{

		long s = System.currentTimeMillis();
		server a = new server();
		client c = new client();
		a.start();
		Thread.sleep(1000);
		c.start();
		long e = System.currentTimeMillis();

		System.out.println(e - s);
	}

}
