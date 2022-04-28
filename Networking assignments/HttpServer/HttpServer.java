import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.net.*;
import java.lang.*;

class HttpServer
{
// create a thread pool of size ten 
public static final ExecutorService exec = Executors.newFixedThreadPool(10);
	
public static void main (String[] args)
	{ 
	
	
		try
		{
		//create a server socket bound to 8080	
		ServerSocket serverSocket = new ServerSocket(7645);
			
			while(true){
			System.out.println("web server starting");
			
			
			//blocking
			Socket client =  serverSocket.accept();
			//testing line
			System.out.println("Connected to :"+ client.getLocalAddress());
			//create a HttpServerSession called task that runs on an thread
			HttpServerSession task = new HttpServerSession(client);
			// assign task to a thread and run it 
			exec.execute(task);
			//close the client
			System.out.println(" ");
			
			
			}
		}
		
		catch(Exception e){
		System.out.println("Exception: " + e);
		}
	}


}

///the class for HttpServerSession/////////////////////////////////////////////////////////////
class HttpServerSession implements Runnable
{
	// the socket that the server has accepted from the client
	Socket client;
	// first line read in	
	String line1;

	public HttpServerSession(Socket s)
	{
	// take the client socket from the server and store it 
	client = s;
	}


///////// writeln method//////////////////////////////////////////////////////////////////////////////////////
private static void writeln(BufferedOutputStream bos, String s) throws IOException
{	
	//put the string to the correct format
	String news = s + "\r\n";
	//convert string to bytes 
	byte[] array = news.getBytes();
	
	for(int i =0; i<array.length; i++)
	// write the string 
	{bos.write(array[i]);}
			
}

///////////////////////////////////////////run method//////////////////////////////////////////////
@Override 
public void run()
{
		try{

	if(client.isClosed() == true)
	{
	 System.out.println("Client is closed");
	}
		


	String fileName;
	BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
	// get the output stream for the client output
	BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
	//read the first line (the request line) from the inputstream
	line1 = reader.readLine();
	System.out.println(line1);	
	//split the request line 
	String[] requestLine = line1.split(" "); 
			
		// should be there parts of the request line
		if (requestLine.length == 3)
		{
			if (requestLine[0].compareTo("GET")== 0)
			{
			//print out the filename of the file being requested	 
			System.out.println(requestLine[1]);
			fileName = requestLine[1].substring(1);
			}
			else
			{
			//ABORT
			return;
			}
		}
		else
		{
		//abort
		return;
		}
	//get the file requested by the server		
	FileInputStream fis = new FileInputStream(fileName);	
	File file= new File(fileName);
	
	if (file.exists() == false){
	//send a 404 error
	HttpServerSession.writeln(bos,"HTTP/1.1 404 Not");
	HttpServerSession.writeln(bos,"");
	HttpServerSession.writeln(bos,"File not found");
	System.out.println("file not found");
	bos.flush();
	bos.close();
	}

	else
	{// now we will send something back 
	// HTTP responce header
	
	HttpServerSession.writeln(bos,"HTTP/1.1 200 OK");
	HttpServerSession.writeln(bos,"");
		

	//create a byte array
	byte[] buf = new byte[1024]; 
	
		
	// read the first 1024 bytes 
	int rc = fis.read(buf);
	//while there is stuff to be read
		while(rc !=-1)
		{
		//write out all the bytes 
		bos.write(buf,0,rc);
		bos.flush();
		// read in the next 1024 bytes 		
		rc = fis.read(buf);
		}
	
	bos.flush();
	bos.close();
	fis.close();
	}
	
	

	
	client.close();
	return;
	}	
	
	catch (Exception e)
	{
	System.out.println("Exception1: " + e);
	return;
	}
}







}

 



