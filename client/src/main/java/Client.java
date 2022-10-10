import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;

public class Client {

    public static final String EXIT = "exit";
    private static String hostname = "";

    public static void main(String[] args) {

        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client",
                extraArgs)) {

            // Printer configuration
            Demo.PrinterPrx twoway = Demo.PrinterPrx.checkedCast(communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);
            Demo.PrinterPrx printer = twoway.ice_twoway();

            if (printer == null) {
                throw new Error("Invalid proxy");
            }

            // Callback configuration
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Callback");
            com.zeroc.Ice.Object obj = new Callback();
            com.zeroc.Ice.ObjectPrx objectPrx = adapter.add(obj, com.zeroc.Ice.Util.stringToIdentity("callback"));
            adapter.activate();
            Demo.CallbackPrx callPrx = Demo.CallbackPrx.uncheckedCast(objectPrx);

            hostname = f("hostname");

            if (args.length == 1) {
                // sendSingleRequest(printer, (args[0]));
            } else if (args.length == 2) {
                // runStressExperiment(printer, args[0], args[1]);
            } else {
                run(printer, callPrx);
            }

        }
    }

    /*
     * CLI FIBONACCI REQUEST LOOP
     */
    public static void run(Demo.PrinterPrx printer, Demo.CallbackPrx callPrx) {

        printCLI();
        printer.printString(hostname + ":REGISTER", callPrx);

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {

                String line = reader.readLine();

                String msg = hostname + ":" + line;
                printer.printString(msg, callPrx);

                if(line.split(" ")[0].equals("to")){
                    System.out.println("");
                }

                if (line.equals(EXIT)) {
                    System.out.println(
                            "\nCLOSING CONNECTION...\n-------------------------------------------------- \n\n");
                    break;
                }

            }

        } catch (IOException e) {

        }

    }

    /*
     * UTILS
     */
    public static String f(String m) {

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
        } catch (Exception ex) {
        }

        return output;
    }

    public static void printCLI() {
        System.out.println("\n-------------------------------------------------- \n");
        System.out.println("HELLO " + hostname);
        System.out.println("\nINSTRUCTIONS:");
        System.out.println("TO GET THE FIBONACCI OF A NUMBER, ENTER THE NUMBER e.g. 10");
        System.out.println("TO SEND A MSG TO ALL THE CONNECTED CLIENTS, ENTER THE COMMAND bc  e.g. bc hello to everyone");
        System.out.println("TO SEND A MSG TO A SPECIFIC CLIENT, ENTER THE COMMAND to FOLLOWEB BY THE HOSTNAME AND : MSG e.g. to hgrid13: hello");
        System.out.println("TO LIST ALL THE CONNECTED CLIENTS, USE THE COMAND listClients e.g. listClients\n");
    }

}
