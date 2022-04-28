import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.naming.ldap.*;
import java.net.*;
import java.io.*;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class MyTLSFileServer 
{
 	
	public static void main (String[] args)
	{
		try
		{	
					
			// get port to bind to from the command line
			int port = Integer.parseInt(args[0]);
			//create keystore
			KeyStore ks = KeyStore.getInstance("JKS");
			//store enabled Protocols
			String[] enabledProtocols ={"TLSv1.2","TLSv1.1"};
			//store password for JKS file
			char[] password = "fatboy".toCharArray();
			
			//while(true)
			//{
			
				System.out.println("Waiting for Connection on " + args[0]);	
				// load the keystore file
				ks.load(new FileInputStream("server.jks"),password);
			
				// create a key manager to manage the keys
				KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				//start the key manager
				kmf.init(ks,password);
				//
				SSLContext ctx =SSLContext.getInstance("TLS");
				//
				ctx.init(kmf.getKeyManagers(),null,null);
			
				//create a server socket factory
				SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
				//create a sslserver socket 
				SSLServerSocket sslServerSocket = (SSLServerSocket)ssf.createServerSocket(port);
				//set protocols for the socket
				sslServerSocket.setEnabledProtocols(enabledProtocols);
				//blocking
				SSLSocket sClient = (SSLSocket)sslServerSocket.accept();
				System.out.println("Server Connected");
				
				// recicece a file name 
				InputStream input = sClient.getInputStream();
				InputStreamReader inputReader = new InputStreamReader(input); 
				BufferedReader bufReader= new BufferedReader(inputReader);
				String fileName = bufReader.readLine();
				System.out.println(fileName);
				
				// if file exists
				File f = new File(fileName);
				if(f.exists())
				{
					//send file
					//create a file input stream
					FileInputStream fis = new FileInputStream(fileName);
					// create outputstream 
					BufferedOutputStream bos = new BufferedOutputStream(sClient.getOutputStream());
					byte[] buf = new byte [1024];
					int rc = fis.read(buf);
					
					while(rc!=-1)
					{

						bos.write(buf,0,rc);
						bos.flush();
						rc =fis.read(buf);
					}
					bos.close();
					fis.close();
					sClient.close();
					

				}
				else
				{
					//else the file doesnt exist
					System.out.println("File name not found");
					sClient.close();
				}

			//}	
				
			

		}
		catch(Exception e)
		{
		e.printStackTrace();
		}











	}


}
