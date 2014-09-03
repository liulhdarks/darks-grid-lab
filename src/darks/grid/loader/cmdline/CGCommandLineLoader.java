package darks.grid.loader.cmdline;

import java.util.concurrent.CountDownLatch;

import darks.grid.CloudGrid;

public final class CGCommandLineLoader
{
	/** Latch. */
	private static CountDownLatch latch;

	/**
	 * Enforces singleton.
	 */
	private CGCommandLineLoader()
	{
		// No-op.
	}

	/**
	 * Echos the given messages.
	 * 
	 * @param msg Message to echo.
	 */
	private static void echo(String msg)
	{
		assert msg != null;

		System.out.println(msg);
	}

	/**
	 * Exists with optional error message, usage show and exit code.
	 * 
	 * @param errMsg Optional error message.
	 * @param showUsage Whether or not to show usage information.
	 * @param exitCode Exit code.
	 */
	private static void exit(String errMsg, boolean showUsage, int exitCode)
	{
		if (errMsg != null)
		{
			echo("ERROR: " + errMsg);
		}

		// String runner = System.getProperty(GG_PROG_NAME_SYS_PROP,
		// "ggstart.{sh|bat}");
		//
		// int space = runner.indexOf(' ');
		//
		// runner = runner.substring(0, space == -1 ? runner.length() : space);
		//
		// if (showUsage) {
		// echo("Usage:");
		// echo("    " + runner + " <arg>");
		// echo("    Where <arg> is:");
		// echo("    ?, /help, -help  - show this message.");
		// echo("    path             - path to Spring XML configuration file.");
		// echo("                       Path can be absolute or relative to GRIDGAIN_HOME.");
		// echo("Spring file should contain one bean definition of Java type");
		// echo("org.gridgain.grid.GridConfiguration. Note that bean will be");
		// echo("fetched by the type and its ID is not used.");
		// }

		System.exit(exitCode);
	}

	/**
	 * Tests whether argument is help argument.
	 * 
	 * @param arg Command line argument.
	 * @return {@code true} if given argument is a help argument, {@code false}
	 *         otherwise.
	 */
	private static boolean isHelp(String arg)
	{
		String s;

		if (arg.startsWith("--"))
		{
			s = arg.substring(2);
		}
		else if (arg.startsWith("-") || arg.startsWith("/") || arg.startsWith("\\"))
		{
			s = arg.substring(1);
		}
		else
		{
			s = arg;
		}

		return "?".equals(s) || "help".equalsIgnoreCase(s) || "h".equalsIgnoreCase(s);
	}

	/**
	 * Main entry point.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args)
	{

		if (args.length > 1)
		{
			exit("Too many arguments.", true, -1);
		}

		if (isHelp(args[0]))
		{
			exit(null, true, 0);
		}

		try
		{
			if (args.length < 1)
			{
				CloudGrid.Start();
			}
			else
			{
				CloudGrid.Start(args[0]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exit("Failed to start CloudGrid: " + e.getMessage(), false, -1);
		}

		latch = new CountDownLatch(CloudGrid.allClouds().size());

		try
		{
			while (latch.getCount() > 0 && CloudGrid.isStart())
			{
				latch.await();
			}
		}
		catch (InterruptedException e)
		{
			echo("Loader was interrupted (exiting): " + e.getMessage());
		}

		System.exit(0);
	}
}
