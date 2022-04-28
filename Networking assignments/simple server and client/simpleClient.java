import java.net.*;
import java.io.*;




public class simpleClient
{
		public static void main (String args[])
		{
		try{
		// host name is the first thing in args
		String hostName = args[0];
		int port = Integer.parseInt(args[1]);
		// look up ip of host
		InetAddress clientIP = InetAddress.getByName(hostName);
		// create a socket for the client
		Socket clientSocket = new Socket(clientIP, port);
		// create a reader to read from the server
		// first create an input stream reader
		InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
		// then create a reader
		BufferedReader reader = new BufferedReader(isr);
		String print  = reader.readLine();

			while (print!=null)
			{
			// read a line into print
			
			// print out whatever is in print
			System.out.println(print);
			print  = reader.readLine();

			}
	
		reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}





		}



}
