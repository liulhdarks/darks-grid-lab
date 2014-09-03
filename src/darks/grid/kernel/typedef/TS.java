package darks.grid.kernel.typedef;

import darks.grid.kernel.store.CGThreadStore;

public class TS
{
	public static final int EXEC_THREAD = 1001;
	public static final int EXEC_QUEUE_THREAD = 1002;
	public static final int EXEC_QUEUE_PROC_THREAD = 1003;
	public static final int CHK_THREAD = 1004;
	public static final int EXEC_RET_THREAD = 1005;
	public static final int INFO_THREAD = 1006;
	public static final int INFO_QUEUE_THREAD = 1007;
	public static final int TASK_THREAD = 1008;

	public static CGThreadStore getInstance()
	{
		return CGThreadStore.getInstance();
	}
}
