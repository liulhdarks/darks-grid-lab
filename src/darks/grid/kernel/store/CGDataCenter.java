package darks.grid.kernel.store;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGFutureJobTask;
import darks.grid.kernel.job.CGJobPackage;
import darks.grid.kernel.network.ICGEvent;

public class CGDataCenter
{
	private static CGDataCenter instance = null;

	public static final Object mutex = new Object();

	// 节点集合
	private ConcurrentSkipListMap<String, ICGNode> node_map = new ConcurrentSkipListMap<String, ICGNode>();

	// 信息缓冲队列
	private BlockingQueue<ICGEvent> info_buffer = new LinkedBlockingQueue<ICGEvent>();

	// private Object mutex_exec=new Object();
	private int exec_index = 0;
	//
	// private Object mutex_pexec=new Object();
	private int pexec_index = 0;

	private final ReentrantReadWriteLock readWriteLockExec = new ReentrantReadWriteLock();
	private final Lock readExec = readWriteLockExec.readLock();
	private final Lock writeExec = readWriteLockExec.writeLock();

	private final ReentrantReadWriteLock readWriteLockExecp = new ReentrantReadWriteLock();
	private final Lock readExecp = readWriteLockExecp.readLock();
	private final Lock writeExecp = readWriteLockExecp.writeLock();

	private final ReentrantReadWriteLock readWriteLockJpack = new ReentrantReadWriteLock();
	private final Lock readjpack = readWriteLockJpack.readLock();
	private final Lock writejpack = readWriteLockJpack.writeLock();

	private final ReentrantReadWriteLock readWriteLockNode = new ReentrantReadWriteLock();
	private final Lock readNode = readWriteLockNode.readLock();
	private final Lock writeNode = readWriteLockNode.writeLock();

	// 执行缓冲队列
	// private BlockingQueue<ICGEvent> exec_buffer=new
	// LinkedBlockingQueue<ICGEvent>();
	private ConcurrentLinkedQueue<ICGEvent>[] exec_buffer;

	// 执行处理缓冲队列
	// private BlockingQueue<CGFutureJobTask> execProcBuffer=new
	// LinkedBlockingQueue<CGFutureJobTask>();
	private ConcurrentLinkedQueue<CGFutureJobTask>[] execProcBuffer;

	// 结果集合
	private ConcurrentSkipListMap<String, CGJobPackage> jobpack_map = new ConcurrentSkipListMap<String, CGJobPackage>();

	// --------------------------------------------------------------------------
	public CGDataCenter()
	{
		initExecBuffer();
		initExecProcBuffer();
	}

	public static synchronized CGDataCenter getInstance()
	{
		if (instance == null)
		{
			instance = new CGDataCenter();
		}
		return instance;
	}

	// CG节点----------------------------------------------------------------------
	public ConcurrentSkipListMap<String, ICGNode> getNodeMap()
	{

		readNode.lock();
		try
		{
			// synchronized (mutex) {
			return node_map;
			// }
		}
		finally
		{
			readNode.unlock();
		}
	}

	public void setNodeMap(ConcurrentSkipListMap<String, ICGNode> node_map)
	{
		this.node_map = node_map;
	}

	public boolean addCGNode(String key, ICGNode node)
	{
		writeNode.lock();
		try
		{
			if (containCGNode(node.getAddress(), node.getInfoPort()))
				return false;
			// synchronized (mutex) {
			node_map.put(key, node);
			return true;
			// }
		}
		finally
		{
			writeNode.unlock();
		}
	}

	public ICGNode getCGNode(String key)
	{
		readNode.lock();
		try
		{
			// synchronized (mutex) {
			return node_map.get(key);
			// }
		}
		finally
		{
			readNode.unlock();
		}
	}

	public boolean containsCGNode(String key)
	{
		return node_map.containsKey(key);
	}

	public List<ICGNode> getNodeList()
	{
		readNode.lock();
		try
		{
			// synchronized (mutex) {
			List<ICGNode> list = new ArrayList<ICGNode>();
			for (Iterator<ICGNode> it = node_map.values().iterator(); it.hasNext();)
			{
				ICGNode node = (ICGNode) it.next().clone();
				list.add(node);
			}
			list.add((ICGNode) CGDataStore.getLocalNode().clone());
			return list;
			// }
		}
		finally
		{
			readNode.unlock();
		}
	}

