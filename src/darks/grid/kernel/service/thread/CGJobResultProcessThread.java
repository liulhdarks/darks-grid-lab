package darks.grid.kernel.service.thread;

import darks.grid.kernel.ICGJobResult;
import darks.grid.kernel.job.CGApartJobPackage;
import darks.grid.kernel.job.CGJobPackage;
import darks.grid.kernel.job.CGJobResult;
import darks.grid.kernel.meter.ICGJobResultApartMeter;
import darks.grid.kernel.meter.ICGJobResultMeter;
import darks.grid.kernel.meter.ICGObjectMeter;
import darks.grid.kernel.network.ICGEvent;
import darks.grid.kernel.store.CGDataCenter;
import darks.grid.kernel.store.CGPrintCenter;
import darks.grid.kernel.store.job.CGJobApartStore;
import darks.grid.kernel.typedef.DS;
import darks.grid.kernel.typedef.MF;
import darks.grid.util.CGUtil;

public class CGJobResultProcessThread implements Runnable
{

	private ICGEvent e;

	public CGJobResultProcessThread()
	{

	}

	public CGJobResultProcessThread(ICGEvent e)
	{
		this.e = e;
	}

	public void run()
	{
		try
		{

			ICGObjectMeter ometer = (ICGObjectMeter) CGUtil.ByteToObject(e.getData());
			ICGJobResultMeter meter = null;
			if (ometer.getType() == 1)
			{
				meter = (ICGJobResultMeter) ometer.getObject();

			}
			else if (ometer.getType() == 2)
			{
				ICGJobResultApartMeter pack = (ICGJobResultApartMeter) ometer.getObject();

				CGPrintCenter.printObject("[DATA]收到分包数据(" + pack.getIndex() + "/"
						+ pack.getNumber() + "):数据便偏移" + pack.getPartData().getStart() + " 数据包大小"
						+ pack.getPartData().getSize() + " 原包大小"
						+ pack.getPartData().getSrclength());
				CGApartJobPackage apart = CGJobApartStore.getInstance().addJobResultApartMeter(
						pack, ometer.getFromHost(), ometer.getFromInfoPort(),
						ometer.getFromObjPort());
				if (apart.isFinshed())
				{
					if (!apart.isComplete())
					{
						meter = apart.getResultMeter();
						CGJobApartStore.getInstance().delApartJobPackage(pack.getResultMeterId());
						DS.getNetInfo().response(ometer.getFromHost(), ometer.getFromInfoPort(),
								MF.createDataMessage(MF.DATA_APART_COMPLETE, meter.getId()));
					}
				}
			}
			if (meter == null)
				return;
			ICGJobResult rlt = new CGJobResult(meter);
			CGJobPackage jpack = CGDataCenter.getInstance().getJobPackage(meter.getTaskId());
			if (meter.isFail())
				CGPrintCenter.printObject("[FAIL]" + meter.getNodeid() + " " + rlt.getFinishtime()
						+ " " + meter.getError());
			if (jpack == null)
				throw new Exception(
						"[ERROR]CGExecutorResultListener.CGJobPackage is null, the task "
								+ meter.getTaskId() + " may be missing");
			jpack.addJobResult(rlt);
		}
		catch (Exception ex)
		{
			System.out.println(ex.toString());
		}
	}

}
