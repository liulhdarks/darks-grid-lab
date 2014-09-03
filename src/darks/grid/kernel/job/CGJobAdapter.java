package darks.grid.kernel.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import darks.grid.kernel.ICGJob;
import darks.grid.util.CGUtil;

public abstract class CGJobAdapter<G extends Object> implements ICGJob<G>
{

	private String UUID;
	private List<G> list;
	private volatile boolean cancelled = false;

	public CGJobAdapter()
	{
		list = new ArrayList<G>();
		UUID = CGUtil.getUUID();
	}

	public CGJobAdapter(G... args)
	{
		UUID = CGUtil.getUUID();
		if (args == null)
		{
			this.list = new ArrayList<G>(1);
		}
		else
		{
			this.list = new ArrayList<G>(args.length);
			this.list.addAll(Arrays.asList(args));
		}
	}

	public String getJobId()
	{
		return UUID;
	}

	public void addParameter(G param)
	{
		list.add(param);
	}

	public void setParameter(int pos, G param)
	{
		if (pos < 0 || pos >= list.size())
			return;
		list.set(pos, param);
	}

	public G getParameter()
	{
		return isEmpty() == true ? null : list.get(0);
	}

	public G getParameter(int pos)
	{
		if (pos < 0 || pos >= list.size())
			return null;
		return isEmpty() == true ? null : list.get(pos);
	}

	public List<G> getParameterList()
	{
		return list;
	}

	public boolean isEmpty()
	{
		if (list.size() == 0)
			return true;
		return false;
	}

	public void cancel()
	{
		cancelled = true;
	}

	public boolean isCancel()
	{
		return cancelled;
	}

	public final Object call() throws Exception
	{
		return execute();
	}

}
