public class Task implements Runnable{

        private Demo.PrinterPrx printer;
        private int number;
        private String hostname;


        public Task(Demo.PrinterPrx printer, int number, String hostname){
            this.printer = printer;
            this.number = number;
            this.hostname = hostname;


        }
        public void run(){
             System.out.println("Sending request");
             startRequest(this.printer, this.number);
        }



    public void startRequest(Demo.PrinterPrx printer, int number){

        String msg = hostname+":"+number;
        System.out.println("--> "+printer.printString(msg));
   
    }
}