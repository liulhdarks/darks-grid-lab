package darks.grid.kernel.network;

import java.io.IOException;
import java.net.BindException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

public interface ICGNet
{
	public void init() throws Exception;

	public void init(String host, int port) throws BindException, Exception;

	public void bind(String host, int port) throws BindException, Exception;

	public void response(String addr, String data);

	public void response(String addr, byte[] data);

	public void response(String host, int port, byte[] data);

	public void response(String host, int port, String data);

	public void response(String host, int port, Object obj);

	public byte[] request() throws Exception;

	public byte[] request(int timeout) throws InterruptedException, SocketTimeoutException,
			SocketException, IOException;

	public String getAddress();

	public void setAddress(String addr);

	public int getPort();

	public void setPort(int port);

	public void close();

	public boolean isInited();

	public boolean isClosed();

	public void addRecevieListener(ICGListener listener);

	public void removeRecevieListener(ICGListener listener);

	public List<ICGListener> getListenersList();

	public ICGListener getListener(int index);

	public void setTimeout(int timeOut);

	public int getSendBufferSize() throws Exception;
}
