package scheduling;

public class HRRNProcess extends Process implements ScheduledProcess {
	private float priority;
	
	public HRRNProcess(int b, int t, int pid) {
		super(b, t, pid);
	}
	
	public int compareTo(ScheduledProcess o) {
		int diff = this.getTime() - o.getTime();
		
		if(diff == 0)
			return this.getPid() - o.getPid();
		else return diff;
	}
	
	public void setPriority(int t)
	{
		this.priority = 1 + ((float) (t - this.getTime())/this.getBurst());
	}
	
	public float getPriority()
	{
		return this.priority;
	}
}
