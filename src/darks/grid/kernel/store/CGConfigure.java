package darks.grid.kernel.store;

import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CGConfigure
{

	private String cfgfile = "/cloudgrid.xml";

	public CGConfigure()
	{

	}

	public CGConfigure(String cfgfile)
	{
		this.cfgfile = cfgfile;
	}

	public boolean loadConfig() throws Exception
	{
		CGPrintCenter.printObject("开始加载配置文件...");
		InputStream ins = this.getClass().getResourceAsStream(cfgfile);
		if (ins == null)
			throw new Exception("找不到配置文件cloudgrid.xml");
		SAXReader reader = new SAXReader();
		Document doc = reader.read(ins);
		Element root = doc.getRootElement();
		CGDataStore.httpRoot = root.elementText("workroot");
		CGDataStore.srcRoot = root.elementText("srcroot");
		CGDataStore.classRoot = root.elementText("classroot");
		for (Iterator<Element> it = root.elementIterator(); it.hasNext();)
		{
			Element el = it.next();
			// System.out.println(el.getName());
			if ("connector".equals(el.getName().trim()))
			{
				procConnector(el);
			}
			else if ("initialize".equals(el.getName().trim()))
			{
				procInitialize(el);
			}
			else if ("service".equals(el.getName().trim()))
			{
				procService(el);
			}
		}
		return true;
	}

	private void procConnector(Element el)
	{
		for (Iterator<Attribute> it = el.attributeIterator(); it.hasNext();)
		{
			Attribute at = it.next();
			String name = at.getName().trim();
			String value = at.getValue().trim();
			if ("host".equals(name))
			{
				if ("localhost".equals(value))
					value = "127.0.0.1";
				CGDataStore.connectInfoHost = value;
			}
			else if ("infoport".equals(name))
			{
				CGDataStore.connectInfoPort = Integer.parseInt(value);
			}
			else if ("objport".equals(name))
			{
				CGDataStore.connectObjPort = Integer.parseInt(value);
			}
			else if ("rltport".equals(name))
			{
				CGDataStore.connectObjRltPort = Integer.parseInt(value);
			}
			else if ("httpport".equals(name))
			{
				CGDataStore.connectHttpPort = Integer.parseInt(value);
			}
		}

	}

	private void procInitialize(Element el)
	{
		for (Iterator<Attribute> it = el.attributeIterator(); it.hasNext();)
		{
			Attribute at = it.next();
			String name = at.getName().trim();
			String value = at.getValue().trim();
			// System.out.println(name);
			if ("host".equals(name))
			{
				if ("localhost".equals(value))
					value = "127.0.0.1";
				CGDataStore.initInfoHost = value;
			}
			else if ("infoport".equals(name))
			{
				CGDataStore.initInfoPort = Integer.parseInt(value);
			}
			else if ("objport".equals(name))
			{
				CGDataStore.initObjPort = Integer.parseInt(value);
			}
			else if ("rltport".equals(name))
			{
				CGDataStore.initObjRltPort = Integer.parseInt(value);
			}
			else if ("httpport".equals(name))
			{
				CGDataStore.initHttpPort = Integer.parseInt(value);
			}
		}
		for (Iterator<Element> it = el.elementIterator(); it.hasNext();)
		{
			Element elc = it.next();
			if ("maxport".equals(elc.getName().trim()))
			{
				CGDataStore.PORT_MAX_OFFSET = Integer.parseInt(elc.getText().trim());
			}
		}
	}

	private void procService(Element el)
	{
		for (Iterator<Element> it = el.elementIterator(); it.hasNext();)
		{
			Element elc = it.next();
			String name = elc.getName().trim();
			String value = elc.getText().trim();
			// System.out.println(name);
			if ("check".equals(name))
			{
				procCheck(el);
			}
			else if ("info".equals(name))
			{
				procInfo(el);
			}
			else if ("executor".equals(name))
			{
				procExec(el);
			}
			else if ("http".equals(name))
			{
				procHttp(el);
			}
			else if ("buffer".equals(name))
			{
				procBuffer(el);
			}
			else if ("data_package_size".equals(name))
			{
				CGDataStore.PACKAGE_SIZE = Integer.parseInt(value);
			}
		}
	}

	private void procCheck(Element el)
	{
		for (Iterator<Attribute> it = el.attributeIterator(); it.hasNext();)
		{
			Attribute at = it.next();
			String name = at.getName().trim();
			String value = at.getValue().trim();
			if ("maxnum".equals(name))
			{
				CGDataStore.CHK_THREAD_MAXNUM = Integer.parseInt(value);
			}
		}
	}

	private void procInfo(Element el)
	{
		for (Iterator<Attribute> it = el.attributeIterator(); it.hasNext();)
		{
			Attribute at = it.next();
			String name = at.getName().trim();
			String value = at.getValue().trim();
			if ("maxnum".equals(name))
			{
				CGDataStore.INFO_THREAD_MAXNUM = Integer.parseInt(value);
			}
			else if ("queue_num".equals(name))
			{
				CGDataStore.INFO_QUEUE_THREAD_MAXNUM = Integer.parseInt(value);
			}
		}
	}

	private void procExec(Element el)
	{
		for (Iterator<Attribute> it = el.attributeIterator(); it.hasNext();)
		{
			Attribute at = it.next();
			String name = at.getName().trim();
			String value = at.getValue().trim();
			if ("maxnum".equals(name))
			{
				CGDataStore.EXEC_THREAD_MAXNUM = Integer.parseInt(value);
			}
			else if ("queue_num".equals(name))
			{
				CGDataStore.EXEC_QUEUE_THREAD_MAXNUM = Integer.parseInt(value);
			}
			else if ("queue_proc_num".equals(name))
			{
				CGDataStore.EXEC_QUEUE_PROC_THREAD_MAXNUM = Integer.parseInt(value);
			}
			else if ("result_num".equals(name))
			{
				CGDataStore.EXEC_RET_THREAD_MAXNUM = Integer.parseInt(value);
			}
			else if ("result_queue_num".equals(name))
			{
				CGDataStore.EXEC_RET_QUEUE_THREAD_MAXNUM = Integer.parseInt(value);
			}
		}
	}

	private void procHttp(Element el)
	{
		for (Iterator<Attribute> it = el.attributeIterator(); it.hasNext();)
		{
			Attribute at = it.next();
			String name = at.getName().trim();
			String value = at.getValue().trim();
			if ("maxnum".equals(name))
			{
				CGDataStore.HTTP_THREAD_MAXNUM = Integer.parseInt(value);
			}
		}
	}

	private void procBuffer(Element el)
	{
		for (Iterator<Attribute> it = el.attributeIterator(); it.hasNext();)
		{
			Attribute at = it.next();
			String name = at.getName().trim();
			String value = at.getValue().trim();
			if ("exec_num".equals(name))
			{
				CGDataStore.EXEC_BUFFER_MAXNUM = Integer.parseInt(value);
			}
			else if ("proc_num".equals(name))
			{
				CGDataStore.EXEC_PROC_BUFFER_MAXNUM = Integer.parseInt(value);
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		CGConfigure c = new CGConfigure();
		c.loadConfig();
	}
}
