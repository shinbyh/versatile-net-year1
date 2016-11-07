package kr.ac.kaist.cds.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * TransportUDP manages a common UDP socket for communicating with
 * other objects. This runs as a Thread.
 * 
 * @author Byoungheon Shin (bhshin@kaist.ac.kr)
 *
 */
public abstract class TransportUDP implements Runnable {
	public static final boolean DEBUG_TRANSPORT = false;
	
	private DatagramSocket serverSocket;
	private int serverPort;
	
	/**
	 * Constructs the Transport with a specific port and a handler.
	 * The handler should implement the MessageHandler interface.
	 * 
	 * @param port A port number
	 * @param handler A message handler for the received String
	 */
	public TransportUDP(int port){
		this.serverPort = port;
	}
	
	/**
	 * This method is used as a callback method after packet reception.
	 * 
	 * @param recv The received data
	 * @param clientIP  A client IP address
	 * @param clientPort A client port
	 * @param socket the Socket where the packet arrived
	 */
	public abstract void handleMessage(String recv, InetAddress clientIP, int clientPort, DatagramSocket socket);
	
	/**
	 * Starts a server using ServerSocket to accept SIBMessages.
	 * 
	 * @param port a port number
	 */
	private void startServer(){
		int bufferLen = 1024;
		byte[] buffer;
		String inputLine;
		
		try {
			serverSocket = new DatagramSocket(this.serverPort);
			
			if(DEBUG_TRANSPORT){
				System.out.println("  [TransportUDP] socket initialized (port=" + this.serverPort + ")");
			}
			
			while(true){
				buffer = new byte[bufferLen];
				DatagramPacket receivePacket = new DatagramPacket(buffer, bufferLen);
				serverSocket.receive(receivePacket);
				
				int actualLen = receivePacket.getLength();
				
				byte[] actualData = new byte[actualLen];
				System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), actualData, 0, actualLen);
				inputLine = new String(actualData);
				
				if(DEBUG_TRANSPORT){
					System.out.println("  [TransportUDP] recv from "+ receivePacket.getAddress() +"\n - " + inputLine);
				}
				
				handleMessage(inputLine, receivePacket.getAddress(), receivePacket.getPort(), serverSocket);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getServerPort(){
		return this.serverPort;
	}
	
	/**
	 * Sends a String message to a specific IP address and a port number.
	 * 
	 * @param ip An IPv4 address (String)
	 * @param port A port number
	 * @param msg A message to send (String)
	 */
	public static void sendMessage(String ip, int port, String msg){
		try {
			sendMessage(InetAddress.getByName(ip), port, msg);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a String message to a specific IP address and a port number.
	 * 
	 * @param ip An IPv4 address (InetAddress)
	 * @param port A port number
	 * @param msg A message to send (String)
	 */
	public static void sendMessage(InetAddress ip, int port, String msg){
		byte[] sendData = new byte[1024];
		
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			sendData = msg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
			clientSocket.send(sendPacket);
			
			clientSocket.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a message to the host and gets a reply by waiting for a certain timeout period.
	 * If the waiting time exceeds the timeout, it throws a SocketTimeoutException.
	 * 
	 * @param ip An IPv4 address of a target host
	 * @param port A port number
	 * @param msg A message to send
	 * @param timeout A timeout in milliseconds
	 * @return A reply message in String
	 * @throws SocketTimeoutException
	 */
	public static String sendMessageAndGetReply(String ip, int port, String msg, int timeout) throws SocketTimeoutException {
		String reply = null;
		byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
		
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			sendData = msg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
			
			clientSocket.send(sendPacket);
			
			DatagramPacket recvPacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.setSoTimeout(timeout);
			clientSocket.receive(recvPacket);
			reply = new String(recvPacket.getData());
			clientSocket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if(e instanceof SocketTimeoutException){
				throw new SocketTimeoutException();
			}
			e.printStackTrace();
		}
		
		return reply;
	}

	@Override
	public void run() {
		startServer();
	}
	
}
