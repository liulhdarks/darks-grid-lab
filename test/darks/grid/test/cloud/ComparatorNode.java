package darks.grid.test.cloud;

import java.util.Comparator;

import darks.grid.kernel.ICGNode;

public class ComparatorNode implements Comparator
{

	public int compare(Object arg0, Object arg1)
	{
		ICGNode node1 = (ICGNode) arg0;
		ICGNode node2 = (ICGNode) arg1;
		String[] n1 = node1.getAddress().split(".");
		String[] n2 = node2.getAddress().split(".");
		return node1.getAddress().compareTo(node2.getAddress());
	}

}