	public boolean containCGNode(String addr, int port)
	{
		for (Iterator<ICGNode> it = node_map.values().iterator(); it.hasNext();)
		{
			ICGNode node = (ICGNode) it.next();
			if (node.getAddress().equals(addr) && node.getInfoPort() == port)
			{
				return true;
			}
		}
		return false;
	}

	// 信息缓冲队列----------------------------------------------------------------------
	public BlockingQueue<ICGEvent> getInfoBuffer()
	{
		return info_buffer;
	}

	public void setInfoBuffer(BlockingQueue<ICGEvent> info_buffer)
	{
		this.info_buffer = info_buffer;
	}

	public ICGEvent deInfoQueue() throws InterruptedException
	{
		return info_buffer.take();
	}

	public void enInfoQueue(ICGEvent info) throws InterruptedException
	{
		info_buffer.put(info);
	}

	public boolean isInfoQueueEmpty()
	{
		return info_buffer.isEmpty();
	}

	// 执行缓冲队列-------------------------------------------------------------------------
	public void initExecBuffer()
	{
		exec_buffer = new ConcurrentLinkedQueue[CGDataStore.EXEC_BUFFER_MAXNUM];
		for (int i = 0; i < CGDataStore.EXEC_BUFFER_MAXNUM; i++)
		{
			exec_buffer[i] = new ConcurrentLinkedQueue<ICGEvent>();
		}
	}

	public ICGEvent deExecQueue(int index) throws InterruptedException, Exception
	{
		readExec.lock();
		try
		{
			return exec_buffer[index].poll();
		}
		finally
		{
			readExec.unlock();
		}
	}

	public int enExecQueue(ICGEvent info) throws InterruptedException, Exception
	{
		writeExec.lock();
		try
		{
			int index = 0;
			// synchronized (mutex_exec) {
			index = exec_index++;
			if (exec_index >= CGDataStore.EXEC_BUFFER_MAXNUM)
				exec_index = 0;
			// }
			exec_buffer[index].offer(info);
			return index;
		}
		finally
		{
			writeExec.unlock();
		}

	}

	public boolean isExecQueueEmpty(int index)
	{
		return exec_buffer[index].isEmpty();
	}

	// 执行处理队列-------------------------------------------------------------------------

	public void initExecProcBuffer()
	{
		execProcBuffer = new ConcurrentLinkedQueue[CGDataStore.EXEC_PROC_BUFFER_MAXNUM];
		for (int i = 0; i < CGDataStore.EXEC_PROC_BUFFER_MAXNUM; i++)
		{
			execProcBuffer[i] = new ConcurrentLinkedQueue<CGFutureJobTask>();
		}
	}

	public CGFutureJobTask deExecProcQueue(int index) throws InterruptedException, Exception
	{
		readExecp.lock();
		try
		{
			return execProcBuffer[index].poll();
		}
		finally
		{
			readExecp.unlock();
		}
	}

	public int enExecProcQueue(CGFutureJobTask fut) throws InterruptedException, Exception
	{
		writeExecp.lock();
		try
		{
			int index = 0;
			// synchronized (mutex_pexec) {
			index = pexec_index++;
			if (pexec_index >= CGDataStore.EXEC_PROC_BUFFER_MAXNUM)
				pexec_index = 0;
			// }
			execProcBuffer[index].offer(fut);
			return index;
		}
		finally
		{
			writeExecp.unlock();
		}

	}

	public boolean isExecProcQueueEmpty(int index)
	{
		return execProcBuffer[index].isEmpty();
	}

	// 结果集合-------------------------------------------------------------------------
	public ConcurrentSkipListMap<String, CGJobPackage> getJobResultMap()
	{
		return jobpack_map;
	}

	public CGJobPackage getJobPackage(String taskId)
	{
		readjpack.lock();
		try
		{
			return jobpack_map.get(taskId);
		}
		finally
		{
			readjpack.unlock();
		}
	}

	public CGJobPackage addTaskToJobPackage(String taskId)
	{
		writejpack.lock();
		try
		{
			CGJobPackage pack = new CGJobPackage(taskId);
			jobpack_map.put(taskId, pack);
			return pack;
		}
		finally
		{
			writejpack.unlock();
		}
	}

}
