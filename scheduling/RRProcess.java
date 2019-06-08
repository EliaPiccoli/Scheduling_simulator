package scheduling;

public class RRProcess extends Process implements ScheduledProcess {

	private int exectuted;
	private static int lastQuantum;
	private int quantumW;

	public RRProcess(int b, int a, int pid) {
		super(b, a, pid);
	}
	
	public int compareTo(ScheduledProcess o) {
		int diff = this.getTime() - o.getTime();
		
		if(diff == 0)
			return this.getPid() - o.getPid();
		else return diff;
	}

	public void execOnce()
	{
		this.exectuted++;
	}
	
	public int exectuted() {
		return this.exectuted;
	}
	
	public static void setQ(int x)
	{
		RRProcess.lastQuantum = x;
	}
	
	public static int getQ()
	{
		return RRProcess.lastQuantum;
	}
	
	public void resetQ()
	{
		this.quantumW = 0;
	}
	
	public void waitQ()
	{
		this.quantumW++;
	}
	
	public int getQW()
	{
		return this.quantumW;
	}
}
