package darks.grid;

import java.io.Serializable;

public interface ICGLinkTask extends Serializable
{
	// public Object execute(Object[] objs);
	public Object execute(Object objs) throws Exception;
}
