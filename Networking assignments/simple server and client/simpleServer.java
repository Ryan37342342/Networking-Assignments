import java.net.*;
import java.io.*;


public class simpleServer
{
	public static void main (String[] args)
	{
	
		try
		{		
			ServerSocket socketServer = new ServerSocket(0);
			System.out.println("Port Nukmber"+ socketServer.getLocalPort());

			while(true)
			{
			System.out.println("Idle.....");
			// accept a connection
			Socket client = socketServer.accept();
			// get the ip adress of the client from the socket
			InetAddress clientIP = (client.getInetAddress());

			
			System.out.println("connected to : " + clientIP);
			// get the DNS of the client
			String clientName = clientIP.getHostName();

			//create a buffered writer to write to the client 
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream())); 
			// write the clients address to the client
			writer.write("Hello " + clientName);
			// close the writer
			writer.flush();
			client.close();
			}
		}

		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}


	}



}
