import java.util.*;
public class processes{
int processID;
int arrivalTime;
int cpuBrust;
int reminingT;
public processes(int id,int aTime,int bTime){
processID=id;
arrivalTime=aTime;
cpuBrust=bTime;
reminingT=bTime;
}
public void setID(int i){
processID=i;
}

public int getID(){
return processID;
}
public int getATime(){
return arrivalTime;
}

public int getBTime(){
return cpuBrust;
}

public int getreminingT(){
return reminingT;
}
}

