import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * THREADS POOL
 */
public class ServerTasksManager{
	
	private ExecutorService pool;
	private static ServerTasksManager instance;

	private ServerTasksManager(){
		
		// The number of active threads to perfom the tasks will the number of actual available cores
        int coreCount = Runtime.getRuntime().availableProcessors();
        pool = Executors.newFixedThreadPool(coreCount);

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

	public void executeTask(ServerTask task){
		this.pool.execute(task);
	}
}