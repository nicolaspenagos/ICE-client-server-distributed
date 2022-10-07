import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;
import java.util.Map;
import java.io.*;
import java.util.*;
import Demo.Callback;

/*
 * WORKER THREAD
 */
public class ServerTask implements Runnable {

  // Message from client
  private String msg;

  // Callback to send the message back to the correspinding client ( using
  // the response(String) method )
  private Demo.CallbackPrx callback;
  private Hashtable<String, Demo.CallbackPrx> clients;
  private Semaphore semaphore;

  public ServerTask(String msg, Demo.CallbackPrx callback, Hashtable<String, Demo.CallbackPrx> clients,
      Semaphore semaphore) {
    this.msg = msg;
    this.callback = callback;
    this.clients = clients;
    this.semaphore = semaphore;
  }

  // Handling the task in a worker thread ( concurrently )
  public void run() {

    System.out.println("\n");

    // System.out.println(msg);
    String[] parts = msg.split(":");

    String clientHostname = parts[0];

    String[] opcion = parts[1].split(" ");

    System.out.println(Arrays.toString(parts) + "\n" + Arrays.toString(opcion));
    


    try {

      switch (opcion[0].toLowerCase()) {
        
        case "listclients":
          String response = listClients();
          callback.response(response);
          break;

        case "bc":
          doBroadcast(clientHostname, parts[1]);
          break;

        case "to":
          // if msg contains to, parts should be like this
          // xhgrid10: to xhgrid12: msg
          // parts[0] hostname, parts[1] to xhgrid12, msg
          String toHost = parts[1];
          sendToHost(toHost, clientHostname);
          break;

        default:

          BigInteger num = new BigInteger(parts[1]);
          manageFiboRequest(num, clientHostname);

      }

    } catch (NumberFormatException e) {

      manageNumberFormatException(parts);

    } catch (InterruptedException e) {

      semaphore.release();
      e.printStackTrace();

    }

  }

  public void manageNumberFormatException(String[] parts) {

    if (parts[1].equals("exit")) {
      System.out.print(parts[0] + " DISCONNECTED");
    } else {
      System.out.print(parts[0] + ": " + parts[1]);
    }

    callback.response(BigInteger.ZERO.toString());

  }

  public void manageFiboRequest(BigInteger num, String clientHostname) {

    if (num.compareTo(BigInteger.ZERO) > 0) {
      callback.response(fibo(num, clientHostname).toString());
    } else {
      System.out.print(msg);
      callback.response(BigInteger.ZERO.toString());
    }

  }

  public void sendToHost(String toHost, String clientHostname) throws InterruptedException {

    toHost = toHost.replace("to", "");
    toHost = toHost.replace(" ", "");
    System.out.println("Message to: " + toHost + " from: " + clientHostname);

    semaphore.acquire();


      
      Demo.CallbackPrx callbackClient = ServerTasksManager.clients.get(toHost);
      if (callbackClient!=null) {
        callbackClient.response("Message from: " + clientHostname + msg);
        System.out.println("Message from: " + clientHostname + msg + " succesfully sent");
      }
    

    semaphore.release();

  }

  public void doBroadcast(String clientHostname, String msg) throws InterruptedException {

    msg = msg.replace("bc", "");

    semaphore.acquire();

    for (Map.Entry<String, Demo.CallbackPrx> e : ServerTasksManager.clients.entrySet()) {
      System.out.println("Broadcast from: " + clientHostname + " to: " + e.getKey() + " succesfully sent");
      e.getValue().response("Broadcast from: " + clientHostname + msg);
    }

    semaphore.release();

  }

  public String listClients() throws InterruptedException {

    String clientsList = "CLIENTS IN THE SERVER" + "\n";

    semaphore.acquire();

    for (Map.Entry<String, Demo.CallbackPrx> e : ServerTasksManager.clients.entrySet()) {
      clientsList += "    Host: " + e.getKey() + "\n";
      // System.out.println("***" + e.getKey());
    }

    semaphore.release();

    if (clientsList.equals("")) {
      clientsList = "El servidor no tiene clientes" + "\n";
    }

    return clientsList;

  }

  public BigInteger fibo(BigInteger num, String clientHostname) {

    BigInteger fibo1 = BigInteger.ONE;
    BigInteger fibo2 = BigInteger.valueOf(2);

    System.out.print(clientHostname + ": " + fibo1 + " ");
    for (BigInteger i = BigInteger.valueOf(2); i.compareTo(num) < 0; i = i.add(BigInteger.ONE)) {
      System.out.print(fibo2 + " ");
      fibo2 = fibo1.add(fibo2);
      fibo1 = fibo2.subtract(fibo1);
    }

    return fibo1;

  }

}
