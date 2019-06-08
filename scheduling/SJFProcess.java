package scheduling;

public class SJFProcess extends Process implements ScheduledProcess {
	
	public SJFProcess(int b, int t, int pid) {
		super(b, t, pid);
	}
	
	public int compareTo(ScheduledProcess other) {
		int diff = this.getTime() - ((SJFProcess) other).getTime();
		
		if(diff == 0)
		{
			diff = this.getBurst() - ((SJFProcess) other).getBurst();
			if(diff == 0)
				return this.getPid() - ((SJFProcess) other).getPid();
		}
		
		return diff;
	}
}