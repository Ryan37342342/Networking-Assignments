import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.naming.ldap.*;
import java.net.*;
import java.io.*;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;


class MyTLSFileClient
{
	public static void  displayCert(X509Certificate cert)
	{
		try{		
	
			String name = cert.getSubjectX500Principal().getName();
			
			LdapName ln = new LdapName(name);
			
			for (Rdn rdn : ln.getRdns())
			{
				System.out.println(rdn.getValue());
			}

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String[] args)
	{	
	


		try
		{

			//get host, port and file name
			String host = args[0];
			int port = Integer.parseInt(args[1]);		
			String fileName = args[2];
			System.out.println(fileName);		
			// create a SSLSocket and factory
			SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket ssocket =(SSLSocket)factory.createSocket(host,port);
			// set up host name verification		
			SSLParameters params = new SSLParameters();
			params.setEndpointIdentificationAlgorithm("HTTPS");
			ssocket.setSSLParameters(params);
			
			// start the handshake
			
			ssocket.startHandshake();
			//display X509 certificate
			//SSLSession sesh = ssocket.getSession();
			//X509Certificate cert =(X509Certificate)sesh.getPeerCertificates()[0];
			//displayCert(cert);
			
			//send file name 
			OutputStream output = ssocket.getOutputStream();
			OutputStreamWriter osr = new OutputStreamWriter(output);
			BufferedWriter bufWriter = new BufferedWriter(osr);
			bufWriter.write(fileName +"\n");
			System.out.println("Asking for " +fileName);
			
			// recieve file
			byte[] buf = new byte[1024];
			FileOutputStream fos = new FileOutputStream(fileName);
			BufferedInputStream bis = new BufferedInputStream(ssocket.getInputStream());
			int rc = bis.read(buf);
			
			while(rc!=-1)
			{			
			 	fos.write(buf,0,rc);
				fos.flush();
				rc = bis.read(buf);
			
			}

		}
		catch(Exception ex)
		{
		ex.printStackTrace();
		}


	}












}
