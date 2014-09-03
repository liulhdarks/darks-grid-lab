package darks.grid.kernel;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

public interface ICGJob<G> extends Serializable, Callable
{
	public String getJobId();

	public void cancel();

	public Object execute();

	public G getParameter();

	public G getParameter(int pos);

	public List<G> getParameterList();

	public boolean isEmpty();
}
