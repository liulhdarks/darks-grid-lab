package darks.grid.util;

import java.io.Serializable;

public class CGBytesPackage implements Serializable
{

	private static final long serialVersionUID = 7519754628581887375L;

	public CGBytesPackage(int start, int size, byte[] data, int length)
	{ // size 分包大小 length 原包大小
		this.start = start;
		this.size = size;
		this.data = data;
		this.srclength = length;
	}

	private int start;
	private int size;
	private byte[] data;
	private int srclength;

	public byte getData(int index)
	{
		return data[index];
	}

	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public int getSrclength()
	{
		return srclength;
	}

	public void setSrclength(int srclength)
	{
		this.srclength = srclength;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

}
