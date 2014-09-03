package darks.grid.kernel;

public interface ICGService
{
	public void Start(int type) throws Exception;

	public void Close() throws Exception;

	public boolean isStart();

	public boolean isClose();
}
