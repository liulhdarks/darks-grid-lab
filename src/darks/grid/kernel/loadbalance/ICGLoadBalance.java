package darks.grid.kernel.loadbalance;

import java.util.Collection;
import java.util.List;

import darks.grid.kernel.ICGJob;
import darks.grid.kernel.ICGNode;

public interface ICGLoadBalance
{

	public void init(Collection<ICGNode> nodes);

	public ICGNode getBalancedNode() throws Exception;

	public ICGNode getBalancedNode(List<ICGNode> nodes) throws Exception;

	public ICGNode getBalancedNode(List<ICGNode> nodes, ICGJob job) throws Exception;
}
