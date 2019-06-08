package scheduling;

import java.util.Comparator;

public class RRComparator implements Comparator<Process> {

	public int compare(Process p, Process q) {
		int diff = ((RRProcess) q).getQW() - ((RRProcess) p).getQW();
		if(diff==0)
		{
			diff = ((RRProcess) p).exectuted() - ((RRProcess) q).exectuted();
			if(diff == 0)
			{
				diff = p.getTime() - q.getTime();
				if(diff == 0)
					return p.getPid() - q.getPid();
			}
		}
		return diff;
	}
}
