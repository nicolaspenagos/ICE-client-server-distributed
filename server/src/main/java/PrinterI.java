public class PrinterI implements Demo.Printer
{
    public void printString(String s, com.zeroc.Ice.Current current)
    {
  
        String[] parts = s.split(":");
        String clientHostname = parts[0];

        try{
        	long num = Long.parseLong(parts[1]);
        	if(num>0){
				fibo(num);
        	}else{
        		System.out.println(s);
        		//Retonar 0
        	}
        	
        }catch(NumberFormatException e){
			System.out.println(s);
			//Retonar 0

        }

        
    }

    public void fibo(long num){
    	long fibo1 = 1;
    	long fibo2 = 2;

    	System.out.println(fibo1);
    	for(long i=2; i<num; i++){
    		System.out.print(fibo2 + " ");
    	  	fibo2 = fibo1 + fibo2;
           	fibo1 = fibo2 - fibo1;
    	}

    	  
    	System.out.println();

    }

}