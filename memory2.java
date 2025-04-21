import java.util.*;

public class memory{
   public static  Scanner input=new Scanner(System.in);
   public static MemoryBlock[] myMemory;// here we initialze the memory blocks
   public static int stratigy=-1,numOfReport=0;
//here we provide the allocateing stratigies

   public static void Allocateing(){
      boolean valid=false;
      System.out.print("Enter Aprocess ID and size of process: ");
      String ID=input.next();
      int size=0;
      while(!valid){
         try{
            size= input.nextInt();
            valid=true;
         } catch (InputMismatchException e) {
            System.out.print("Invalid size, enter an integer: ");
            input.nextLine();
         }
         }
         valid=false;
         while(!valid){
         if(size<=0){
            System.out.print("invalid size, the size must be bigger than 0./ntry again: "); 
            size= input.nextInt();
         }
         else
         valid=true;
      }
      int select=-1;
      int smallestS=Integer.MAX_VALUE;
      int biggestS=-1;
   
      for(int i=0;i<myMemory.length;i++){
         if(!myMemory[i].isAllocate()&&myMemory[i].getSize()>=size){
            if(stratigy==1){//this is first fit stratigy
               select=i;
               break;
            }
            else if(stratigy==2){//this is best fit stratigy
               if(myMemory[i].getSize()<smallestS){
                  smallestS=myMemory[i].getSize();
                  select=i;
               }
            }
            else if(stratigy==3){//this is worest fit stratigy
               if (myMemory[i].getSize()>biggestS){
                  biggestS=myMemory[i].getSize();
                  select=i;
               }
            }
         }
      }
   
      if(select!=-1){
         myMemory[select].setAllocate("allocate");
         myMemory[select].setProcID(ID);
         myMemory[select].setFrag((myMemory[select].getSize()-size));
         System.out.println(ID+" Allocated at address: "+myMemory[select].getStart()+", and the internal fragmentation is "+myMemory[select].getFrag());
         System.out.println("================================================================================");
      }
      else
         System.out.println("Error, we can't allocate the any block for you");
   }

   public static void deallocating(){
      boolean findProcess=false;
      System.out.print("Enter the process you want to deallocate: ");
      String procID=input.next();
   
      for(int i=0;i<myMemory.length;i++){
         if(myMemory[i].isAllocate()&&myMemory[i].getProcID().equalsIgnoreCase(procID)){
            myMemory[i].setAllocate("free");
            myMemory[i].setProcID("null");
            myMemory[i].setFrag(0);
            findProcess=true;
            System.out.println("deallocate process "+procID+" is success");
            System.out.println("================================================================================");
            break;
         }
      }
   
      if(!findProcess)
         System.out.println("Error, we don't have this process");
   
   }

   public static void blocksReport(){
      if(numOfReport>0){
         System.out.println("Memory blocks:\n================================================================================");
         System.out.printf("%-8s %-8s %-8s %-8s %-8s %-8s\n","Block#","size","start-end","statu","procesID","fragment");
         System.out.println("================================================================================");
         for(int i=0;i<myMemory.length;i++)
            System.out.printf("%-8s %-8s %-8s %-10s %-19s %-8s\n",i,myMemory[i].getSize(),myMemory[i].getStart()+"-"+myMemory[i].getEnd(),(myMemory[i].isAllocate()?"Allocate":"Free"),myMemory[i].getProcID(),myMemory[i].getFrag());
         System.out.println("================================================================================");
      }
      else{
         System.out.println("Memory blocks:\n=============================================");
         System.out.printf("%-8s %-8s %-8s %-8s\n","Block#","size","start-end","statu");
         System.out.println("=============================================");
         for(int i=0;i<myMemory.length;i++)
            System.out.printf("%-8s %-8s %-8s %-8s\n",i,myMemory[i].getSize(),myMemory[i].getStart()+"-"+myMemory[i].getEnd(),(myMemory[i].isAllocate()?"Allocate":"Free"));
         System.out.println("=============================================");
      }
   }



   public static void main(String[] args){
      System.out.print("Enter the number of blocks: ");
      boolean valid=false;
      int num=0;
      while(!valid){
         try{
            num=input.nextInt();
            valid=true;
         }catch(InputMismatchException e){
            System.out.print("Invalid blocks number, enter an integer: ");
            input.nextLine();
         }
         }
         valid=false;
         while(!valid){
         if(num<=0){
            System.out.print("invalid size, the size must be bigger than 0.\ntry again: "); 
            num=input.nextInt();
         }
         else
         valid=true;
      }
      myMemory=new MemoryBlock[num];
      System.out.println("Enter the size of each block in KB ");
      int size=0,startSum=0;
      for (int i=0;i<myMemory.length;i++){
      valid=false;
         while(!valid){
            try{
            System.out.print("Block"+(i+1)+" size: ");
               size=input.nextInt();
               valid=true;
            }catch(InputMismatchException e) {
               System.out.print("Invalid size, enter an integer ");
               input.nextLine();
            }
           
         }
         myMemory[i]=new MemoryBlock(size,startSum);
         startSum+=size;
      }
      System.out.print("Enter allocation strategy (1 for first-fit, 2 for best-fit, 3 for worst-fit): ");
      valid=false;
      while(!valid){
         stratigy=input.nextInt();
         if(stratigy==1||stratigy==2||stratigy==3)
            valid=true;
         else{
            System.out.print("Your input invalid, Enter allocation strategy (1 for first-fit, 2 for best-fit, 3 for worst-fit): ");
            input.nextLine();
         }
      }
      System.out.println("Memory blocks are created...");
      blocksReport();
      numOfReport++;
      int choice=0;
      do{
         System.out.println("1) Allocates memory blocks");
         System.out.println("2) De-allocates memory blocks");
         System.out.println("3) Print report about the current state of memory and internal Fragmentation");
         System.out.println("4) Exit");
         System.out.println("================================================================================");
         System.out.print("Enter your choice: ");
         choice=input.nextInt();
      
         switch(choice){
            case 1:
               Allocateing();
               break;
            case 2:
               deallocating();
               break;
            case 3:
               blocksReport();
               break;
            case 4:
               System.out.println("Have a nice day, bye!");
               break;
            default:
               System.out.println("invalid choice, try another one!");
         }
      }while(choice!=4);
   
   }
}
