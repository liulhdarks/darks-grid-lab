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

	// 用户请求的文件的url
	private String requestPath;
	// mltipart/form-data方式提交post的分隔符,
	private String boundary = null;
	// post提交请求的正文的长度
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
			System.out.println("网际请求(" + method + "): " + requestPath);
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

	// 处理GET请求
	private void doGet(DataInputStream reader, OutputStream os) throws Exception
	{
		String path = httpnet.getWebRoot() + requestPath;
		BufferedOutputStream out = new BufferedOutputStream(os);
		byte[] bytes = new byte[1024];
		if (new File(path).exists())
		{
			// 从服务器根目录下找到用户请求的文件并发送回浏览器
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

	// 处理post请求
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
			// 表明要上传附件， 跳转到doMultiPart方法。
			else if (line.indexOf("multipart/form-data") != -1)
			{
				// 得multiltipart的分隔符
				this.boundary = line.substring(line.indexOf("boundary") + 9);
				this.doMultiPart(reader, out);
				return;
			}
		}
		// 继续读取普通post（没有附件）提交的数据
		String dataLine = null;
		// 用户发送的post数据正文
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
		// 发送回浏览器的内容
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

	// 处理附件
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
				// 获取multipart分隔符
				this.boundary = line.substring(line.indexOf("boundary") + 9);
			}
		}
		System.out.println("begin get data......");

		/**
		 * 上面的注释是一个带附件的multipart类型的POST的全文模型， 要把附件去出来，就是要找到附件正文的起始位置和结束位置
		 * **/
		if (this.contentLength != 0)
		{
			// 把所有的提交的正文，包括附件和其他字段都先读到buf.
			byte[] buf = new byte[this.contentLength];
			int totalRead = 0;
			int size = 0;
			while (totalRead < this.contentLength)
			{
				size = reader.read(buf, totalRead, this.contentLength - totalRead);
				totalRead += size;
			}
			// 用buf构造一个字符串，可以用字符串方便的计算出附件所在的位置
			String dataString = new String(buf, 0, totalRead);
			System.out.println("the data user posted:\n" + dataString);
			int pos = dataString.indexOf(boundary);
			// 以下略过4行就是第一个附件的位置
			pos = dataString.indexOf("\n", pos) + 1;
			pos = dataString.indexOf("\n", pos) + 1;
			pos = dataString.indexOf("\n", pos) + 1;
			pos = dataString.indexOf("\n", pos) + 1;
			// 附件开始位置
			int start = dataString.substring(0, pos).getBytes().length;
			pos = dataString.indexOf(boundary, pos) - 4;
			// 附件结束位置
			int end = dataString.substring(0, pos).getBytes().length;
			// 以下找出filename
			int fileNameBegin = dataString.indexOf("filename") + 10;
			int fileNameEnd = dataString.indexOf("\n", fileNameBegin);
			String fileName = dataString.substring(fileNameBegin, fileNameEnd);
			/**
			 * 有时候上传的文件显示完整的文件名路径,比如c:\my file\somedir\project.doc
			 * 但有时候只显示文件的名字，比如myphoto.jpg. 所以需要做一个判断。
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
