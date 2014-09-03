package darks.grid.kernel.service.thread;

import java.util.Map.Entry;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.factory.CGNodeFactory;
import darks.grid.kernel.network.ICGEvent;
import darks.grid.kernel.network.ICGNet;
import darks.grid.kernel.network.factory.CGMessageFactory;
import darks.grid.kernel.service.CGInfoService;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.kernel.store.CGPrintCenter;
import darks.grid.kernel.store.job.CGJobReResponseStore;
import darks.grid.kernel.typedef.MF;

public class CGInfoQueueThread extends Thread
{
	private long infoqid = this.getId();
	private CGInfoService service = null;
	private static final Object mux = new Object();

	public CGInfoQueueThread()
	{
	}

	public CGInfoQueueThread(CGInfoService service)
	{
		this.service = service;
	}

	public void run()
	{
		while (!service.isClose())
		{
			try
			{
				while (!CGDataCenter.getInstance().isInfoQueueEmpty())
				{
					ICGEvent e = CGDataCenter.getInstance().deInfoQueue();
					process(e.getData(), e.getAddress(), e.getPort());
				}
				sleep(10);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void process(byte[] btinfo, String addr, int port)
	{
		ICGNet net = CGDataStore.getNetInfo();
		String info = new String(btinfo);

		info = info.trim();
		String[] arg = info.split("_");
		if (arg.length < 2)
			return;
		try
		{
			// 得到命令后，在开始处处理------------------------------------
			int mtype = Integer.parseInt(arg[0]);
			if (mtype != CGMessageFactory.CONN_CHK)
			{
				CGPrintCenter.printObject("[MESSAGE]" + info);
			}
			ICGNode cnode = null;
			switch (mtype)
			{
			case CGMessageFactory.JOIN_NODE:
			case CGMessageFactory.JOIN_NODE_BACK:
			case CGMessageFactory.JOIN_NODE_NOBACK:
				cnode = CGNodeFactory.getInstance().createNode(arg);
				if (cnode == null)
					return;
				CGPrintCenter.printObject("接受节点" + cnode.getUUId() + "加入请求");
				CGPrintCenter.printNodeList();
				break;
			case CGMessageFactory.DATA_RECALL:
				CGJobReResponseStore.processReresponseMsg(arg);
				break;
			case MF.DATA_APART_COMPLETE:
				// if(arg.length>=3){
				CGJobReResponseStore.delReResponseStorePackage(arg[2]);
				// }
				break;
			}

			// 得到命令后，中部匹配转换，得到下一条处理命令---------------------------------
			String back = "";
			int btype = CGMessageFactory.matchMessage(mtype);
			if (btype == CGMessageFactory.ZERO)
				return;
			// 得到转换后的命令，执行相应操作---------------------------------
			if (btype == CGMessageFactory.JOIN_NODE_BACK)
			{ // 下一条命令为(有返回)加入命令时执行
				if (!CGDataCenter.getInstance().getNodeMap().isEmpty())
				{
					for (Entry<String, ICGNode> entry : CGDataCenter.getInstance().getNodeMap()
							.entrySet())
					{
						// String uid=entry.getKey();
						ICGNode node = entry.getValue();
						if (node.getUUId().equals(arg[1]))
							continue;
						back = CGMessageFactory.createMessage(btype, node.getUUId(),
								(String) node.getNodeInfo("HOSTIP"),
								(Integer) node.getNodeInfo("INFOPORT"),
								(Integer) node.getNodeInfo("OBJPORT"),
								(Integer) node.getNodeInfo("RLTPORT"),
								(Integer) node.getNodeInfo("HTTPPORT"));
						if (back.equals(""))
							continue;
						net.response(addr, port, back);
					}
					back = CGMessageFactory.createMessage(CGMessageFactory.JOIN_NODE_NOBACK);
					if (!back.equals(""))
						net.response(addr, port, back);
				}
			}
			else if (btype == CGMessageFactory.JOIN_NODE_NOBACK)
			{ // 下一条命令为(无返回)加入命令时执行
				back = CGMessageFactory.createMessage(btype);
				if (back.equals(""))
					return;
				net.response((String) cnode.getNodeInfo("HOSTIP"),
						(Integer) cnode.getNodeInfo("INFOPORT"), back);

			}
			else if (btype != CGMessageFactory.ZERO)
			{ // 下一条命令不为空时执行
				back = CGMessageFactory.createMessage(btype);
				if (back.equals(""))
					return;
				net.response(addr, port, back);
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

}
