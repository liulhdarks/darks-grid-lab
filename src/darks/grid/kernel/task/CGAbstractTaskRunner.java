package darks.grid.kernel.task;

import darks.grid.kernel.ICGTaskRuner;
import darks.grid.util.CGUtil;

public abstract class CGAbstractTaskRunner<V> implements ICGTaskRuner<V>
{

	protected String uuid = CGUtil.getUUID();

	public V call() throws Exception
	{
		return execute();
	}

	public V execute() throws Exception
	{
		return null;
	}

	public String getUUID()
	{
		return uuid;
	}
}
