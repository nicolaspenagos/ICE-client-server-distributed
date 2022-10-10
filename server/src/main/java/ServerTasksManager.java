import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/*
 * THREADS POOL
 */
public class ServerTasksManager{
	
	private ExecutorService pool;
	private static ServerTasksManager instance;
	private Hashtable<String, Demo.CallbackPrx> clients;
	private Semaphore semaphore;

	private ServerTasksManager(){
		
		// The number of active threads to perfom the tasks will the number of actual available cores
        int coreCount = Runtime.getRuntime().availableProcessors();
        pool = Executors.newFixedThreadPool(coreCount);

		// Hashtable for storing client hostnames
		clients = new Hashtable<String, Demo.CallbackPrx>();
		semaphore = new Semaphore(1);

	}

	// Singleton 
	public static ServerTasksManager getInstance(){

		if(instance == null){
			instance = new ServerTasksManager();
		}

		return instance;
	}

	public void addClient(String hostname, Demo.CallbackPrx callback){

	
	

		try{
			semaphore.acquire();
			if(!clients.containsKey(hostname)){
				clients.put(hostname, callback);
			}			
	
		} catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            semaphore.release();
			System.out.println("\n"+hostname+ " CONNECTED");
        }
	}

	public ExecutorService getPool(){
		return pool;
	}

	public void executeTask(String clientMsg, Demo.CallbackPrx callback){

		String[] parts = clientMsg.split(":");
		String hostname = parts[0].trim();
		String msg = (parts.length<3)?parts[1]: parts[1]+":"+ parts[2];
		

		System.out.println(msg);
		if(clients.containsKey(hostname)){
			this.pool.execute(new ServerTask(hostname, msg, this.semaphore, this.clients));
		}else{
			this.addClient(hostname, callback);
		}
		
	}
}