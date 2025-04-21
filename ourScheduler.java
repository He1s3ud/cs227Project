import java.util.*;

public class ourScheduler {
    
   static int currentTime=0, processCounter=0, totalWaitTime=0, totalTurnaroundTime=0;     
   static final int switchTime=1;
        
   public static void main(String[] args) {
      Scanner sc=new Scanner(System.in);
       
      System.out.print("Enter the number of processes: ");
      int numberOfProcess = 1;
      while(true){
         try{
            numberOfProcess = sc.nextInt();
            while(numberOfProcess==0){//any time the user enter 0 number make error in the next instructions, so we solve the problem by prevent the user from entering 0
               System.out.print("You enter invalid value\nPlease enter intger number except \"0\":");
               numberOfProcess = sc.nextInt();
            }
            break;
         }catch(InputMismatchException e){
            System.out.print("You enter invalid value\nPlease enter intger number except \"0\":");
            sc.next();
         }
      }
      Process[] processes=new Process[numberOfProcess];
      int temp1=0,temp2=0;
      for (int i = 0; i < numberOfProcess; i++) {
         processes[i]=new Process();
         System.out.println("Enter P" + (i + 1) + " information:");
         while(true){
            try{
               System.out.print("Arrival time: ");
               temp1=sc.nextInt();
               processes[i].arrivingTime = temp1;
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
         processes[i].remainingTime=processes[i].burstTime;  //in the begining the remaining time is the full burst time
         processes[i].completingTime=-1;  
         processes[i].startingTime=-1;  
      }
   
       System.out.print("\nNumber of processes = " + numberOfProcess); 
        System.out.print("("); // to print the opening parenthesis at the begining 
        for(int i=1 ; i<= numberOfProcess; i++){
         System.out.print("P" + i);
         if (i<numberOfProcess) // to make sure, there's no comma after the last process
         System.out.print(",");
       }// end of the loop
        System.out.println(")");

       for (int i = 0; i < numberOfProcess; i++) {
         System.out.println("P" + (i + 1) + ": Arrival time = " + processes[i].arrivingTime + ", Burst time = " + processes[i].burstTime + "ms");
      }
      System.out.println("\nScheduling Algorithm: Shortest Remaining Time First");
      System.out.println("Context Switch: 1ms");
        
      runSchedular(processes,numberOfProcess);
   }

   public static void runSchedular(Process[] processes, int numProcs) {
      List<processGantChart> firstGant=new ArrayList<processGantChart>();//this array list is hold all step happen after any unit time, beacuse help us to know the processes have the same name to marege thim togather
   
      while (processCounter<numProcs) {
         int selectedProc=pickProcess(processes,numProcs);//since the method here select the shortest burst time avalible process
         if (selectedProc!=-1){
                
            if (!firstGant.isEmpty()&& !firstGant.get(firstGant.size()-1).processType.endsWith("P"+(selectedProc+ 1))) {
               firstGant.add(new processGantChart(currentTime, currentTime + 1, "CS"));
               currentTime+=switchTime;
            }
            processStart(selectedProc, processes);
                
            firstGant.add(new processGantChart(currentTime, currentTime+1, "P" + (selectedProc+1)));
            processes[selectedProc].remainingTime--;  
            currentTime++;
                
            if (processes[selectedProc].remainingTime==0){
               processes[selectedProc].completingTime=currentTime;
               processCounter++;
            }
         } else{
            currentTime++;
         }
      }
   
      List<processGantChart> finalGant=new ArrayList<>();//this the array list to marege any sequence process have the same name
      processGantChart prev=firstGant.get(0);
      for (int i=1;i<firstGant.size();i++){
         processGantChart cur=firstGant.get(i); 
         if ((!cur.processType.equalsIgnoreCase("CS"))&&prev.processType.equalsIgnoreCase(cur.processType))//if the current process is not context switching and has name like the previuse, then make the previuse end time like the current end time
            prev.end=cur.end; 
         else{//if it's context switching or the current process hasn't name like the previuse then put it in the final array list, the pre have has end time to the last sequence process has the same name
            finalGant.add(prev); 
            prev= cur;
         }
      }
      finalGant.add(prev);  
   
      computeMetrics(processes, numProcs);
      printResults(finalGant,numProcs);
   }
   
   public static int pickProcess(Process[] processes, int numProcs){
      int selectedProcess=-1;
      int shortest=Integer.MAX_VALUE;
      for(int i=0;i<numProcs;i++) {
         if(processes[i].remainingTime>0&& processes[i].arrivingTime<=currentTime){
            if (processes[i].remainingTime< shortest||(processes[i].remainingTime== shortest&&processes[i].arrivingTime<processes[selectedProcess].arrivingTime)){
               shortest=processes[i].remainingTime;
               selectedProcess=i;
            }
         }
      }
      return selectedProcess;
   }
    
   public static void processStart(int processIndex, Process[] process){
      if(process[processIndex].startingTime==-1){
         process[processIndex].startingTime=currentTime;
      }
   }
    
   public static void computeMetrics(Process[] processes, int numProcs) {
      for(int i=0;i<numProcs;i++){
         int turnaroundTime=processes[i].completingTime-processes[i].arrivingTime;
         int waitTime=turnaroundTime-processes[i].burstTime;
         totalTurnaroundTime+=turnaroundTime;
         totalWaitTime+= waitTime;
      }
   }

   public static void printResults(List<processGantChart> timeline, int numProcs){
      System.out.println("\nGantt Chart:");
      System.out.printf("%-10s %s\n", "Time", "Process/CS");
      for (processGantChart gantt: timeline) {
         System.out.printf("%-7s %s\n", gantt.start +"-"+ gantt.end, gantt.processType);
      }
      double avarageTurnaroundTime=(double)totalTurnaroundTime/numProcs;
      double avarageWaitingTime=(double)totalWaitTime/numProcs;
      double cpuUtilization =((double)(currentTime-(processCounter*switchTime))/currentTime)*100;
   
      System.out.println("\nPerformance Metrics:");
      System.out.println("Average Turnaround Time: "+avarageTurnaroundTime);
      System.out.println("Average Waiting Time: "+avarageWaitingTime);
      System.out.println("CPU Utilization: "+String.format("%.2f", cpuUtilization) + "%");
   }
}
