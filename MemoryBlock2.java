public class MemoryBlock{

private int size,sAddress,eAddress, frag;
private String statu, processID;

public MemoryBlock(int s, int start){
size=s;
sAddress=start;
eAddress=s+start-1;
frag=0;
statu="";
processID="null";
}

public int getSize(){
return size;}

public int getStart(){
return sAddress;}

public int getEnd(){
return eAddress;}

public int getFrag(){
return frag;}

public String getProcID(){
return processID;}

public boolean isAllocate(){
return statu.equalsIgnoreCase("allocate")?true:false;}

public void setSize(int s){
size=s;}

public void setStart(int s){
sAddress=s;}

public void setFrag(int f){
frag=f;}

public void setProcID(String p){
processID=p;}

public void setAllocate(String is){
statu=is;}
}
