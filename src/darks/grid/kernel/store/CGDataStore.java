package darks.grid.kernel.store;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.network.ICGHttpNet;
import darks.grid.kernel.network.ICGNet;
import darks.grid.kernel.typedef.ActionEnum;
import darks.grid.util.CGUtil;

public class CGDataStore
{
	private static String UUID = CGUtil.getUUID();
	private static ICGNet net_info = null;
	private static ICGNet net_obj = null;
	private static ICGNet net_result = null;
	private static ICGNet net_check = null;
	private static ICGHttpNet net_http = null;

	private static ICGNode localNode = null;

	public static String connectInfoHost = "127.0.0.1";
	public static int connectInfoPort = 9213;
	// public static String connectObjHost = "172.22.114.191";
	public static int connectObjPort = 20184;
	public static int connectObjRltPort = 51020;
	public static int connectHttpPort = 8686;

	public static int PORT_MAX_OFFSET = 10;

	public static String initInfoHost = "127.0.0.1";
	public static int initInfoPort = 9213; // 消息端口
	public static int initObjPort = 20184; // 对象处理端口
	public static int initObjRltPort = 51020; // 对象结果端口
	public static int initHttpPort = 8686;

	public static String httpRoot = CGUtil.getCurrentPath();
	public static String srcRoot = "src";
	public static String classRoot = "bin";

	private static CGConfigure cfg = null;

	public static int CHK_THREAD_MAXNUM = 1;

	public static int INFO_THREAD_MAXNUM = 2;
	public static int INFO_QUEUE_THREAD_MAXNUM = 2;

	public static int EXEC_THREAD_MAXNUM = 4;
	public static int EXEC_QUEUE_THREAD_MAXNUM = 4;
	public static int EXEC_QUEUE_PROC_THREAD_MAXNUM = 3;

	public static int EXEC_RET_THREAD_MAXNUM = 3;
	public static int EXEC_RET_QUEUE_THREAD_MAXNUM = 3;

	public static int HTTP_THREAD_MAXNUM = 4;

	public static int EXEC_BUFFER_MAXNUM = 4;// EXEC_QUEUE_THREAD_MAXNUM必须不小于EXEC_BUFFER_MAXNUM

	public static int EXEC_PROC_BUFFER_MAXNUM = 3;// EXEC_QUEUE_PROC_THREAD_MAXNUM必须不小于EXEC_PROC_BUFFER_MAXNUM

	public static int PACKAGE_SIZE = 870; // 分割包大小

	// -------------------------------------
	// public static final int ACTION_TIMEOUT_STOP=1;

	// public static final int ACTION_TIMEOUT_RECALL=2;

	private static ActionEnum linkaction = ActionEnum.ACTION_TIMEOUT_STOP; // 超适动作

	private static int linktimeout = 0; // 超时时间

	private static final Object mutex = new Object();

	public static String getUUID()
	{
		return UUID;
	}

	public static ICGNet getNetInfo()
	{
		return net_info;
	}

	public static ICGNet getNetObj()
	{
		return net_obj;
	}

	public static ICGNet getNetResult()
	{
		return net_result;
	}

	public static ICGNet getNetCheck()
	{
		return net_check;
	}

	public static ICGHttpNet getNetHttp()
	{
		return net_http;
	}

	public static void setNetInfo(ICGNet netInfo)
	{
		net_info = netInfo;
	}

	public static void setNetObj(ICGNet netObj)
	{
		net_obj = netObj;
	}

	public static void setNetResult(ICGNet netResult)
	{
		net_result = netResult;
	}

	public static void setNetCheck(ICGNet netCheck)
	{
		net_check = netCheck;
	}

	public static void setNetHttp(ICGHttpNet netHttp)
	{
		net_http = netHttp;
	}

	public static ICGNode getLocalNode()
	{
		return localNode;
	}

	public static void setLocalNode(ICGNode localNode)
	{
		CGDataStore.localNode = localNode;
	}

	public static boolean isCheckNull()
	{
		return (net_check == null);
	}

	public static boolean isInfoNull()
	{
		return (net_info == null);
	}

	public static boolean isObjNull()
	{
		return (net_obj == null);
	}

	public static boolean isObjRltNull()
	{
		return (net_result == null);
	}

	public static boolean isHttpNull()
	{
		return (net_http == null);
	}

	public static boolean loadConfig() throws Exception
	{
		if (cfg == null)
			cfg = new CGConfigure();
		return cfg.loadConfig();
	}

	public static boolean loadConfig(String strcfg) throws Exception
	{
		if (cfg == null)
			cfg = new CGConfigure(strcfg);
		return cfg.loadConfig();
	}

	public static boolean loadConfig(CGConfigure pcfg) throws Exception
	{
		cfg = pcfg;
		return cfg.loadConfig();
	}

	// ---------------------------------------------
	public static void setLinkTimeout(int timeout, ActionEnum action)
	{
		synchronized (mutex)
		{
			linkaction = action;
			linktimeout = timeout;
		}
	}

	public static ActionEnum getLinkTimeoutAction()
	{
		synchronized (mutex)
		{
			return linkaction;
		}
	}

	public static int getLinkTimeout()
	{
		synchronized (mutex)
		{
			return linktimeout;
		}
	}
}
