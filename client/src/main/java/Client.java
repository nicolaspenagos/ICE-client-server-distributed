import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client
{

    public static final String EXIT = "exit";
    private static String hostname = "";


    public static void main(String[] args)
    {

        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"config.client",extraArgs))
        {
            //com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 10000");
            Demo.PrinterPrx twoway = Demo.PrinterPrx.checkedCast(
                communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);
            //Demo.PrinterPrx printer = Demo.PrinterPrx.checkedCast(base);
            Demo.PrinterPrx printer = twoway.ice_twoway();

            if(printer == null)
            {
                throw new Error("Invalid proxy");
            }

          
            hostname = f("hostname");
            runStressExperiment(printer, args[0]);
            //run(printer);

        }
    }

    public static void runStressExperiment(Demo.PrinterPrx printer, String number){
          String msg = hostname+":"+number;
          System.out.println("--> "+printer.printString(msg));
    }
    

    public static String f(String m)
    {

        String str = null, output = "";

        InputStream s;
        BufferedReader r;

        try {
            Process p = Runtime.getRuntime().exec(m);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())); 
            while ((str = br.readLine()) != null) 
            output += str + System.getProperty("line.separator"); 
            br.close(); 
            return output;
        }
        catch(Exception ex) {
        }

        return output;
    }


    /*
    *   FORMER EXERCISE CODE
    */
    public static void run(Demo.PrinterPrx printer)
    {

        printCLI();


        try{

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
         
            while(true){

                String line = reader.readLine();
            
                String msg = hostname+":"+line;
                System.out.println("--> "+printer.printString(msg));
                

                if(line.equals(EXIT)){
                    System.out.println("\nCLOSING CONNECTION...\n-------------------------------------------------- \n\n");
                    break;
                }


            }

        }catch(IOException e){

        }

    }

    public static void printCLI()
    {
        System.out.println("\n-------------------------------------------------- \n");
        System.out.println("HELLO "+hostname);
        System.out.println("PLEASE ENTER A NUMBER OR 'exit' TO EXIT: \n");
    }

    /*
    
    public static void runStressExperiment(Demo.PrinterPrx printer, int number, int numberOfConcurrentRequests)
    {

        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(coreCount);
        


        for(int i=0; i<numberOfConcurrentRequests; i++){
            
            
            pool.execute(

                new Task(printer, number, hostname)

            );


        }

        pool.shutdown();
        while(!pool.isTerminated()){
            Thread.yield();
        }

    }
    */

   
}







