/*
 * RESPONSE FROM THE SERVER
 */
public class Callback implements Demo.Callback {

    public void response(String s, com.zeroc.Ice.Current current){
        long estimatedTime = System.currentTimeMillis() - Client.startTime;
        
        if(Client.responseCounter!=0){
            System.out.println("--> " + s + "\n");
            System.out.println("Response time: " + estimatedTime + " ms");
        }
        Client.responseCounter++;
        
    }

}
