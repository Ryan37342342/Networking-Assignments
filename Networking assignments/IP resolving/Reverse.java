import java.io.*;
import java.net.InetAddress;

public class Reverse {
	
	public static void main (String[] args)
	{
		
		try
		{
			// while something is in args
			for (int i = 0 ; i < args.length; i++)
			{
				// get the ip
				String hostName = args[i];
				// get the host name
				InetAddress IP = InetAddress.getByName(hostName);
				// print out host name and ip
				
				// if they are the same
				if(IP.getHostName().equals(IP.getHostAddress()))
				{
				System.out.println(IP.getHostName() +": " + "Address not found");
				}	
				else
				{				
				System.out.println(IP.getHostName() + ": " + IP.getHostAddress());
				}				
				 
				
			}
		}
		catch (Exception e)
		{
		System.out.println(e.getMessage());
		}
	}


}
