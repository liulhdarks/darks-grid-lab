package darks.grid.kernel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ICGTask<T, R> extends Serializable
{
	public static final long serialVersionUID = 1L;

	public Map<? extends ICGJob, ICGNode> map(List<ICGNode> subgrid, T arg);

	public Collection<? extends ICGJob> split(int csize, T arg);

	public R reduce(List<ICGJobResult> results);

}
