import java.io.*;
import java.net.*;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class TftpClient
{
	
	//read request packet 
	public static final byte RRQ = 1;
	//data packet
	public static final byte DATA = 2;
	//ack packet
	public static final byte ACK = 3;
	//error packet
	public static final byte ERROR = 4;

	public static void main(String[] args)
	{
	
	try
		{	
		int port = 8080;
		String filename;
		//create a datagram socket 
		DatagramSocket clientSocket = new DatagramSocket();
		// create a byte array
		byte[] buf = new byte[1427];
		// get filename		
			if (args[0] != "")
			{//get file name from args 
			filename = args[0];
			System.out.println(filename);
			
			}
			else
			{
			System.out.println("please enter a filename");
			return;
			}
		// turn file name into data with a RRQ header		
		byte[] dataFile = new byte[1472];
		dataFile = filename.getBytes();
		int dataLength = dataFile.length + 1;
		//create a byte buffer		
		ByteBuffer byteBuffer = ByteBuffer.allocate(dataLength); 
		byteBuffer.put(RRQ);
		byteBuffer.put(dataFile);
		//get inetaddress
		InetAddress serverIP = InetAddress.getByName("127.0.0.0");
	
		//create a data packet to send to the server	
		DatagramPacket dpSend = new DatagramPacket (byteBuffer.array(),dataLength,serverIP,8080);
		// send the datapacket		
		clientSocket.send(dpSend);
		//create datapacket to receive data
	
		DatagramPacket dpReceive = new DatagramPacket(buf,514);
		clientSocket.receive(dpReceive);
		//check packet type 
		byte type =  TftpUtil.checkPacketType(dpReceive);
		byte[] data = dpReceive.getData();
		
		
		if (type == ERROR)
		{

		TftpUtil.printErrorString(dpReceive);
		}

		else if (type == DATA)
		{	
			
			byte lastBlockNum =0;
			byte blockNum;
			
			int recLength;
			SocketAddress ackA = dpReceive.getSocketAddress();
			
			//create the file requested
			File file = new File(filename);	
			// create the file writer for the data			
			FileOutputStream fos = new FileOutputStream(file);		

			while(true)	
			{
			//get the block number and length
			blockNum = TftpUtil.extractBlockSeq(dpReceive);
			
	
			recLength = dpReceive.getLength();
			System.out.println("Recieved block :" + blockNum + " length of " + recLength);
			ByteBuffer byteBufferAck1 = ByteBuffer.allocate(2);
		
			//check block number 
				
				// if  this data packet has aready been received resend ACK 
				if(blockNum == lastBlockNum)
				{
					blockNum = lastBlockNum;
					System.out.println("Resending: " + blockNum);	
				}
				
				//check data length
				// if the data packet recieved is empty				
				if(recLength -2 == 0 )
				{ 
				// then all data has been send 
				System.out.println("done");
				break;
				}
				// if data length is less than 512
				if ((recLength -2) < 512)
				{
				// write data to file 
				fos.write(data,2,recLength-2);
				fos.flush();
				
				byteBufferAck1.put(ACK);	
				byteBufferAck1.put(blockNum);
				
				// create a new datagram packet for ack 
				DatagramPacket dpAck = new DatagramPacket(byteBufferAck1.array(),2,ackA);	
				System.out.println("Sent ACK: " + blockNum);
				clientSocket.send(dpAck);		
				System.out.println("done1");
				break;
				}
				// write the data to a file 
				fos.write(data,2,recLength-2);
				fos.flush();
				//update last received block number 
				lastBlockNum = blockNum;

				// create a bytebuffer with the ack code and block number
				
				byteBufferAck1.put(ACK);	
				byteBufferAck1.put(blockNum);
			
			
				DatagramPacket dpAck = new DatagramPacket(byteBufferAck1.array(),2,ackA);	
				System.out.println(" Sent ACK: " + lastBlockNum);
				clientSocket.send(dpAck);
						
				// receive new data packet 
				System.out.println(" waiting for Block:" + (blockNum+1));
				clientSocket.receive(dpReceive);
				
							
			}
		fos.flush();
		fos.close();	
				
		}
		




		
	
		clientSocket.close();
		System.out.println("File has been recived");
		
		}
	
	catch(Exception e)
		{
		e.printStackTrace();
		}
	


 }

}
