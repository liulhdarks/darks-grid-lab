package darks.grid.kernel;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

public interface ICGNodeFactory
{
	public ICGNode createNode(String[] arg);

	public ICGNode createLocalNode();

	public List<ICGNode> getNodes();

	public ICGNode getLocalNode();

	public ICGNode getNode(String nodeId);

	public ICGNode getNode(String address, int infoPort);

	public ConcurrentSkipListMap<String, ICGNode> getHistoryNodes();

	public ICGNode getHistoryNode(String id);

	public boolean containsHistoryNode(String id);

	public String containsHistoryNode(String ip, int port);

	public void addHistoryNode(ICGNode node);

	public void removeHistoryNode(String id);
}
