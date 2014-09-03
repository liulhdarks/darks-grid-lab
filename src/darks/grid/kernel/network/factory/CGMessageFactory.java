package darks.grid.kernel.network.factory;

import darks.grid.kernel.store.CGDataStore;

public class CGMessageFactory
{
	public final static int ZERO = 0;
	public final static int CONN_CHK = 1010;// $(CONN_CHK)_ID_IP:INFOPORT
	public final static int CONN_CHK_BACK = 1011;// $(CONN_CHK_BACK)_ID_IP:INFOPORT
	public final static int JOIN_NODE = 1020;// $(JOIN_NODE)_ID_IP:INFOPORT:OBJPORT
	public final static int JOIN_NODE_BACK = 1021;// $(JOIN_NODE)_ID_IP:INFOPORT:OBJPORT
	public final static int JOIN_NODE_NOBACK = 1022;// $(JOIN_NODE)_ID_IP:INFOPORT:OBJPORT
	public final static int DATA_RECALL = 1031;// $(DATA_RECALL)_ID_METERID_RECALLDATA
	public final static int DATA_APART_COMPLETE = 1032;// $(DATA_APART_COMPLETE)_ID_METERID

	public static String createMessage(int mtype)
	{
		switch (mtype)
		{
		case CONN_CHK:
			return CONN_CHK + "_" + CGDataStore.getUUID() + "_"
					+ CGDataStore.getNetInfo().getAddress() + ":"
					+ CGDataStore.getNetInfo().getPort();
		case CONN_CHK_BACK:
			return CONN_CHK_BACK + "_" + CGDataStore.getUUID() + "_"
					+ CGDataStore.getNetInfo().getAddress() + ":"
					+ CGDataStore.getNetInfo().getPort();
		case JOIN_NODE:
			return JOIN_NODE + "_" + CGDataStore.getUUID() + "_"
					+ CGDataStore.getNetInfo().getAddress() + ":"
					+ CGDataStore.getNetInfo().getPort() + ":" + CGDataStore.getNetObj().getPort()
					+ ":" + CGDataStore.getNetResult().getPort() + ":"
					+ CGDataStore.getNetHttp().getPort();
		case JOIN_NODE_NOBACK:
			return JOIN_NODE_NOBACK + "_" + CGDataStore.getUUID() + "_"
					+ CGDataStore.getNetInfo().getAddress() + ":"
					+ CGDataStore.getNetInfo().getPort() + ":" + CGDataStore.getNetObj().getPort()
					+ ":" + CGDataStore.getNetResult().getPort() + ":"
					+ CGDataStore.getNetHttp().getPort();
		}
		return "";
	}

	public static String createMessage(int mtype, String uid, String addr, int infoport,
			int objport, int rltport, int httpport)
	{
		switch (mtype)
		{
		case CONN_CHK:
		case CONN_CHK_BACK:
		case JOIN_NODE:
		case JOIN_NODE_NOBACK:
			return createMessage(mtype);
		case JOIN_NODE_BACK:
			return JOIN_NODE_BACK + "_" + uid + "_" + addr + ":" + infoport + ":" + objport + ":"
					+ rltport + ":" + httpport;

		}
		return "";
	}

	public static String createDataMessage(int mtype, Object obj)
	{
		switch (mtype)
		{
		case DATA_RECALL:
			return DATA_RECALL + "_" + CGDataStore.getUUID() + "_" + obj;
		case DATA_APART_COMPLETE:
			return DATA_APART_COMPLETE + "_" + CGDataStore.getUUID() + "_" + obj;
		}
		return "";
	}

	public static int matchMessage(int mtype)
	{
		switch (mtype)
		{
		case CONN_CHK:
			return CONN_CHK_BACK;
		case JOIN_NODE:
			return JOIN_NODE_BACK;
		case JOIN_NODE_BACK:
			return JOIN_NODE_NOBACK;
		default:
			return 0;
		}
	}
}
