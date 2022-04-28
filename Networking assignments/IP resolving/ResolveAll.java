import java.io.*;
import java.net.InetAddress;

public class ResolveAll{
	
	public static void main (String[] args)
	{
		

		try
		{

			for(int i = 0; i < args.length; i++)
			{
				// read in host names from console
				String hostName = args[i];
				// solve all ip addresses 
				InetAddress[] IP = InetAddress.getAllByName(hostName);
				int f =0;
					for(int b =0; b < IP.length; b++)
					{
					// printout all address for each name
					System.out.println(hostName + ": " + IP[b].getHostAddress());
					}
				
				
				
			}
		}
		catch (Exception e)
		{
		System.out.println(e.getMessage());
		}
	}


}
