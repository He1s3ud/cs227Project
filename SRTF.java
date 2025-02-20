import java.util.*;

public class SRTF{

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);

        System.out.print("Number of processes = ");
        int Nprocesses = read.nextInt();
        
        
        System.out.print("("); // to print the opening parenthesis at the begining 

       for(int i=1 ; i<= Nprocesses; i++){
         System.out.print("P" + i);

       if (i<Nprocesses) // to make sure, there's no comma after the last process
       System.out.print(",");
       }// end of the loop

       System.out.println(")");



        Process[] processes = new Process[Nprocesses]; //create array has the same size of process number 
        System.out.println("Arrival	times	and burst	times	as	follows:");
        for (int i = 0; i < Nprocesses; i++) {
            System.out.print("P" + (i + 1) + ":");
            System.out.print(" Arrival time = ");
            int arrivalTime = read.nextInt();
            System.out.print(", Burst time = ");
            int burstTime = read.nextInt();
            System.out.println(" ms");
            processes[i] = new Process(i + 1, arrivalTime, burstTime);
        }

        System.out.println("Scheduling Algorithm: Shortest remaining time first");
        System.out.println("Context Switch: 1 ms");

        simulateSRTF(processes); // at line 43
    }

    public static void simulateSRTF(Process[] processes) {
        int currentTime = 0; // track 
        int numberOfCompletedProcesses = 0; // To track and save number of completed process
        LinkedList<Process> readyQueue = new LinkedList<>();// list for ready process
        ArrayList<Event> timeline = new ArrayList<>();

        while (numberOfCompletedProcesses < processes.length) {
            // Add processes that have arrived to the ready queue
            for (Process process : processes) { // for each
                if (process.arrivalTime == currentTime) { // procees is objrct for class process
                    readyQueue.add(process);
                }
            }

            // If the ready queue is empty, increment time
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Find the process with the shortest remaining time
            Process currentProcess = Collections.min(readyQueue, Comparator.comparingInt(p -> p.remainTime));

            // To execute the process
            currentProcess.remainTime--;

            // Add the event to the timeline or gant chart 0-1...etc
            timeline.add(new Event(currentTime, currentProcess.pId));

            // If the process is finished
            if (currentProcess.remainTime == 0) {
                currentProcess.completionTime = currentTime + 1;
                numberOfCompletedProcesses++;
                readyQueue.remove(currentProcess);
            }

            currentTime++;
        }

        // Calculate performance metrics
        calculateMetrics(processes, timeline);
    }

    public static void calculateMetrics(Process[] processes, ArrayList<Event> timeline) {
        // Calculate average turnaround time
        double turnaroundTime = 0;
        for (Process process : processes) {
            turnaroundTime += process.completionTime - process.arrivalTime;
        }
        double averageTurnaroundTime = turnaroundTime / processes.length;

        // Calculate average waiting time
        double waitingTime = 0;
        for (Process process : processes) {
            waitingTime += process.completionTime - process.arrivalTime - process.burstTime;
        }
        double avrWaitingTime = waitingTime / processes.length;

        // Calculate CPU utilization
        double executionTime = 0;
        for (Process process : processes) {
            executionTime += process.burstTime;
        }
        double cpuUtilization = (executionTime / timeline.size()) * 100;

        
        System.out.println("Process/CS\tTime");
        for (int i = 0; i < timeline.size(); i++) {
            Event event = timeline.get(i);
            if (i > 0 && event.processId != timeline.get(i - 1).processId) {
                System.out.print("CS\t");
            } else {
                System.out.print("P" + event.processId + "\t");
            }
            System.out.println(event.time + "-" + (event.time + 1));
        }
        System.out.println("Performance Metrics");
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("Average Waiting Time: " + waitingTime);
        System.out.println("CPU Utilization: " + cpuUtilization + "%");
    }

    static class Process { // class has attributes that are related to each single process
        int pId;
        int arrivalTime;
        int burstTime;
        int remainTime;
        int completionTime;

        public Process(int id, int arrivalTime, int burstTime) {
            pId = id;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            remainTime = burstTime;
        }
    }

    static class Event {
        int time;
        int processId; //which process related to this process 

        public Event(int time, int processId) {
            this.time = time;
            this.processId = processId;
        }
    }
}