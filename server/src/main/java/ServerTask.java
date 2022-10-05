import java.math.BigInteger;

/*
 * WORKER THREAD
 */
public class ServerTask implements Runnable {

  // Message from client
  private String msg;

  // Callback to send the message back to the correspinding client ( using
  // the response(String) method )
  private Demo.CallbackPrx callback;

  public ServerTask(String msg, Demo.CallbackPrx callback) {
    this.msg = msg;
    this.callback = callback;
  }

  // Handling the task in a worker thread ( concurrently )
  public void run() {

    System.out.println("\n");

    String[] parts = msg.split(":");
    String clientHostname = parts[0];

    try {

      BigInteger num = new BigInteger(parts[1]);
      if (num.compareTo(BigInteger.ZERO) > 0) {
        callback.response(fibo(num, clientHostname).toString());
      } else {
        System.out.print(msg);
        callback.response(BigInteger.ZERO.toString());
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
