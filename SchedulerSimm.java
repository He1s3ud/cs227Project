import java.util.*;

public class SchedulerSimm {
    // currentTime
    static int ctime = 0;
    
    // completedProcesses
    static int pCount = 0;
    
    // 'totalWait'
    static int totalWaitTime = 0;
    
    // 'totalTurnaround'
    static int totalTurnaroundTime = 0;
    
    //'contextSwitchTime'
    static final int switchTime = 1;
    
    //'ganttChart' 
    static List<String> timeline = new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
       
        System.out.print("Enter the number of processes: ");
        int numProcs = sc.nextInt();
        
        int[] arrTimes = new int[numProcs];
        int[] burstTimes = new int[numProcs];  
        int[] remTimes = new int[numProcs];
        int[] compTimes = new int[numProcs];
        int[] startTimes = new int[numProcs];
        
        for (int i = 0; i < numProcs; i++) {
            System.out.println("Enter P" + (i + 1) + " information:");
            System.out.print("Arrival time: ");
            arrTimes[i] = sc.nextInt();
            System.out.print("Burst time: ");
            burstTimes[i] = sc.nextInt();
            remTimes[i] = burstTimes[i];  // Initial remaining time is the same as burst time
            compTimes[i] = -1;  // -1 signifies the process has not been completed yet
            startTimes[i] = -1;  // -1 signifies the process has not started yet
        }

        System.out.println("\nNumber of processes = " + numProcs);
        for (int i = 0; i < numProcs; i++) {
            System.out.println("P" + (i + 1) + ": Arrival time = " + arrTimes[i] + ", Burst time = " + burstTimes[i] + "ms");
        }
        System.out.println("\nScheduling Algorithm: Shortest Remaining Time First");
        System.out.println("Context Switch: 1ms");
        
        executeScheduler(arrTimes, burstTimes, remTimes, compTimes, startTimes, numProcs);
    }

    public static void executeScheduler(int[] arrTimes, int[] burstTimes, int[] remTimes, int[] compTimes, int[] startTimes, int numProcs) {
        List<processGantChart> firstGant = new ArrayList<>();
        int firstGantCounter = 0;

        while (pCount < numProcs) {
            int selectedProc = pickProcess(arrTimes, remTimes, numProcs);
            if (selectedProc != -1) {
                // Add context switch if the process changes
                if (!firstGant.isEmpty() && !firstGant.get(firstGant.size() - 1).processType.endsWith("P" + (selectedProc + 1))) {
                    firstGant.add(new processGantChart(ctime, ctime + 1, "CS"));
                    firstGantCounter++;
                    ctime += switchTime;  // Add context switch time
                }
                
                // Record the start time if it's the first time the process is scheduled
                recordStart(selectedProc, startTimes);
                
                // Add the process event to the Gantt chart
                firstGant.add(new processGantChart(ctime, ctime + 1, "P" + (selectedProc + 1)));
                firstGantCounter++;
                
                // Execute the process for 1ms
                remTimes[selectedProc]--;  // Decrease remaining burst time for the process
                ctime++;  // Increment system time by 1
                
                // If the process is completed, mark its completion time
                if (remTimes[selectedProc] == 0) {
                    compTimes[selectedProc] = ctime;  // Mark process as completed
                    pCount++;  // Increment completed process count
                }
            } else {
                ctime++;  // If no process can run, increment system time
            }
        }

        // Generate the final Gantt chart, merging adjacent processes if needed
        List<processGantChart> finalGant = new ArrayList<>();
        processGantChart prev = firstGant.get(0);
        for (int i = 1; i < firstGant.size(); i++) {
            processGantChart cur = firstGant.get(i); 
            if ((!cur.processType.equalsIgnoreCase("CS")) && prev.processType.equalsIgnoreCase(cur.processType)) {
                prev.end = cur.end; // Merge the current process with the previous one if they are the same
            } else {
                finalGant.add(prev); 
                prev = cur;
            }
        }
        finalGant.add(prev);  // Add the last process

        // Compute and display metrics
        computeMetrics(arrTimes, burstTimes, compTimes, numProcs);
        showResults(finalGant, numProcs);
    }

    // Picks the process with the shortest remaining time
    public static int pickProcess(int[] arrTimes, int[] remTimes, int numProcs) {
        int selected = -1;
        int shortest = Integer.MAX_VALUE;
        
        for (int i = 0; i < numProcs; i++) {
            if (remTimes[i] > 0 && arrTimes[i] <= ctime) {
                if (remTimes[i] < shortest || (remTimes[i] == shortest && arrTimes[i] < arrTimes[selected])) {
                    shortest = remTimes[i];
                    selected = i;
                }
            }
        }
        return selected;
    }

    // Records the start time of a process if it is not started yet
    public static void recordStart(int process, int[] startTimes) {
        if (startTimes[process] == -1) {
            startTimes[process] = ctime;
        }
    }
    
    // Computes the performance metrics: average wait time, turnaround time
    public static void computeMetrics(int[] arrTimes, int[] burstTimes, int[] compTimes, int numProcs) {
        for (int i = 0; i < numProcs; i++) {
            int turnaroundTime = compTimes[i] - arrTimes[i];
            int waitTime = turnaroundTime - burstTimes[i];
            totalTurnaroundTime += turnaroundTime;
            totalWaitTime += waitTime;
        }
    }

    // Displays the results: Gantt chart and performance metrics
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
