package darks.grid.kernel.network;

import java.util.LinkedList;
import java.util.List;

public abstract class ACGNet implements ICGNet
{

	private List<ICGListener> list = new LinkedList<ICGListener>();

	@Override
	public void addRecevieListener(ICGListener listener)
	{
		list.add(listener);
	}

	@Override
	public void removeRecevieListener(ICGListener listener)
	{
		list.remove(listener);
	}

	@Override
	public List<ICGListener> getListenersList()
	{
		return list;
	}

	@Override
	public ICGListener getListener(int index)
	{
		return list.get(index);
	}
}
