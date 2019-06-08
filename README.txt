SIMULAZIONE SCHEDULING CPU IN JAVA

FUNZIONAMENTO DEL PROGRAMMA:
1- Da terminale inserire il seguente comando per avviare il programma:
			java -jar <nomearchivio>.jar
2- Inserire i diversi dati

Una volta avviata l'esecuzione del programma verrà chiesto di inserire i diversi dati.
Dati richiesti:
	- Numero processi
	- Per ogni processo verrà chiesto il tempo di burst e il tempo di arrivo, il PID verrà assegnato in automatico da parte del programma a partire da valore 1
	- Numero istanze RR, verrà chiesto quanti diversi RoundRobin visualizzare (es. Voglio RR con quanto 1 e 3 -> Inserisco 2 e successivamente mi verranno chiesti i quanti)
	- Quanti del RR, in base al valore inserito precedentemente (numero istanze RR) verrà chiesta per ogni istanza il suo quanto
Una volta inseriti tutti i dati il programma eseguirà lo scheduling dei processi tramite i diversi algoritmi, in ordine: [FCFS, SJF, SRTF, RR, HRRN]

SPECIFICHE DEL PROGRAMMA (v1)
L'output di ogni scheduling avrà sempre lo stesso stile: 

<NomeAlgoritmo> : [<Ordine di esecuzione dei processi>] -> Eventuali esecuzioni per istanti successivi dello stesso sono riassunte con un unico valore
<Disegno dello scheduling> -> Verrà visualizzata il disegno dello scheduling dove ogni processo ha la sua riga, negli istanti in cui è eseguito verrà scritto il suo PID negli istanti in attesa verrà stampato "-", dopo il termine del processo non avremo più caratteri nella sua riga; mentre se l'istante di arrivo del processo nella ReadyQueue è diverso dall'istante di risposta verrà segnalato tale istante tramite "x" (che andrà aggiunto ai "-" nel calcolo di TAttesa)
<Tabella dei tempi> -> Per ogni processo verranno riportati i 3 tempi relativi allo scheduling appena eseguito

Il programma contiene controlli per quanto riguarda l'inserimento dei dati:
- Numero Processi > 0
- TempoBurst >= 0
- TempoArrivo > 0
- Numero istanze RR >= 1
- Quanto >= 1

NB. Il programma, nella versione attuale, non contiene controlli per quanto riguarda tempi morti.
Se durante l'inserimento dei processi viene inserito un set di dati che contiene istanti in cui non è presente nessun processo nella ReadyQueue da poter eseguire, il programma inizierà la normale esecuzione e, molto probabilmente, si bloccherà lanciando una NullPointerException.

Il criterio di scelta di accodamento dei processi nella ReadyQueue, durante la simulazione di RoundRobin, è:
	Eventuali processi arrivati durante l'ultimo quanto, supereranno nella ReadyQueue l'ultimo processo eseguito






Elia Piccoli
AA 2018/2019
