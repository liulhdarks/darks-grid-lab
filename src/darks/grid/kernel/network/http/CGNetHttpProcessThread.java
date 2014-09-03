package darks.grid.kernel.network.http;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CGNetHttpProcessThread extends Thread
{

	private CGNetHttp httpnet;

	// �û�������ļ���url
	private String requestPath;
	// mltipart/form-data��ʽ�ύpost�ķָ���,
	private String boundary = null;
	// post�ύ��������ĵĳ���
	private int contentLength = 0;

	private Socket socket;

	public CGNetHttpProcessThread(CGNetHttp httpnet, Socket socket)
	{
		this.httpnet = httpnet;
		requestPath = null;
		this.socket = socket;
	}

	public void run()
	{
		try
		{
			DataInputStream reader = new DataInputStream((socket.getInputStream()));
			byte[] rq = new byte[256];
			reader.read(rq);
			String line = new String(rq);
			String method = line.substring(0, 4).trim();
			OutputStream out = socket.getOutputStream();
			this.requestPath = line.split(" ")[1];
			System.out.println("��������(" + method + "): " + requestPath);
			requestPath = java.net.URLDecoder.decode(requestPath, "utf-8");
			if ("GET".equalsIgnoreCase(method))
			{
				this.doGet(reader, out);
			}
			else if ("POST".equalsIgnoreCase(method))
			{
				this.doPost(reader, out);
			}
			socket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// ����GET����
	private void doGet(DataInputStream reader, OutputStream os) throws Exception
	{
		String path = httpnet.getWebRoot() + requestPath;
		BufferedOutputStream out = new BufferedOutputStream(os);
		byte[] bytes = new byte[1024];
		if (new File(path).exists())
		{
			// �ӷ�������Ŀ¼���ҵ��û�������ļ������ͻ������
			InputStream fileIn = new FileInputStream(path);
			byte[] buf = new byte[fileIn.available()];
			fileIn.read(buf);
			out.write(buf);
			out.close();
			// int ch = fileIn.read(bytes, 0, 1024);

			// while (ch != -1) {
			// out.write(bytes, 0, ch);
			// ch = fileIn.read(bytes, 0, 1024);
			// }
			fileIn.close();
			reader.close();
		}
	}

	// ����post����
	private void doPost(DataInputStream reader, OutputStream out) throws Exception
	{
		String line = reader.readLine();
		while (line != null)
		{
			System.out.println(line);
			line = reader.readLine();
			if ("".equals(line))
			{
				break;
			}
			else if (line.indexOf("Content-Length") != -1)
			{
				this.contentLength = Integer
						.parseInt(line.substring(line.indexOf("Content-Length") + 16));
			}
			// ����Ҫ�ϴ������� ��ת��doMultiPart������
			else if (line.indexOf("multipart/form-data") != -1)
			{
				// ��multiltipart�ķָ���
				this.boundary = line.substring(line.indexOf("boundary") + 9);
				this.doMultiPart(reader, out);
				return;
			}
		}
		// ������ȡ��ͨpost��û�и������ύ������
		String dataLine = null;
		// �û����͵�post��������
		byte[] buf = {};
		int size = 0;
		if (this.contentLength != 0)
		{
			buf = new byte[this.contentLength];
			while (size < this.contentLength)
			{
				int c = reader.read();
				buf[size++] = (byte) c;

			}
			System.out.println("The data user posted: " + new String(buf, 0, size));
		}
		// ���ͻ������������
		String response = "";
		response += "HTTP/1.1 200 OK\n";
		response += "Server: Sunpache 1.0\n";
		response += "Content-Type: text/html\n";
		response += "Last-Modified: Mon, 11 Jan 1998 13:23:42 GMT\n";
		response += "Accept-ranges: bytes";
		response += "\n";
		String body = "<html><head><title>test server</title></head><body><p>post ok:</p>"
				+ new String(buf, 0, size) + "</body></html>";
		System.out.println(body);
		out.write(response.getBytes());
		out.write(body.getBytes());
		out.flush();
		reader.close();
		out.close();
		System.out.println("request complete.");
	}

	// ������
	private void doMultiPart(DataInputStream reader, OutputStream out) throws Exception
	{
		System.out.println("doMultiPart ......");
		String line = reader.readLine();
		while (line != null)
		{
			System.out.println(line);
			line = reader.readLine();
			if ("".equals(line))
			{
				break;
			}
			else if (line.indexOf("Content-Length") != -1)
			{
				this.contentLength = Integer
						.parseInt(line.substring(line.indexOf("Content-Length") + 16));
				System.out.println("contentLength: " + this.contentLength);
			}
			else if (line.indexOf("boundary") != -1)
			{
				// ��ȡmultipart�ָ���
				this.boundary = line.substring(line.indexOf("boundary") + 9);
			}
		}
		System.out.println("begin get data......");

		/**
		 * �����ע����һ����������multipart���͵�POST��ȫ��ģ�ͣ� Ҫ�Ѹ���ȥ����������Ҫ�ҵ��������ĵ���ʼλ�úͽ���λ��
		 * **/
		if (this.contentLength != 0)
		{
			// �����е��ύ�����ģ����������������ֶζ��ȶ���buf.
			byte[] buf = new byte[this.contentLength];
			int totalRead = 0;
			int size = 0;
			while (totalRead < this.contentLength)
			{
				size = reader.read(buf, totalRead, this.contentLength - totalRead);
				totalRead += size;
			}
			// ��buf����һ���ַ������������ַ�������ļ�����������ڵ�λ��
			String dataString = new String(buf, 0, totalRead);
			System.out.println("the data user posted:\n" + dataString);
			int pos = dataString.indexOf(boundary);
			// �����Թ�4�о��ǵ�һ��������λ��
			pos = dataString.indexOf("\n", pos) + 1;
			pos = dataString.indexOf("\n", pos) + 1;
			pos = dataString.indexOf("\n", pos) + 1;
			pos = dataString.indexOf("\n", pos) + 1;
			// ������ʼλ��
			int start = dataString.substring(0, pos).getBytes().length;
			pos = dataString.indexOf(boundary, pos) - 4;
			// ��������λ��
			int end = dataString.substring(0, pos).getBytes().length;
			// �����ҳ�filename
			int fileNameBegin = dataString.indexOf("filename") + 10;
			int fileNameEnd = dataString.indexOf("\n", fileNameBegin);
			String fileName = dataString.substring(fileNameBegin, fileNameEnd);
			/**
			 * ��ʱ���ϴ����ļ���ʾ�������ļ���·��,����c:\my file\somedir\project.doc
			 * ����ʱ��ֻ��ʾ�ļ������֣�����myphoto.jpg. ������Ҫ��һ���жϡ�
			 */
			if (fileName.lastIndexOf("\\") != -1)
			{
				fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			}
			fileName = fileName.substring(0, fileName.length() - 2);
			OutputStream fileOut = new FileOutputStream("c:\\" + fileName);
			fileOut.write(buf, start, end - start);
			fileOut.close();
			fileOut.close();
		}
		String response = "";
		response += "HTTP/1.1 200 OK\n";
		response += "Server: Sunpache 1.0\n";
		response += "Content-Type: text/html\n";
		response += "Last-Modified: Mon, 11 Jan 1998 13:23:42 GMT\n";
		response += "Accept-ranges: bytes";
		response += "\n";
		out.write("<html><head><title>test server</title></head><body><p>Post is ok</p></body></html>"
				.getBytes());
		out.flush();
		reader.close();
		System.out.println("request complete.");
	}

}
