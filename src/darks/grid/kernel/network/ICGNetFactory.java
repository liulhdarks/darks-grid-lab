package darks.grid.kernel.network;

public interface ICGNetFactory
{
	public static final int NET_TYPE_UDP = 1;
	public static final int NET_TYPE_TCP = 2;
	public static final int NET_TYPE_HTTP = 3;

	public ACGNet createCGNet(int type);

	public ICGHttpNet createCGHttpNet();
}
