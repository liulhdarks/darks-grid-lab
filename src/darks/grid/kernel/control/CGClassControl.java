package darks.grid.kernel.control;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentSkipListMap;

import darks.grid.kernel.task.CGTask;
import darks.grid.util.CGUtil;

public class CGClassControl
{

	// private static CGClassLoader loader=new CGClassLoader();
	private static ConcurrentSkipListMap<String, CGClassLoader> loadermap = new ConcurrentSkipListMap<String, CGClassLoader>();

	public static Class loadClass(String clsName)
	{
		try
		{
			if (clsName == null)
				return null;
			return Class.forName(clsName);
		}
		catch (Exception e)
		{
			System.out.println("CGClassControl.loadClass:" + e.toString());
		}
		return null;
	}

	public static <T, R> CGTask<T, R> newTask(Class<? extends CGTask<T, R>> taskCls)
	{
		try
		{
			if (taskCls == null)
				return null;
			return taskCls.newInstance();
		}
		catch (Exception e)
		{
			System.out.println("CGClassControl.newTask:" + e.toString());
		}
		return null;
	}

	public static Method getClassMethod(Class cls, String methodName, Class... clss)
	{
		try
		{
			if (cls == null)
				return null;
			// return cls.getMethod(methodName, clss);
			return cls.getDeclaredMethod(methodName, clss);
		}
		catch (Exception e)
		{
			System.out.println("CGClassControl.getClassMethod:" + e.toString());
		}
		return null;
	}

	public static Method getClassMethodB(Class cls, String methodName, Class paramType[])
	{
		try
		{
			if (cls == null)
				return null;
			return cls.getMethod(methodName, paramType);
		}
		catch (Exception e)
		{
			System.out.println("CGClassControl.getClassMethod:" + e.toString());
		}
		return null;
	}

	public static Object invokeClass(String clsName, Method method, Object... params)
	{
		return invokeClass(loadClass(clsName), method, params);
	}

	public static Object invokeClass(Class cls, Method method, Object... params)
	{

		try
		{
			if (cls != null)
			{
				return method.invoke(CGUtil.newInstance(cls), params);
			}
			else
			{
				return method.invoke(null, params);
			}
		}
		catch (Exception e)
		{
			System.out.println("CGClassControl.invokeClass:" + e.toString());
		}
		return null;
	}

	public static Object invokeClass(Class cls, String methodName, boolean instance,
			Class paramType[], Object... params)
	{
		Method method = getClassMethodB(cls, methodName, paramType);
		try
		{
			if (instance)
			{
				return method.invoke(CGUtil.newInstance(cls), params);
			}
			else
			{
				return method.invoke(null, params);
			}
		}
		catch (Exception e)
		{
			System.out.println("CGClassControl.invokeClass:" + e.toString());
		}
		return null;
	}

	public static CGClassLoader getClassLoader(String className)
	{
		return getClassLoader(className, true);
	}

	public static CGClassLoader getClassLoader(String className, boolean isupdate)
	{
		if (loadermap.size() == 0)
		{
			return addClassLoader(className);
		}
		if (!isupdate)
		{
			if (loadermap.containsKey(className))
			{
				return loadermap.get(className);
			}
			else
			{
				return addClassLoader(className);
			}
		}
		else
		{
			if (loadermap.containsKey(className))
			{
				CGClassLoader tmp = addClassLoader(className);
				loadermap.put(className, tmp);
				return tmp;
			}
			else
			{
				return addClassLoader(className);
			}
		}
	}

	public static CGClassLoader addClassLoader(String className)
	{
		CGClassLoader loader = new CGClassLoader();
		loadermap.put(className, loader);
		return loader;
	}
}
