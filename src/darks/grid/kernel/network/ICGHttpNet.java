package darks.grid.kernel.network;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

public interface ICGHttpNet
{
	public void init() throws Exception;

	public void init(int port) throws Exception;

	public ExecutorService getExecservice();

	public void close() throws Exception;

	public boolean isInited();

	public boolean isClosed();

	public String getWebRoot();

	public void setWebRoot(String webroot);

	public int getPort();

	public void setPort(int port);

	public String getAddress();

	public ServerSocket getServerSocket();

}
