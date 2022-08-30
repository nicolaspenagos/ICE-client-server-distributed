public class PrinterI implements Demo.Printer
{
    public long printString(String s, com.zeroc.Ice.Current current)
    {
  
        System.out.println("\n");

        String[] parts = s.split(":");
        String clientHostname = parts[0];

        try{
        	long num = Long.parseLong(parts[1]);
        	if(num>0){
				return fibo(num, clientHostname);
        	}else{
        	   System.out.print(s);
        	   return 0;
        	}
        	
        }catch(NumberFormatException e){
            if(parts[1].equals("exit")){
                System.out.print(parts[0]+" DISCONNECTED"); 
            }else{
                System.out.print(parts[0]+": "+parts[1]); 
            }
			
			return 0;

        }

        
    }

    public long fibo(long num, String clientHostname){
    	long fibo1 = 1;
    	long fibo2 = 2;

    	System.out.print(clientHostname+": "+fibo1+" ");
    	for(long i=2; i<num; i++){
    		System.out.print(fibo2 + " ");
    	  	fibo2 = fibo1 + fibo2;
           	fibo1 = fibo2 - fibo1;
            
    	}
        return fibo1;

    }

}