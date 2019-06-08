package scheduling;

public class Process {
	private int cpuBurst;
	private int arrTime;
	private int pid;
	private int alreadyExec;
	private int tFinish;
	private int tStart;
	
	public Process(int burst, int arr, int pid)
	{
		this.cpuBurst = burst;
		this.arrTime = arr;
		this.pid = pid;
	}
	
	public int getBurst()
	{
		return this.cpuBurst;
	}
	
	public int getTime()
	{
		return this.arrTime;
	}
	
	public int getPid()
	{
		return this.pid;
	}
	
	public void exec()
	{
		this.alreadyExec++;
	}
	
	public int remainingBurst()
	{
		return this.getBurst() - this.alreadyExec;
	}
	
	public int gettFinish() {
		return tFinish;
	}

	public void settFinish(int tFinish) {
		this.tFinish = tFinish;
	}

	public int gettStart() {
		return tStart;
	}

	public void settStart(int tStart) {
		this.tStart = tStart;
	}

	//toString()
	public String toString() {
		return "\nProcesso: " + this.pid + "\tTempo Burst: " + this.cpuBurst + "\tTempo Arrivo: " + this.arrTime + "\nTempo Start: "
				+ this.tStart + "\tTempo Fine: " + this.tFinish; 
	}
}