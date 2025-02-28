import java.util.*;

public class scheduling{
public static void main(String [] args){

Scanner input=new Scanner(System.in);

System.out.print("Enter the number of processes: ");
int Pnumber=input.nextInt();

System.out.println("Enter Arrival time and Burst time for each process");
List<processes>processList=new ArrayList<>();

for(int i=1;i<=Pnumber;i++){
System.out.print("P"+i+":Arrival time, Burst time=");
int ATime=input.nextInt();
int BTime=input.nextInt();
processes p=new processes(i,ATime,BTime);
processList.add(p);
}


System.out.println("Schduling Algorithm: Shortest remaining time first");
System.out.println("Context Switch: 1ms");
SRTF(processList);


}

public static void SRTF(List<processes>process){

int cArivalTime=0;
int completedProcesss=0;
int minBrustTime=0;
List<String>timeLine=new ArrayList<>();
processes nextRegister=null;
processes prevRegister=null;



do{
processes currentRegister=null;
for(processes p:process){

if(p.arrivalTime<=cArivalTime && p.reminingT>0)
if(minBrustTime==0){
currentRegister=p;
minBrustTime=p.reminingT;
}

else
if(p.reminingT<minBrustTime){
currentRegister=p;
minBrustTime=p.reminingT;
}
}


if(currentRegister==null){
cArivalTime++;
continue;
}
for(processes p1:process){//to know which process will complete next

if(currentRegister!=null &&p1.reminingT>0 && p1.reminingT<currentRegister.reminingT )
nextRegister=p1;
else
continue;

}


if(nextRegister.processID==currentRegister.processID){
cArivalTime++;
continue;
}
else
if(prevRegister!=null&& prevRegister.processID!=currentRegister.processID)
timeLine.add(cArivalTime+"-"+(cArivalTime+1)+"      CS");

timeLine.add(cArivalTime +"-"+(cArivalTime+1)+"      P"+currentRegister.processID);
currentRegister.reminingT--;
cArivalTime++;
prevRegister=currentRegister;

if(currentRegister.reminingT==0){
completedProcesss++;
}

}while(completedProcesss!=process.size());

System.out.println("\nTime Process/CS");
for (String event : timeLine) {
System.out.println(event); }





}
}