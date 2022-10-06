/*
 * MESSAGE FROM CLIENT
 */
public class PrinterI implements Demo.Printer {

    public void printString(String msg, Demo.CallbackPrx callback, com.zeroc.Ice.Current current) {

        // Get the unique instance of the threads pool (Singleton)
        ServerTasksManager serverTasksManager = ServerTasksManager.getInstance();

        // Perform the task in a worker thread sending the callback to be able to
        // return the result to the corresponding client
        serverTasksManager.executeTask(msg, callback);

    }

}