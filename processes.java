public class Process {
    public int  arrivalTime, burstTime, remainingTime, startTime, completTime;


    public Process(){
    this.arrivalTime = 0;
        this.burstTime = 0;
        this.remainingTime = 0;
        this.startTime = -1;
    }
    
    public Process( int arrivalTime, int burstTime) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.startTime = -1;
    }

}
