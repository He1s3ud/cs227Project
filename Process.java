public class Process {
   public int  arrivingTime, burstTime, remainingTime, startingTime, completingTime;

   public Process(){
      this.arrivingTime= 0;
      this.burstTime = 0;
      this.remainingTime = 0;
      this.startingTime = -1;
   }
   
   public Process( int arrivingTime, int burstTime) {
      this.arrivingTime = arrivingTime;
      this.burstTime = burstTime;
      this.remainingTime = burstTime;
      this.startingTime = -1;
   }

}
