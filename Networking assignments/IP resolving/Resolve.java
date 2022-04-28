import java.io.*;
import java.net.InetAddress;

public class Resolve {
	
	public static void main (String[] args)
	{
		
		try
		{
			// while there is somethinf in args
			for (int i =0; i < args.length; i++)
			{
				// get the hostname 
				String hostName = args[i];
				// get the IP address 
				InetAddress IP = InetAddress.getByName(hostName);
				// print it out
				System.out.println(hostName + ": " + IP.getHostAddress());
				
			}
		}
		catch (Exception e)
		{
		System.out.println(e.getMessage());
		}
	}


}
