import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import Demo.Callback;

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
		semaphore = new Semaphore(0);

	}

	// Singleton 
	public static ServerTasksManager getInstance(){

		if(instance == null){
			instance = new ServerTasksManager();
		}

		return instance;
	}

	public ExecutorService getPool(){
		return pool;
	}

	public void executeTask(String msg, Demo.CallbackPrx callback){
		this.pool.execute(new ServerTask(msg, callback, clients));

	}
}