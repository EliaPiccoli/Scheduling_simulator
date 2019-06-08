package scheduling;

public interface ScheduledProcess extends Comparable<ScheduledProcess> {
	public int getBurst();
	public int getTime();
	public int getPid();
	public void exec();
	public int remainingBurst();
}
