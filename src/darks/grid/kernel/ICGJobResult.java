package darks.grid.kernel;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

public interface ICGJobResult extends Serializable
{

	public <T> T getData() throws ExecutionException, InterruptedException;

	public String getId();

	public ICGNode getNode();

	public String getMeterId();

	public Object getResult();

	public String getError();

	public String getFinishtime();

	public String getNodeid();

	public String getJobId();

	public String getTaskId();

	public int getType();

	public boolean isFail();

	public boolean isCanceled();

}
