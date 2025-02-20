import java.util.*;

public class SF2 {
public static void main(String[] args) {
        Scanner read = new Scanner(System.in);

        System.out.print("Number of processes = ");
        int Nprocesses = read.nextInt();

        System.out.print("(");

        for (int i = 1; i <= Nprocesses; i++) {
            System.out.print("P" + i);

            if (i < Nprocesses)
                System.out.print(",");
        }

        System.out.println(")");

        Process[] processes = new Process[Nprocesses];
        System.out.println("Arrival\ttimes\tand\tburst\ttimes\tas\tfollows:");

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

        simulateSRTF(processes);
    }

    public static void simulateSRTF(Process[] processes) {
        int currentTime = 0;
        int completedProcesses = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainTime));
        ArrayList<Event> timeline = new ArrayList<>();
        int contextSwitchTime = 1; // Context switch overhead

        while (completedProcesses < processes.length || !readyQueue.isEmpty()) {
            // Add arriving processes to the ready queue
            for (Process process : processes) {
                if (process.arrivalTime == currentTime) {
                    readyQueue.add(process);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process currentProcess = readyQueue.poll();

            if (timeline.size() > 0 && 
                !timeline.get(timeline.size() - 1).processId.equals("CS") &&
                !timeline.get(timeline.size() - 1).processId.equals(String.valueOf(currentProcess.pId))) {

                currentTime += contextSwitchTime;
                timeline.add(new Event(currentTime - 1, "CS"));

                // Add the preempted process back to the ready queue
                int preemptedProcessId = timeline.get(timeline.size() - 2).processId.charAt(1) - '0'; // Get ID of preempted process
                for (Process p : processes) {
                    if (p.pId == preemptedProcessId) {
                        readyQueue.add(p); // Add the correct process.
                        break;
                    }
                }
                currentProcess = readyQueue.poll(); // Get the process to run after context switch
            }

            currentProcess.remainTime--;
            timeline.add(new Event(currentTime, "P" + currentProcess.pId));

            if (currentProcess.remainTime == 0) {
                currentProcess.completionTime = currentTime + 1;
                completedProcesses++;
            } else {
                // Create a NEW Process object
                Process newProcess = new Process(currentProcess.pId, currentProcess.arrivalTime, currentProcess.burstTime);
                newProcess.remainTime = currentProcess.remainTime; // Update remaining time

                readyQueue.add(newProcess); // Add the NEW process to the queue
            }

            currentTime++;
        }

        calculateMetrics(processes, timeline);
    }

    public static void calculateMetrics(Process[] processes, ArrayList<Event> timeline) {
        double turnaroundTime = 0;
        double totalWaitingTime = 0;
        double totalExecutionTime = 0;

        // Corrected Waiting Time Calculation
        for (Process process : processes) {
            process.waitingTime = 0; // Initialize waiting time for each process
        }

        for (int i = 0; i < timeline.size(); i++) {
            Event event = timeline.get(i);
            if (event.processId.equals("CS")) {
                // Context Switch - Add waiting time to the process that was preempted
                int preemptedProcessId = timeline.get(i - 2).processId.charAt(1) - '0'; // Get the process ID from the previous event
                for (Process process : processes) {
                    if (process.pId == preemptedProcessId) {
                        process.waitingTime++;
                        break;
                    }
                }
            }
        }
        for(Process process : processes)
        {
            totalWaitingTime += process.waitingTime;
        }

        for (Process process : processes) {
            turnaroundTime += process.completionTime - process.arrivalTime;
            totalExecutionTime += process.burstTime;
        }

        double averageTurnaroundTime = turnaroundTime / processes.length;
        double averageWaitingTime = totalWaitingTime / processes.length;

        // Corrected CPU Utilization Calculation
        int totalSimulationTime = timeline.get(timeline.size() - 1).time +1; // Time when the last process finishes
        double cpuUtilization = (totalExecutionTime / totalSimulationTime) * 100;

        System.out.println("Process/CS\tTime");
        for (int i = 0; i < timeline.size(); i++) {
            Event event = timeline.get(i);

            System.out.print(event.processId + "\t");
            System.out.println(event.time + "-" + (event.time + 1));
        }
        System.out.println("Performance Metrics");
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("CPU Utilization: " + cpuUtilization + "%");
    }

    static class Process {
        int pId;
        int arrivalTime;
        int burstTime;
        int remainTime;
        int completionTime;
        int waitingTime; // Add waitingTime attribute

        public Process(int id, int arrivalTime, int burstTime) {
            pId = id;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            remainTime = burstTime;
            waitingTime = 0;
        }
    }

    static class Event {
        int time;
        String processId;

        public Event(int time, String processId) {
            this.time = time;
            this.processId = processId;
        }
    }
}