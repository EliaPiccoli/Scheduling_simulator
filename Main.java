import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import scheduling.FCFSProcess;
import scheduling.HRRNProcess;
import scheduling.Process;
import scheduling.RRProcess;
import scheduling.SJFProcess;
import scheduling.Scheduling;

public class Main{
	public static void main(String[] args) {
		try (Scanner k = new Scanner(System.in)) {
			int nProc;
			Process[] proc;
			int nrr;
			int[] rrquantum;
			//Inserisco informazioni riguardanti la tabella dei processi
			do
			{
				System.out.println("Inserire il numero di processi di cui si vuole eseguire lo Scheduling:\t");
				nProc = k.nextInt();
			} while(nProc <= 0);
			proc = new Process[nProc];
			for(int i=0; i<nProc; i++)
			{
				System.out.println("Processo " + (i+1));
				proc[i] = new Process(Scheduling.insBurst(k), Scheduling.insTime(k), i+1);
			}
			//Inserisco informazioni riguardanti RR
			do
			{
				System.out.println("Quante diverse istanze di Round Robin si desidera visualizzare?");
				nrr = k.nextInt();
			} while(nrr < 1);

			rrquantum = new int[nrr];
			for(int i=0; i<nrr; i++)
			{
				do
				{
					System.out.println("Inserire quanto dell'algoritmo RoundRobin numero " + (i+1));
					rrquantum[i] = k.nextInt();
				} while(rrquantum[i] < 1);
			}			
	
			Scheduling s = new Scheduling(nProc, nrr, proc);
			
			//Creo le diverse code che poi vado a riempire con i processi contenuti nell'array
			Queue<Process> fcfs = new PriorityQueue<Process>();
			Queue<Process> sjf = new PriorityQueue<Process>();
			Queue<Process> srtf = new PriorityQueue<Process>();
			Queue<Process> rr = new PriorityQueue<Process>();
			Queue<Process> hrrn = new PriorityQueue<Process>();
			
			int burst, time, pid;
			for(int i=0; i<nProc; i++)
			{
				burst = proc[i].getBurst();
				time = proc[i].getTime();
				pid = proc[i].getPid();
				
				fcfs.add(new FCFSProcess(burst, time, pid));
				sjf.add(new SJFProcess(burst, time, pid));
				srtf.add(new SJFProcess(burst, time, pid));
				rr.add(new RRProcess(burst, time, pid));
				hrrn.add(new HRRNProcess(burst, time, pid));
			}
			
			int totalexec = Scheduling.totalExec(proc);
			int[] execution = new int[totalexec];
			Process p;
			int counter = 0;
			
			//FCFS
			while ((p = fcfs.poll()) != null)
			{
				p.settStart(counter);
				counter = Scheduling.execProc(execution, p, counter, 0);
				p.settFinish(counter - 1);
				Scheduling.setProcessTime(p);
			}
			System.out.println("\nFCFS: " + Scheduling.printExec(execution, false));
			//System.out.println(Scheduling.printProc());
			
			//SJF
			counter = 0;
			p = sjf.peek();
			while (p != null)
			{
				sjf.remove(p);
				p.settStart(counter);
				counter = Scheduling.execProc(execution, p, counter, 0);
				p.settFinish(counter - 1);
				Scheduling.setProcessTime(p);
				p = Scheduling.getNext(sjf, counter);
			}
			System.out.println("\nSJF: " + Scheduling.printExec(execution, false));
			//System.out.println(Scheduling.printProc());
			
			//SRTF
			counter = 0;
			for(int i=0; i<totalexec; i++)
			{
				if(i != 0 || Scheduling.hasArrivedProcess(srtf, i))
					p = Scheduling.getNext(srtf, i);
				else
					p = srtf.peek();
				
				if(p.remainingBurst() == p.getBurst())
					p.settStart(i);
				p.exec();
				execution[i] = p.getPid();
				if(p.remainingBurst() == 0)
				{
					p.settFinish(i);
					Scheduling.setProcessTime(p);
					srtf.remove(p);
				}
			}
			System.out.println("\nSRTF: " + Scheduling.printExec(execution, true));
			//System.out.println(Scheduling.printProc());
			
			//RR
			int quanto;
			for(int i=0; i<rrquantum.length; i++)
			{
				counter = 0;
				quanto = rrquantum[i];
				p = rr.peek();			
				while(p != null)
				{					
					if((p.remainingBurst() - quanto) > 0)
					{
						if(p.remainingBurst() == p.getBurst())
							p.settStart(counter);
						counter = Scheduling.execProc(execution, p, counter, (p.remainingBurst()-quanto));
						((RRProcess) p).execOnce();
						RRProcess.setQ(quanto);
					}
					else
					{
						if(p.remainingBurst() == p.getBurst())
							p.settStart(counter);
						RRProcess.setQ(p.remainingBurst());
						counter = Scheduling.execProc(execution, p, counter, 0);
						p.settFinish(counter - 1);
						Scheduling.setProcessTime(p);
						rr.remove(p);
					}
					p = Scheduling.getNext(rr, counter);
				}
				System.out.println("\nRR con quanto " + quanto + ": " + Scheduling.printExec(execution, true));
				//System.out.println(Scheduling.printProc());
				
				for(int a=0; a<nProc; a++)
					rr.add(new RRProcess(proc[a].getBurst(), proc[a].getTime(), proc[a].getPid()));
			}
			
			//HRRN
			counter = 0;
			p = hrrn.peek();
			while(p != null)
			{
				hrrn.remove(p);
				p.settStart(counter);
				counter = Scheduling.execProc(execution, p, counter, 0);
				p.settFinish(counter - 1);
				Scheduling.setProcessTime(p);
				p = Scheduling.getNext(hrrn, counter);
			}
			System.out.println("\nHRRN: " + Scheduling.printExec(execution, false));
			//System.out.println(Scheduling.printProc());
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException" + "\nScanner expected an Integer and a different value was inserted!");
			e.printStackTrace();
		}
	}
}