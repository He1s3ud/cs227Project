import java.util.*;

public class SchedulerSim1 {
    
   static int ctime = 0;
    
    
   static int pCount = 0;
    
    
   static int totalWaitTime = 0;
    
    
   static int totalTurnaroundTime = 0;
    
    
   static final int switchTime = 1;
    
    
   static List<String> timeline = new ArrayList<>();
    
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
       
      System.out.print("Enter the number of processes: ");
      int numProcs = 1;
      while(true){
         try{
            numProcs = sc.nextInt();
            while(numProcs==0){
               System.out.print("You enter invalid value\nPlease enter intger number except \"0\":");
               numProcs = sc.nextInt();
            }
            break;
         }catch(InputMismatchException e){
            System.out.print("You enter invalid value\nPlease enter intger number except \"0\":");
            sc.next();
         }
      }
    /*int[] arrTimes = new int[numProcs];
      int[] burstTimes = new int[numProcs];  
      int[] remTimes = new int[numProcs];
      int[] compTimes = new int[numProcs];
      int[] startTimes = new int[numProcs];*/
      Process[] processes=new Process[numProcs];
      int temp1=0,temp2=0;
      for (int i = 0; i < numProcs; i++) {
      processes[i]=new Process();
         System.out.println("Enter P" + (i + 1) + " information:");
         while(true){
            try{
               System.out.print("Arrival time: ");
               temp1 = sc.nextInt();
               processes[i].arrivalTime = temp1;
               break;
            }catch(InputMismatchException e){
               System.out.println("You enter invalid value!");
               sc.next();
            }
         }
         while(true){
            try{
               System.out.print("Burst time: ");
               temp2 = sc.nextInt();
               processes[i].burstTime=temp2;
               break;
            }catch(InputMismatchException e){
               System.out.println("You enter invalid value!");
               sc.next();
            }
         }
         processes[i].remainingTime = processes[i].burstTime;  
         processes[i].completTime=-1;  
         processes[i].startTime=-1;  
      }
   
      System.out.println("\nNumber of processes = " + numProcs);
      for (int i = 0; i < numProcs; i++) {
         System.out.println("P" + (i + 1) + ": Arrival time = " + processes[i].arrivalTime + ", Burst time = " + processes[i].burstTime + "ms");
      }
      System.out.println("\nScheduling Algorithm: Shortest Remaining Time First");
      System.out.println("Context Switch: 1ms");
        
      executeScheduler(processes,numProcs);
   }

   public static void executeScheduler(Process[] processes, int numProcs) {
      List<processGantChart> firstGant = new ArrayList<processGantChart>();
      int firstGantCounter = 0;
   
      while (pCount < numProcs) {
         //int selectedProc = pickProcess(arrTimes, remTimes, numProcs);
         int selectedProc = pickProcess(processes,numProcs);
         if (selectedProc != -1) {
                
            if (!firstGant.isEmpty() && !firstGant.get(firstGant.size() - 1).processType.endsWith("P" + (selectedProc + 1))) {
               firstGant.add(new processGantChart(ctime, ctime + 1, "CS"));
               firstGantCounter++;
               ctime += switchTime;  
            }
                
                
            recordStart(selectedProc, processes);
                
               
            firstGant.add(new processGantChart(ctime, ctime + 1, "P" + (selectedProc + 1)));
            firstGantCounter++;
                
               
            processes[selectedProc].remainingTime--;  
            ctime++;  
                
            if (processes[selectedProc].remainingTime == 0) {
               processes[selectedProc].completTime = ctime;  
               pCount++;  
               }
         } else {
            ctime++;  
         }
      }
   
        
      List<processGantChart> finalGant = new ArrayList<>();
      processGantChart prev = firstGant.get(0);
      for (int i = 1; i < firstGant.size(); i++) {
         processGantChart cur = firstGant.get(i); 
         if ((!cur.processType.equalsIgnoreCase("CS")) && prev.processType.equalsIgnoreCase(cur.processType)) {
            prev.end = cur.end; 
         } else {
            finalGant.add(prev); 
            prev = cur;
         }
      }
      finalGant.add(prev);  
   
       
      computeMetrics(processes, numProcs);
      showResults(finalGant, numProcs);
   }

    
   public static int pickProcess(Process[] processes, int numProcs) {
      int selected = -1;
      int shortest = Integer.MAX_VALUE;
        
      for (int i = 0; i < numProcs; i++) {
         if (processes[i].remainingTime > 0 && processes[i].arrivalTime <= ctime) {
            if (processes[i].remainingTime < shortest || (processes[i].remainingTime == shortest &&  processes[i].arrivalTime <  processes[selected].arrivalTime)) {
               shortest = processes[i].remainingTime;
               selected = i;
            }
         }
      }
      return selected;
   }

    
   public static void recordStart(int processIndex, Process[] process) {
      if (process[processIndex].startTime==-1) {
         process[processIndex].startTime = ctime;
      }
   }
    
    
   public static void computeMetrics(Process[] processes, int numProcs) {
      for (int i = 0; i < numProcs; i++) {
         int turnaroundTime = processes[i].completTime - processes[i].arrivalTime;
         int waitTime = turnaroundTime - processes[i].burstTime;
         totalTurnaroundTime += turnaroundTime;
         totalWaitTime += waitTime;
      }
   }

      public static void showResults(List<processGantChart> timeline, int numProcs) {
      System.out.println("\nGantt Chart:");
      System.out.printf("%-10s %s\n", "Time", "Process/CS");
      for (processGantChart gantt : timeline) {
         System.out.printf("%-7s %s\n", gantt.start + "-" + gantt.end, gantt.processType);
      }
   
      double avgTurnaround = (double) totalTurnaroundTime / numProcs;
      double avgWaitTime = (double) totalWaitTime / numProcs;
      double cpuUtil = ((double) (ctime - (pCount * switchTime)) / ctime) * 100;
   
      System.out.println("\nPerformance Metrics:");
      System.out.println("Average Turnaround Time: " + avgTurnaround);
      System.out.println("Average Waiting Time: " + avgWaitTime);
      System.out.println("CPU Utilization: " + String.format("%.2f", cpuUtil) + "%");
   }
}
