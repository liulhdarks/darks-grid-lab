package darks.grid.kernel.control;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentSkipListMap;

public class CGClassLoader extends ClassLoader
{
	// �����ϣ��Hashtable�����͵ı�����
	// ���ڱ��汻����������ݡ�
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

		// ���Ҫ������������Ƿ��Ѿ��������ڹ�ϣ���С�
		newClass = (Class) loadedClasses.get(className);
		// ����������Ѿ�������resolveֵΪtrue�����������
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
		// �������ϵͳ�࣬
		// ����ͼ��������ָ����URL��ַ�����ࡣ
		try
		{
			// ���Զ��巽�����������ݣ�
			// ������ֽ�����classData�С�
			classData = getClassData(linkPath);
			// ���ֽ����������������ݽ���һ��class���͵Ķ���
			newClass = defineClass(classData, 0, classData.length);
			if (newClass == null)
				throw new ClassNotFoundException(className);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ClassNotFoundException(className);
		}

		// ����౻��ȷ���룬
		// �������ݱ����ڹ�ϣ���У��Ա��ٴ�ʹ�á�
		loadedClasses.put(className, newClass);
		// ���resolveֵΪtrue������������ݡ�
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

	// ������������������������ݡ�
	protected byte[] getClassData(String className) throws IOException
	{
		byte[] data;
		int length;
		try
		{
			// �������в���URL��ķ���
			// ����ָ��URL��ַ��������ݡ�
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
