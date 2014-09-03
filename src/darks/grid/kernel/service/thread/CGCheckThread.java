package darks.grid.kernel.service.thread;

import java.util.Iterator;
import java.util.LinkedList;

import darks.grid.kernel.ICGNode;
import darks.grid.kernel.factory.CGNodeFactory;
import darks.grid.kernel.meter.ICGMeter;
import darks.grid.kernel.service.CGCheckService;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGThreadStore;
import darks.grid.util.CGUtil;

public class CGCheckThread extends Thread
{
	private long chkid = this.getId();
	private CGCheckService service;

	public CGCheckThread()
	{
	}

	public CGCheckThread(CGCheckService service)
	{
		this.service = service;
	}

	public void run()
	{

		while (!service.isClose())
		{
			try
			{
				if (!CGDataCenter.getInstance().getNodeMap().isEmpty())
				{
					Iterator<String> iterator = CGDataCenter.getInstance().getNodeMap().keySet()
							.iterator();
					while (iterator.hasNext())
					{
						String uid = (String) iterator.next();
						ICGNode node = CGDataCenter.getInstance().getCGNode(uid);
						if (!CGCheckService.checkJoinConnection(
								(String) node.getNodeInfo("HOSTIP"),
								(Integer) node.getNodeInfo("INFOPORT"), 1000))
						{
							if (!CGCheckService.checkJoinConnection(
									(String) node.getNodeInfo("HOSTIP"),
									(Integer) node.getNodeInfo("INFOPORT"), 500))
							{
								CGNodeFactory.getInstance().addHistoryNode((ICGNode) node.clone());
								iterator.remove();
								if (node.getJobSize() > 0)
								{
									LinkedList<ICGMeter> list = new LinkedList<ICGMeter>(node
											.getJobs().values());
									if (list.size() > 0)
									{
										try
										{
											CGRecallThread recall = new CGRecallThread(list);
											CGThreadStore.getInstance()
													.getExecutorService(CGThreadStore.TASK_THREAD)
													.execute(recall);
										}
										catch (Exception e)
										{
											try
											{
												CGRecallThread recall = (CGRecallThread) CGUtil
														.newInstance(Class
																.forName("darks.grid.kernel.service.thread.CGRecallThread"));
												recall.setMeter(list);
												CGThreadStore
														.getInstance()
														.getExecutorService(
																CGThreadStore.TASK_THREAD)
														.execute(recall);
											}
											catch (Exception ex)
											{
												ex.printStackTrace();
											}
										}
									}

								}
								System.out.println(node.getUUId() + "响应超时，从节点列表移除");
							}
						}
						sleep(10);
					}
				}
				sleep(1500);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

}
