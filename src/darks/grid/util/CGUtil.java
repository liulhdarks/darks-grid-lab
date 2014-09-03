package darks.grid.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class CGUtil
{

	private static final int MASK = 0xf;

	private static InetAddress localHost;

	public static java.lang.Object ByteToObject(byte[] bytes)
	{
		java.lang.Object obj = null;
		try
		{
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();

			bi.close();
			oi.close();
		}
		catch (Exception e)
		{
			System.out.println("translation:" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	public static byte[] ObjectToByte(java.lang.Object obj)
	{
		byte[] bytes = null;
		try
		{
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		}
		catch (Exception e)
		{
			System.out.println("translation:" + e.getMessage());
			e.printStackTrace();
		}
		return (bytes);
	}

	public static String getUUID()
	{
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23)
				+ s.substring(24);
	}

	public static String[] getUUID(int number)
	{
		if (number < 1)
		{
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++)
		{
			ss[i] = getUUID();
		}
		return ss;
	}

	public static Object[] readArray(ObjectInput in) throws IOException, ClassNotFoundException
	{
		int len = in.readInt();

		Object[] arr = null;

		if (len > 0)
		{
			arr = new Object[len];

			for (int i = 0; i < len; i++)
			{
				arr[i] = in.readObject();
			}
		}

		return arr;
	}

	public static <T> void writeArray(ObjectOutput out, T[] arr) throws IOException
	{
		int len = arr == null ? 0 : arr.length;

		out.writeInt(len);

		if (arr != null && arr.length > 0)
		{
			for (T t : arr)
			{
				out.writeObject(t);
			}
		}
	}

	public static byte[] CollectionToByte(Collection<?> col)
	{
		byte[] bytes = null;
		try
		{
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);

			writeCollection(oo, col);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		}
		catch (Exception e)
		{
			System.out.println("translation:" + e.getMessage());
			e.printStackTrace();
		}
		return (bytes);
	}

	public static <E> Collection<E> ByteToCollection(byte[] bytes)
	{
		Collection<E> obj = null;
		try
		{
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = readCollection(oi);

			bi.close();
			oi.close();
		}
		catch (Exception e)
		{
			System.out.println("translation:" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	public static void writeCollection(ObjectOutput out, Collection<?> col) throws IOException
	{
		// Write null flag.
		out.writeBoolean(col == null);

		if (col != null)
		{
			out.writeInt(col.size());

			for (Object o : col)
			{
				out.writeObject(o);
			}
		}
	}

	public static <E> Collection<E> readCollection(ObjectInput in) throws IOException,
			ClassNotFoundException
	{
		List<E> col = null;

		// Check null flag.
		if (!in.readBoolean())
		{
			int size = in.readInt();

			col = new ArrayList<E>(size);

			for (int i = 0; i < size; i++)
			{
				col.add((E) in.readObject());
			}
		}

		return col;
	}

	public static <K, V> Map<K, V> copyMap(Map<K, V> m)
	{
		return new HashMap<K, V>(m);
	}

	public static String byteArray2HexString(byte[] arr)
	{
		StringBuilder buf = new StringBuilder(arr.length << 1);

		for (byte b : arr)
		{
			buf.append(Integer.toHexString(MASK & b >>> 4)).append(Integer.toHexString(MASK & b));
		}

		return buf.toString().toUpperCase();
	}

	public static byte[] hexString2ByteArray(String hex) throws IllegalArgumentException
	{
		// If Hex string has odd character length.
		if (hex.length() % 2 != 0)
		{
			hex = '0' + hex;
		}

		char[] chars = hex.toCharArray();

		byte[] bytes = new byte[chars.length / 2];

		int byteCount = 0;

		for (int i = 0; i < chars.length; i += 2)
		{
			int newByte = 0;

			newByte |= hexCharToByte(chars[i]);

			newByte <<= 4;

			newByte |= hexCharToByte(chars[i + 1]);

			bytes[byteCount] = (byte) newByte;

			byteCount++;
		}

		return bytes;
	}

	@SuppressWarnings({ "UnnecessaryFullyQualifiedName" })
	private static byte hexCharToByte(char ch) throws IllegalArgumentException
	{
		switch (ch)
		{
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		{
			return (byte) (ch - '0');
		}

		case 'a':
		case 'A':
		{
			return 0xa;
		}

		case 'b':
		case 'B':
		{
			return 0xb;
		}

		case 'c':
		case 'C':
		{
			return 0xc;
		}

		case 'd':
		case 'D':
		{
			return 0xd;
		}

		case 'e':
		case 'E':
		{
			return 0xe;
		}

		case 'f':
		case 'F':
		{
			return 0xf;
		}

		default:
		{
			throw new IllegalArgumentException("Hex decoding wrong input character [character="
					+ ch + ']');
		}
		}
	}

	public static void writeMap(ObjectOutput out, Map<?, ?> map) throws IOException
	{
		// Write null flag.
		out.writeBoolean(map == null);

		if (map != null)
		{
			out.writeInt(map.size());

			for (Map.Entry<?, ?> e : map.entrySet())
			{
				out.writeObject(e.getKey());
				out.writeObject(e.getValue());
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public static <K, V> Map<K, V> readMap(ObjectInput in) throws IOException,
			ClassNotFoundException
	{
		Map<K, V> map = null;

		// Check null flag.
		if (!in.readBoolean())
		{
			int size = in.readInt();

			map = new HashMap<K, V>(size, 1.0f);

			for (int i = 0; i < size; i++)
			{
				map.put((K) in.readObject(), (V) in.readObject());
			}
		}

		return map;
	}

	public static void writeObject(ObjectOutput out, Object obj) throws IOException
	{
		writeByteArray(out, ObjectToByte(obj));
	}

	public static Object readObject(ObjectInput in) throws IOException
	{
		return ByteToObject(readByteArray(in));
	}

	public static void writeByteArray(ObjectOutput out, byte[] arr) throws IOException
	{
		if (arr == null)
		{
			out.writeInt(-1);
		}
		else
		{
			out.writeInt(arr.length);

			out.write(arr);
		}
	}

	public static byte[] readByteArray(ObjectInput in) throws IOException
	{
		int len = in.readInt();

		if (len == -1)
		{
			return null; // Value "-1" indicates null.
		}

		byte[] res = new byte[len];

		in.readFully(res);

		return res;
	}

	public static boolean join(Thread t)
	{
		if (t != null)
		{
			try
			{
				t.join();

				return true;
			}
			catch (InterruptedException e)
			{
				System.out.println("Got interrupted while waiting for completion of a thread: " + t
						+ " " + e.toString());

				return false;
			}
		}

		return true;
	}

	public static boolean joinThreads(Iterable<? extends Thread> workers)
	{
		boolean retval = true;

		if (workers != null)
		{
			for (Thread worker : workers)
			{
				if (!join(worker))
				{
					retval = false;
				}
			}
		}

		return retval;
	}

	public static <V> ArrayList<V>[] splitArrayList(ArrayList<V> src, int limit)
	{
		int num = src.size() % limit == 0 ? (src.size() / limit) : (src.size() / limit + 1);
		ArrayList<V>[] list = new ArrayList[num];
		int index = 0;
		for (int i = 0; i < num; i++)
		{
			int size = (i + 1) * limit - 1;
			int len = src.size() < size ? src.size() : size;
			ArrayList<V> tmp = new ArrayList<V>(len);
			for (int j = i * limit; j < len; j++)
			{
				V obj = src.get(i);
				tmp.add(obj);
			}
			list[index++] = tmp;
		}
		return list;
	}

	public static <T> T newInstance(Class<T> cls) throws Exception
	{
		boolean set = false;

		Constructor<T> ctor = null;

		try
		{
			ctor = cls.getDeclaredConstructor();

			if (ctor == null)
			{
				return null;
			}

			if (!ctor.isAccessible())
			{
				ctor.setAccessible(true);

				set = true;
			}

			return ctor.newInstance();
		}
		catch (NoSuchMethodException e)
		{
			throw new Exception("Failed to find empty constructor for class: " + cls, e);
		}
		catch (InstantiationException e)
		{
			throw new Exception("Failed to create new instance for class: " + cls, e);
		}
		catch (IllegalAccessException e)
		{
			throw new Exception("Failed to create new instance for class: " + cls, e);
		}
		catch (InvocationTargetException e)
		{
			throw new Exception("Failed to create new instance for class: " + cls, e);
		}
		finally
		{
			if (ctor != null && set)
			{
				ctor.setAccessible(false);
			}
		}
	}

	public static String getTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar MyDate = Calendar.getInstance();
		MyDate.setTime(new java.util.Date());
		String ndate = df.format(MyDate.getTime());
		return ndate;
	}

	public static String getTime(String style)
	{
		SimpleDateFormat df = new SimpleDateFormat(style);
		Calendar MyDate = Calendar.getInstance();
		MyDate.setTime(new java.util.Date());
		String ndate = df.format(MyDate.getTime());
		return ndate;
	}

	public static boolean buildDirectory(String rootDirPath, String dirPath)
	{
		if (rootDirPath.equals("") || dirPath.equals(""))
			return false;
		if (!rootDirPath.endsWith("/"))
			rootDirPath += "/";
		File tmp = new File(dirPath);
		String ppath = tmp.getParent();
		if (!ppath.endsWith("/"))
			ppath += "/";
		if (rootDirPath.equals(ppath))
			return true;
		if (!tmp.exists())
		{
			buildDirectory(rootDirPath, ppath);
			System.out.println(dirPath);
			tmp.mkdir();
		}
		return true;
	}

	public static String getCurrentPath()
	{
		File f = new File("");
		String path = f.getAbsolutePath();
		return path;
	}

	public static void shutdownNow(ExecutorService exec)
	{
		if (exec != null)
		{
			List<Runnable> tasks = exec.shutdownNow();

			if (tasks.size() == 0)
			{
				System.out.println("Runnable tasks outlived thread pool executor service ["
						+ ", tasks=" + tasks + ']');
			}

			try
			{
				exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				System.out.println("Got interrupted while waiting for executor service to stop.["
						+ e.toString() + "]");
			}
		}
	}

	public static synchronized InetAddress getLocalHost() throws IOException
	{
		if (localHost == null)
		{
			// Cache it.
			localHost = resetLocalHost();
		}

		return localHost;
	}

	/**
	 * @return Local host.
	 * @throws IOException If attempt to get local host failed.
	 */
	private static InetAddress resetLocalHost() throws IOException
	{
		localHost = InetAddress.getLocalHost();

		// It should not take longer than 2 seconds to reach
		// local address on any network.
		int reachTimeout = 2000;

		if (localHost.isLoopbackAddress() || !localHost.isReachable(reachTimeout))
		{
			for (NetworkInterface itf : asIterable(NetworkInterface.getNetworkInterfaces()))
			{
				for (InetAddress addr : asIterable(itf.getInetAddresses()))
				{
					if (!addr.isLoopbackAddress() && !addr.isLinkLocalAddress()
							&& addr.isReachable(reachTimeout))
					{
						localHost = addr;

						break;
					}
				}
			}
		}

		return localHost;
	}

	public static <T> Iterable<T> asIterable(final Enumeration<T> e)
	{
		if (e == null)
			return null;
		return new Iterable<T>()
		{
			@Override
			public Iterator<T> iterator()
			{
				return new Iterator<T>()
				{
					@Override
					public boolean hasNext()
					{
						return e.hasMoreElements();
					}

					@SuppressWarnings({ "IteratorNextCanNotThrowNoSuchElementException" })
					@Override
					public T next()
					{
						return e.nextElement();
					}

					@Override
					public void remove()
					{
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public static byte[] apartByteArray(byte[] data, int start, int size)
	{
		byte[] bytes = new byte[size];
		// System.out.println(start+" "+size);
		int index = 0;
		// for(int i=start;i<start+size;i++){
		// bytes[index++]=data[i];
		// }
		System.arraycopy(data, start, bytes, 0, size);
		return bytes;
	}

	public static List<CGBytesPackage> apartByteArray(byte[] data, int size)
	{
		int num = data.length % size == 0 ? data.length / size : data.length / size + 1;
		List<CGBytesPackage> list = new ArrayList<CGBytesPackage>();
		for (int i = 0; i < num; i++)
		{
			int len = i * size;
			if (data.length - len < size)
				size = data.length - len;
			byte[] bytes = apartByteArray(data, len, size);
			list.add(new CGBytesPackage(len, size, bytes, data.length));
		}
		return list;
	}

	public static byte[] makeupByteArray(List<CGBytesPackage> datas)
	{
		if (datas.size() <= 0)
			return null;
		int size = datas.get(0).getSrclength();
		byte[] bytes = new byte[size];
		int offset = 0;
		for (CGBytesPackage d : datas)
		{
			// System.out.println(d.getStart()+" "+d.getSize());
			byte[] src = d.getData();
			// int index=0;
			// for(int i=d.getStart();i<d.getStart()+d.getSize();i++){
			// bytes[i]=d.getData(index++);
			// }
			System.arraycopy(src, 0, bytes, d.getStart(), d.getSize());
		}
		return bytes;
	}

	public static String NumberLine(List<String> list)
	{
		String msg = "";
		for (int i = 0; i < list.size(); i++)
		{
			int t = Integer.parseInt(list.get(i));
			int e = Integer.parseInt(list.get(i));
			int off = 1;
			for (int j = i + 1; j < list.size(); j++)
			{

				if (t + off != Integer.parseInt(list.get(j)))
				{
					i = j - 1;
					break;
				}
				else
				{
					i = j;
				}
				e = Integer.parseInt(list.get(j));
				off++;
			}
			msg += "_" + t + "-" + e;
		}
		return msg;
	}

	public static void main(String[] args) throws Exception
	{

		// String s="中国人吧";
		// System.out.println(s.getBytes().length);
		// List<CGBytesPackage>
		// list=apartByteArray(s.getBytes(),s.getBytes().length/5);
		// byte[] bt=makeupByteArray(list);
		// System.out.println(new String(bt));

	}
}
