package scheduling;

import java.util.Comparator;

public class SJFComparator implements Comparator<Process> {

	public int compare(Process p, Process q) {
		int diff = p.remainingBurst() - q.remainingBurst();
		
		if(diff == 0)
			return p.getPid() - q.getPid();
		else return diff;
	}
}
