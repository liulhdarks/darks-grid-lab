package darks.grid.kernel.control;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentSkipListMap;

public class CGClassLoader extends ClassLoader
{
	// 定义哈希表（Hashtable）类型的变量，
	// 用于保存被载入的类数据。
	ConcurrentSkipListMap<String, Class> loadedClasses;

	public CGClassLoader()
	{
		loadedClasses = new ConcurrentSkipListMap<String, Class>();
	}

	public synchronized Class loadClass(String className, boolean resolve)
			throws ClassNotFoundException
	{
		return loadClass(className, className, resolve);
	}

	public synchronized Class loadClass(String className, String linkPath, boolean resolve)
			throws ClassNotFoundException
	{
		Class newClass;
		byte[] classData;

		// 检查要载入的类数据是否已经被保存在哈希表中。
		newClass = (Class) loadedClasses.get(className);
		// 如果类数据已经存在且resolve值为true，则解析它。
		if (newClass != null)
		{
			if (resolve)
				resolveClass(newClass);
			return newClass;
		}

		try
		{
			newClass = findSystemClass(className);
			if (newClass != null)
				return newClass;
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(className + " is not a system class!");
		}

		try
		{
			newClass = findLoadedClass(className);
			if (newClass != null)
				return newClass;
		}
		catch (Exception e)
		{
			System.out.println(className + " is not a loaded class!");
		}

		try
		{
			newClass = Class.forName(className);
			if (newClass != null)
				return newClass;
		}
		catch (Exception e)
		{
			System.out.println(className + " is not a forName class!");
		}
		// 如果不是系统类，
		// 则试图从网络中指定的URL地址载入类。
		try
		{
			// 用自定义方法载入类数据，
			// 存放于字节数组classData中。
			classData = getClassData(linkPath);
			// 由字节数组所包含的数据建立一个class类型的对象。
			newClass = defineClass(classData, 0, classData.length);
			if (newClass == null)
				throw new ClassNotFoundException(className);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ClassNotFoundException(className);
		}

		// 如果类被正确载入，
		// 则将类数据保存在哈希表中，以备再次使用。
		loadedClasses.put(className, newClass);
		// 如果resolve值为true，则解析类数据。
		if (resolve)
		{
			resolveClass(newClass);
		}
		return newClass;
	}

	public synchronized boolean containClass(String className)
	{
		return loadedClasses.containsKey(className);
	}

	// 这个方法从网络中载入类数据。
	protected byte[] getClassData(String className) throws IOException
	{
		byte[] data;
		int length;
		try
		{
			// 从网络中采用URL类的方法
			// 载入指定URL地址的类的数据。
			URL url = new URL(className.endsWith(".class") ? className : className + ".class");
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			InputStream inputStream = connection.getInputStream();
			BufferedInputStream in = new BufferedInputStream(inputStream);

			length = connection.getContentLength();
			data = new byte[in.available()];
			in.read(data);
			in.close();

			return data;
		}
		catch (Exception e)
		{
			throw new IOException(className);
		}
	}
}
