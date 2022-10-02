import java.math.BigInteger;

public class PrinterI implements Demo.Printer
{
    public String printString(String s, com.zeroc.Ice.Current current)
    {
  
        System.out.println("\n");

        String[] parts = s.split(":");
        String clientHostname = parts[0];

        try{
        	BigInteger num = new BigInteger(parts[1]);
        	if(num.compareTo(BigInteger.ZERO)>0){
				return fibo(num, clientHostname).toString();
        	}else{
        	   System.out.print(s);
        	   return BigInteger.ZERO.toString();
        	}
        	
        }catch(NumberFormatException e){
            if(parts[1].equals("exit")){
                System.out.print(parts[0]+" DISCONNECTED"); 
            }else{
                System.out.print(parts[0]+": "+parts[1]); 
            }
			
			return BigInteger.ZERO.toString();

        }

        
    }

    public BigInteger fibo(BigInteger num, String clientHostname){
    	BigInteger fibo1 = BigInteger.ONE;
    	BigInteger fibo2 = BigInteger.valueOf(2);

    	System.out.print(clientHostname+": "+fibo1+" ");
    	for(BigInteger i= BigInteger.valueOf(2); i.compareTo(num)<0; i = i.add(BigInteger.ONE)){
    		System.out.print(fibo2 + " ");
    	  	fibo2 = fibo1.add(fibo2);
           	fibo1 = fibo2.subtract(fibo1);
            
    	}
        return fibo1;

    }

}