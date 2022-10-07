import java.math.BigInteger;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;
import java.util.Map;

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

  public ServerTask(String msg, Demo.CallbackPrx callback, Hashtable<String, Demo.CallbackPrx> clients, Semaphore semaphore) {
    this.msg = msg;
    this.callback = callback;
    this.clients = clients;
    this.semaphore = semaphore;
  }

  // Handling the task in a worker thread ( concurrently )
  public void run() {

    System.out.println("\n");

    System.out.println(msg);
    String[] parts = msg.split(":");
    String clientHostname = parts[0];
   
    
    try {

      if(parts[1].equalsIgnoreCase("List clients")){
       
        String response = listClients();
        System.out.print(response);
        callback.response(response);
          
         
      } else if(parts[1].equals("BC")){

        try {
            semaphore.acquire();
            for (Map.Entry<String, Demo.CallbackPrx> e : clients.entrySet()){
              System.out.println("Broadcast from: " + clientHostname + " to: " + e.getKey() + " succesfully sent");
              e.getValue().response("Broadcast from: " + clientHostname + msg);
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            semaphore.release();
        }
           

      } else if(parts[1].contains("to")){
        //if msg contains to, parts should be like this
        //xhgrid10: to xhgrid12: msg
        //parts[0] hostname, parts[1] to xhgrid12, msg

        String toHost = parts[1];
        toHost.replace("to", "");
        toHost.replace(" ", "");
        System.out.println("Message to: " + toHost + " from: " + clientHostname);

        try {
            semaphore.acquire();
            for (Map.Entry<String, Demo.CallbackPrx> e : clients.entrySet()){
              if(e.getKey().equals(toHost)){
                e.getValue().response("Message from: " + clientHostname + msg);
                System.out.println("Message from: " + clientHostname + msg + " succesfully sent");
              }
            }         
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            semaphore.release();
        }


      } else{ //else{

        BigInteger num = new BigInteger(parts[1]);
        if (num.compareTo(BigInteger.ZERO) > 0) {
          callback.response(fibo(num, clientHostname).toString());
        } else {
          System.out.print(msg);
          callback.response(BigInteger.ZERO.toString());
        }
      }

    } catch (NumberFormatException e) {

      if (parts[1].equals("exit")) {
        System.out.print(parts[0] + " DISCONNECTED");
      } else {
        System.out.print(parts[0] + ": " + parts[1]);
      }

      callback.response(BigInteger.ZERO.toString());

    }

  }

  public String listClients(){
    String clientsList = "CLIENTS IN THE SERVER" + "\n";

    try {
      semaphore.acquire();
            
      for (Map.Entry<String, Demo.CallbackPrx> e : clients.entrySet()){
        clientsList += "Host: " + e.getKey() + "\n"; 
        System.out.println("***" + e.getKey());
      }
            
    } catch (InterruptedException e) {
      e.printStackTrace();
    }finally{
      semaphore.release();
    }

    if(clientsList.equals("")){
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
