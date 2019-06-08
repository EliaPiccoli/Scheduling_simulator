package scheduling;

import java.util.Comparator;

public class HRRNComparator implements Comparator<Process> {
	public int compare(Process p, Process q) {
		float diff = ((HRRNProcess) q).getPriority() - ((HRRNProcess) p).getPriority();
		
		if(diff == 0)
		{
			return p.getPid() - q.getPid();
		}
		else return (int) ((diff > 0) ? Math.ceil(diff) : -1*Math.ceil(Math.abs(diff)));
	}
}
