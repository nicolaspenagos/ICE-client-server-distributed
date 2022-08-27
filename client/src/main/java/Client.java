import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            Demo.PrinterPrx printer = twoway.ice_oneway();

            if(printer == null)
            {
                throw new Error("Invalid proxy");
            }

          
            hostname = communicator.getProperties().getProperty("Ice.Default.Host");
            //printer.printString("--------------------------------------------------");
            //printer.printString(hostname+" CONNECTED!");
            run(printer);
        }
    }

    public static void run(Demo.PrinterPrx printer){

        printCLI();


        try{

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
         
            while(true){

                String line = reader.readLine();
            
                String msg = hostname+":"+line;
                printer.printString(msg);
                System.out.println(msg);

                if(line.equals(EXIT)){
                    break;
                }


            }

        }catch(IOException e){

        }

    

    }

    public static void printCLI(){
        System.out.println("\n-------------------------------------------------- \n");
        System.out.println("HELLO "+hostname);
        System.out.println("PLEASE ENTER A NUMBER OR 'exit' TO EXIT: \n");
    }
}