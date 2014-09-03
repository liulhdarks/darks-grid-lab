package darks.grid.kernel.store;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import darks.grid.util.CGUtil;

public class CGThreadStore
{

	public static final int EXEC_THREAD = 1001;
	public static final int EXEC_QUEUE_THREAD = 1002;
	public static final int EXEC_QUEUE_PROC_THREAD = 1003;
	public static final int CHK_THREAD = 1004;
	public static final int EXEC_RET_THREAD = 1005;
	public static final int INFO_THREAD = 1006;
	public static final int INFO_QUEUE_THREAD = 1007;
	public static final int TASK_THREAD = 1008;
	/*
	 * public static LinkedList<CGCheckThread> chkList=new
	 * LinkedList<CGCheckThread>(); public static LinkedList<CGInfoThread>
	 * infoList=new LinkedList<CGInfoThread>(); public static
	 * LinkedList<CGInfoQueueThread> infoqList=new
	 * LinkedList<CGInfoQueueThread>(); public static
	 * LinkedList<CGExecutorThread> execList=new LinkedList<CGExecutorThread>();
	 * public static LinkedList<CGExecutorQueueThread> execqList=new
	 * LinkedList<CGExecutorQueueThread>(); public static
	 * LinkedList<CGExecutorQueueProcessThread> execqpList=new
	 * LinkedList<CGExecutorQueueProcessThread>(); public static
	 * LinkedList<CGExecutorResultThread> rltList=new
	 * LinkedList<CGExecutorResultThread>();
	 */

	private static Object mutex = new Object();

	private static CGThreadStore instance = null;

	private ExecutorService chk = null;

	private ExecutorService exec = null;

	private ExecutorService execq = null;

	private ExecutorService execqp = null;

	private ExecutorService execret = null;

	private ExecutorService info = null;

	private ExecutorService infoq = null;

	private ExecutorService taskexec = null;

	private ExecutorService[] threadpool = null;

	private CGThreadStore()
	{
		chk = Executors.newFixedThreadPool(CGDataStore.CHK_THREAD_MAXNUM);
		exec = Executors.newFixedThreadPool(CGDataStore.EXEC_THREAD_MAXNUM);
		execq = Executors.newFixedThreadPool(CGDataStore.EXEC_QUEUE_THREAD_MAXNUM);
		execqp = Executors.newFixedThreadPool(CGDataStore.EXEC_QUEUE_PROC_THREAD_MAXNUM);
		execret = Executors.newFixedThreadPool(CGDataStore.EXEC_RET_THREAD_MAXNUM);
		info = Executors.newFixedThreadPool(CGDataStore.INFO_THREAD_MAXNUM);
		infoq = Executors.newFixedThreadPool(CGDataStore.INFO_QUEUE_THREAD_MAXNUM);
		threadpool = new ExecutorService[CGDataStore.EXEC_BUFFER_MAXNUM];
		taskexec = Executors.newCachedThreadPool();
		for (int i = 0; i < CGDataStore.EXEC_BUFFER_MAXNUM; i++)
		{
			threadpool[i] = Executors.newCachedThreadPool();
		}
	}

	public static CGThreadStore getInstance()
	{
		if (instance == null)
			instance = new CGThreadStore();
		return instance;
	}

	public ExecutorService getThreadExecutorService()
	{
		return getThreadExecutorService(0);
	}

	public ExecutorService getThreadExecutorService(int index)
	{
		synchronized (threadpool[index])
		{
			return threadpool[index];
		}
	}

	public ExecutorService getExecutorService(int type)
	{

		synchronized (mutex)
		{
			switch (type)
			{
			case EXEC_THREAD:
				return exec;
			case EXEC_QUEUE_THREAD:
				return execq;
			case EXEC_QUEUE_PROC_THREAD:
				return execqp;
			case CHK_THREAD:
				return chk;
			case EXEC_RET_THREAD:
				return execret;
			case INFO_THREAD:
				return info;
			case INFO_QUEUE_THREAD:
				return infoq;
			case TASK_THREAD:
				return taskexec;
			default:
				return Executors.newCachedThreadPool();
			}
		}
	}

	public void shutdown()
	{
		// try{
		// chk.shutdownNow();chk=null;
		// exec.shutdownNow();exec=null;
		// execq.shutdownNow();execq=null;
		// execqp.shutdownNow();execqp=null;
		// execret.shutdownNow();execret=null;
		// info.shutdownNow();info=null;
		// infoq.shutdownNow();infoq=null;
		// taskexec.shutdownNow();
		// for(int i=0;i<threadpool.length;i++){
		// threadpool[i].shutdownNow();
		// }
		// threadpool=null;
		CGUtil.shutdownNow(chk);
		CGUtil.shutdownNow(exec);
		CGUtil.shutdownNow(execq);
		CGUtil.shutdownNow(execqp);
		CGUtil.shutdownNow(execret);
		CGUtil.shutdownNow(info);
		CGUtil.shutdownNow(infoq);
		CGUtil.shutdownNow(taskexec);
		for (int i = 0; i < threadpool.length; i++)
		{
			CGUtil.shutdownNow(threadpool[i]);
		}
		// }
	}
}
