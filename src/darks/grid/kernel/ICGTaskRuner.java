package darks.grid.kernel;

import java.util.concurrent.Callable;

public interface ICGTaskRuner<V> extends Callable<V>
{

	public V execute() throws Exception;
}
