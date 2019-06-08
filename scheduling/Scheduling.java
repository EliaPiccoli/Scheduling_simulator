package scheduling;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Scheduling {
	private static int nProc;
	private static Collection<Process> procc = new PriorityQueue<Process>(new Comparator<Process>() {
		public int compare(Process p1, Process p2) {
			return p1.getPid() - p2.getPid();
		}
	});
	
	public Scheduling(int n, int nrr, Process[] proc) {
		nProc = n;
		procc = java.util.Arrays.asList(proc);
	}
	
	public static String printProc(){
		return procc.toString();
	}
	
	public static void setProcessTime(Process p) {
		for(Process proc : procc)
		{
			if(proc.getPid() == p.getPid())
			{
				proc.settStart(p.gettStart());
				proc.settFinish(p.gettFinish());
			}
		}
	}
	
	private static int getNP() {
		return nProc;
	}
	
	public static int insBurst(Scanner k) {
		int b;
		
		do
		{
			System.out.println("Inserire tempo di Burst del processo:\t");
			b = k.nextInt();
		}while (b <= 0);
		
		return b;
	}

	public static int insTime(Scanner k) {
		int t;
		
		do
		{
			System.out.println("Inserire tempo di Arrivo del processo:\t");
			t = k.nextInt();
		}while (t < 0);
		
		return t;
	}

	public static int totalExec(Process[] x) {
		int counter = 0;
		for(Process p : x)
			counter += p.getBurst();
		
		return counter;
	}
	
	public static String printExec(int[] y, boolean preemtive)
	{
		String msg = "";
		
		if(!preemtive)
		{
			for(int i : y)
			{
				String num = java.lang.String.valueOf(i);
				if(!msg.contains(num))
				{
					if(msg.isEmpty())
						msg += num;
					else msg += ", " + num;
				}
			}
		}
		else
		{
			for(int i=0; i<y.length; i++)
			{
				if(!msg.isEmpty())
				{
					if(y[i] != y[i-1])
						msg += ", " + java.lang.String.valueOf(y[i]);
				}
				else msg += java.lang.String.valueOf(y[i]);
			}
		}
		//ho gia creato l'array con l'ordine di esecuzione (msg)
		//aggiungo la creazione della tabellina disegnata
		int n = Scheduling.getNP();
		int m = y.length+1;
		char tab[][] = new char[n][m];
		for(int i=0; i<n; i++)
			tab[i][0] = Character.forDigit(i+1, 10);
 		for(int i=1; i<m; i++)
			tab[y[i-1]-1][i] = Character.forDigit(y[i-1], 10);
		
 		int[][] time = new int[2][nProc];
 		String timeTable = "";
		int tr, tt, tw;
		for(Process p : procc)
		{
			time[0][p.getPid()-1] = p.getTime() + 1;
			time[1][p.getPid()-1] = p.gettFinish() + 1;			
			tr = p.gettStart() - p.getTime();
			tw = p.gettFinish() - p.getTime() - p.getBurst() +1;
			if(!preemtive || tw <= 0)
				tw = tr;
			tt = tw + p.getBurst();
			timeTable += "\nP" + String.format("%d", p.getPid()) + "\tTRisposta: " + String.format("%2d", tr) + 
					"\tTAttesa: " + String.format("%2d", tw) + "\tTTurnaround: " + String.format("%2d", tt);
		}
		
		String tabella = "";
		for(int i=0; i<n; i++)
		{
			for(int j=0; j<m; j++)
			{
				if(j == 0)
					tabella += "P";
				if(tab[i][j] == '\u0000')
				{
					if(j < time[0][i] || j > time[1][i]) // prima dell'arrivo dopo della fine
						tabella += " ";
					else if(j == time[0][i])			// istante di arrivo
						tabella += "x";
					else if(j < time[1][i])				// istanti di prelazione
						tabella += "-";
				}
				else tabella += String.format("%c", tab[i][j]);
				if(j == 0)
					tabella += "| ";
			}
			tabella += "\n";
		}
		
		return "[" + msg + "]" + "\n" + tabella + timeTable;
	}
	
	public static int execProc(int[] y, Process p, int c, int r)
	{
		while(p.remainingBurst() != r)
		{
			y[c++] = p.getPid();
			p.exec();
		}
		
		return c;
	}
	
	public static Process getNext(Queue<Process> x, int time)
	{
		if(!x.isEmpty())
		{
			Queue<Process> others = null;
			if(x.element() instanceof SJFProcess) //con SJF intendiamo anche la variante preemtive SRTF (gestita anche lei qui)
			{
				others = new PriorityQueue<Process>(new SJFComparator());
				for(Process p : x)
					if(((SJFProcess) p).getTime() <= time)
						others.add(p);
				return others.poll();
			}
			else if(x.element() instanceof HRRNProcess)
			{
				others = new PriorityQueue<Process>(new HRRNComparator());
				for(Process p : x)
					if(((HRRNProcess) p).getTime() <= time)
					{
						((HRRNProcess) p).setPriority(time); // calcolo la priorità attuale al processo e lo metto nella collection
						others.add(p);
					}
				
				return others.poll();
			}
			else if(x.element() instanceof RRProcess)
			{
				others = new PriorityQueue<Process>(new RRComparator());
				//elementi arrivati prima dell'ultimo quanto
				for(Process p : x)
					if(((RRProcess) p).getTime() <= (time - RRProcess.getQ()))
							others.add(p);
				//creo una lista con gli elementi arrivati precedentemente mantenendo l'ordine della PriorityQueue
				//creo una coda poi ordinerà gli elementi secondo il comparatore dell'RR
				LinkedList<Process> rq = new LinkedList<Process>(others);
				PriorityQueue<RRProcess> y = new PriorityQueue<RRProcess>(new RRComparator());
				for(Process c : x)
					y.add((RRProcess) c);
				
				int h = rq.size(); 
				RRProcess p = y.poll();
				
				while(p != null) //elementi arrivati durante l'ultimo quanto che devono superare l'ultimo proc
				{
					if(((RRProcess) p).getTime() <= time && ((RRProcess) p).getTime() > (time - RRProcess.getQ()))
					{
						//se un processo è arrivato durante l'ultimo quanto guardo nella mia readyqueue dove devo inserirlo
						if(h == 0)
						{
							rq.add((Process) p);
						}
						else
						{
							Process n = rq.get(rq.size()-1); //prendo l'elemento infondo alla coda
							if(((RRProcess)n).getQW() == 0) //se è quello che era in esecuzione prima, per scelta lo supero
								rq.add(rq.size()-1, (Process) p);
							else rq.add((Process) p); //se è un processo che era già in attesa mi accodo
						}
					}
					p = y.poll();
				}
				//Itero lungo la mia linked list che contiene tutti i processi arrivati prima dell'istante attuale e ad ognuno
				//aggiungero 1 al campo che contiene il numero di quanti attesi, campo utilizzato nel criterio di ordinamento
				ListIterator<Process> i = rq.listIterator(1);
				while(i.hasNext())
				{
					((RRProcess) i.next()).waitQ();
				}
				((RRProcess) rq.peek()).resetQ(); // azzero la testa in quanto sarà il processo che vai in esecuzione
				
				return rq.poll();
			}
			return null;
		}
		else return null;
	}
	
	public static boolean hasArrivedProcess(Queue<Process> x, int time)
	{
		for(Process p : x)
			if(p.getTime() == time)
				return true;
		return false;
	}
}
