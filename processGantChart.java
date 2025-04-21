public class processGantChart{//this class to marged the sequential similar processes
   public int start,end;
   public String processType;
   public processGantChart(int s, int e, String type){
      start=s;
      end=e;
      processType=type;
   }
   public processGantChart(processGantChart process){
      this(process.start,process.end,process.processType);
   }

}
