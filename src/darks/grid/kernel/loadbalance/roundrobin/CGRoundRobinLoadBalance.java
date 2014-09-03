package darks.grid.kernel.loadbalance.roundrobin;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGNode;
import darks.grid.kernel.loadbalance.ICGLoadBalance;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGDataStore;

public class CGRoundRobinLoadBalance implements ICGLoadBalance
{

	private final LinkedList<ICGNode> nodeQueue = new LinkedList<ICGNode>();

	private final Object mux = new Object();

	public CGRoundRobinLoadBalance()
	{
	}

	public CGRoundRobinLoadBalance(Collection<ICGNode> nodes)
	{
		init(nodes);
	}

	public ICGNode getBalancedNode(List<ICGNode> nodes) throws Exception
	{
		return getBalancedNode(nodes, null);
	}

	public ICGNode getBalancedNode(List<ICGNode> nodes, ICGJob job) throws Exception
	{
		return getBalancedNode();
	}

	public ICGNode getBalancedNode() throws Exception
	{

		synchronized (mux)
		{
			ICGNode found = null;

			for (Iterator<ICGNode> iter = nodeQueue.iterator(); iter.hasNext();)
			{
				ICGNode node = iter.next();

				if (CGDataCenter.getInstance().getNodeMap().containsKey(node.getUUId())
						|| node.getUUId().equals(CGDataStore.getLocalNode().getUUId()))
				{
					found = node;

					iter.remove();

					break;
				}
			}

			if (found != null)
			{
				nodeQueue.addLast(found);
				return found;
			}
			else
			{
				System.out.println("Task topology does not have alive nodes");
				nodeQueue.addLast(CGDataStore.getLocalNode());
				return CGDataStore.getLocalNode();
			}
		}

	}

	public void init(Collection<ICGNode> nodes)
	{
		nodeQueue.addAll(nodes);
	}

}
