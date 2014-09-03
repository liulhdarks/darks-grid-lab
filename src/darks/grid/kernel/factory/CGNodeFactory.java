package darks.grid.kernel.factory;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.ICGNodeFactory;
import darks.grid.kernel.Impl.CGNode;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;

public class CGNodeFactory implements ICGNodeFactory
{
	private static CGNodeFactory instance = null;

	private ConcurrentSkipListMap<String, ICGNode> histroy_node = new ConcurrentSkipListMap<String, ICGNode>();

	public ICGNode createNode(String[] arg)
	{// $(JOIN_NODE)_ID_IP:INFOPORT:OBJPORT:RLTPORT
		CGNode node = new CGNode(arg[1]);
		String[] tmp = arg[2].split(":");
		node.setNodeInfo(CGNode.HOSTIP, tmp[0]);
		node.setNodeInfo(CGNode.INFOPORT, Integer.parseInt(tmp[1]));
		node.setNodeInfo(CGNode.OBJPORT, Integer.parseInt(tmp[2]));
		node.setNodeInfo(CGNode.RLTPORT, Integer.parseInt(tmp[3]));
		node.setNodeInfo(CGNode.HTTPPORT, Integer.parseInt(tmp[4]));
		if (!CGDataCenter.getInstance().addCGNode(node.getUUId(), node))
			return null;
		String tmpid = containsHistoryNode(node.getAddress(), node.getInfoPort());
		if (tmpid != null)
		{
			removeHistoryNode(tmpid);
		}
		return node;
	}

	public ICGNode createLocalNode()
	{
		CGNode node = new CGNode(CGDataStore.getUUID());
		node.setNodeInfo(CGNode.HOSTIP, CGDataStore.initInfoHost);
		node.setNodeInfo(CGNode.INFOPORT, CGDataStore.initInfoPort);
		node.setNodeInfo(CGNode.OBJPORT, CGDataStore.initObjPort);
		node.setNodeInfo(CGNode.RLTPORT, CGDataStore.initObjRltPort);
		node.setNodeInfo(CGNode.HTTPPORT, CGDataStore.initHttpPort);
		CGDataStore.setLocalNode(node);
		return node;
	}

	public List<ICGNode> getNodes()
	{
		return CGDataCenter.getInstance().getNodeList();
	}

	public ICGNode getLocalNode()
	{
		return CGDataStore.getLocalNode();
	}

	public ICGNode getNode(String nodeId)
	{
		return CGDataCenter.getInstance().getCGNode(nodeId);
	}

	public ICGNode getNode(String address, int infoPort)
	{
		ConcurrentMap<String, ICGNode> map = CGDataCenter.getInstance().getNodeMap();
		for (Entry<String, ICGNode> e : map.entrySet())
		{
			ICGNode node = e.getValue();
			if (node.getAddress().equals(address) && node.getInfoPort() == infoPort)
			{
				return node;
			}
		}
		return null;
	}

	public ConcurrentSkipListMap<String, ICGNode> getHistoryNodes()
	{
		return histroy_node;
	}

	public ICGNode getHistoryNode(String id)
	{
		if (histroy_node.containsKey(id))
		{
			return histroy_node.get(id);
		}
		return null;
	}

	public boolean containsHistoryNode(String id)
	{
		if (histroy_node.containsKey(id))
		{
			return true;
		}
		return false;
	}

	public String containsHistoryNode(String ip, int port)
	{
		for (ICGNode node : histroy_node.values())
		{
			if (node.getAddress().equals(ip) && node.getInfoPort() == port)
			{
				return node.getUUId();
			}
		}
		return null;
	}

	public void addHistoryNode(ICGNode node)
	{
		histroy_node.put(node.getUUId(), node);
	}

	public void removeHistoryNode(String id)
	{
		for (Iterator<String> it = histroy_node.keySet().iterator(); it.hasNext();)
		{
			String key = (String) it.next();
			if (key.equals(id))
			{
				it.remove();
				break;
			}
		}
	}

	public static ICGNodeFactory getInstance()
	{
		if (instance == null)
		{
			instance = new CGNodeFactory();
		}
		return instance;
	}
}
