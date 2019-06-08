package scheduling;

public class FCFSProcess extends Process implements ScheduledProcess {
	
	public FCFSProcess(int b, int t, int pid) {
		super(b, t, pid);
	}
	
	public int compareTo(ScheduledProcess other) {
		int diff = this.getTime() - ((FCFSProcess) other).getTime();
		
		if(diff == 0)
			return this.getPid() - ((FCFSProcess) other).getPid();
		else return diff;
	}
}
