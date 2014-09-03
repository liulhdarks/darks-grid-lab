package darks.grid.kernel.thread;

import java.util.List;

import darks.grid.kernel.job.CGApartJobPackage;
import darks.grid.kernel.network.factory.CGMessageFactory;
import darks.grid.kernel.store.CGDataStore;
import darks.grid.util.CGUtil;

public class CGApartJobPackageListener implements Runnable
{

	private CGApartJobPackage pack;

	public CGApartJobPackageListener(CGApartJobPackage pack)
	{
		this.pack = pack;
	}

	public void run()
	{
		try
		{
			while (!pack.isFinshed())
			{
				long cur = System.currentTimeMillis();
				if (cur - pack.getCurtime() >= 150)
				{
					List<String> list = pack.getIndexrs();
					// for(String s:list){
					// System.out.println("申请重发:"+s);
					// }
					String msg = CGUtil.NumberLine(list);
					// msg+=msg;
					msg = pack.getMeterResultId() + msg;
					System.out.println("数据包部分丢失，请求重发消息:" + msg);
					if (!pack.isFinshed())
						CGDataStore.getNetInfo().response(
								pack.getHost(),
								pack.getInfoport(),
								CGMessageFactory.createDataMessage(CGMessageFactory.DATA_RECALL,
										msg));
				}
				Thread.sleep(10);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
