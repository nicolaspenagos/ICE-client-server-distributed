 /*
  *  THREAD CLASS TO EXCUTE THE CONCURRENT REQUESTS
  */
public class Task implements Runnable{

        private Demo.PrinterPrx printer;
        private String number;
        private String hostname;


        public Task(Demo.PrinterPrx printer, String number, String hostname){
            this.printer = printer;
            this.number = number;
            this.hostname = hostname;


        }
        public void run(){
            
            long startTime = System.currentTimeMillis();
             try{
                  sendRequest(this.printer, this.number);
                  System.out.println("\n\nRequest sent from:");
                  System.out.println(hostname);
                  System.out.println("Number: "+number);

             }catch(Exception e){
                System.out.println("Connection Timeout Exception");

             }
             long estimatedTime = System.currentTimeMillis() - startTime;
             System.out.println("Time: "+estimatedTime+" ms \n\n");
        
       
        }


    public void sendRequest(Demo.PrinterPrx printer, String number){

        String msg = hostname+":"+number;
        //System.out.println("--> "+printer.printString(msg));
        printer.printString(msg);
   
    }

}