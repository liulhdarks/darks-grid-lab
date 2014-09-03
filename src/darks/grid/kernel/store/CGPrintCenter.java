package darks.grid.kernel.store;

import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.Impl.CGNode;

public class CGPrintCenter
{

	private static Object mutex = new Object();

	private static Lock lock = new ReentrantLock();

	public static void printHeader()
	{
		lock.lock();
		// synchronized (mutex) {

		System.out.println("*************************************************************\n"
				+ "Start Up Cloud Node. ID:" + CGDataStore.getUUID() + "\n"
				+ "-------------------------------------------------------------\n"
				+ "              /-----------\\             /------------\\\n"
				+ "             / /---------\\ \\           / /----------\\ \\\n"
				+ "            / /           \\/          / /            \\/\n"
				+ "           | |                       | |\n"
				+ "           | |                       | |\n"
				+ "           | |                       | |      |-------|\n"
				+ "           | |                       | |      |-----| |\n"
				+ "            \\ \\           /\\          \\ \\           | |\n"
				+ "	     \\ \\---------/ /           \\ \\----------| |\n"
				+ "              \\-----------/             \\-----------| |\n"
				+ "                                                    |-|\n"
				+ "-------------------------------------------------------------\n"
				+ "CloudGrid 1.0 demo version CopyRight@2011 刘力华 CQUPT Blues\n"
				+ "*************************************************************\n");

		// }
		lock.unlock();
	}

	public static void printObject(Object obj)
	{
		lock.lock();
		// synchronized (mutex) {
		System.out.println(obj);
		// }
		lock.unlock();
	}

	public static void printNodeList()
	{
		lock.lock();
		if (CGDataStore.getLocalNode() == null)
			return;
		System.out.println("------------------------------------------------");
		System.out.println("节点数:" + (CGDataCenter.getInstance().getNodeMap().size() + 1));
		for (Entry<String, ICGNode> e : CGDataCenter.getInstance().getNodeMap().entrySet())
		{
			ICGNode node = e.getValue();
			System.out.println(node.getNodeInfo(CGNode.HOSTIP) + ":"
					+ node.getNodeInfo(CGNode.INFOPORT));
		}
		System.out.println(CGDataStore.getLocalNode().getNodeInfo(CGNode.HOSTIP) + ":"
				+ CGDataStore.getLocalNode().getNodeInfo(CGNode.INFOPORT) + " [THIS]");
		System.out.println("------------------------------------------------");
		lock.unlock();
	}
}
