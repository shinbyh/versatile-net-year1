package kr.ac.kaist.cds.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import kr.ac.kaist.cds.Configuration;
import kr.ac.kaist.cds.util.NetworkUtil;
import kr.ac.kaist.cds.util.TransportUDP;

public class NDServer extends TransportUDP {
	private static NDServer _instance = null;
	private int requestSeqNo;
	private ArrayList<String> neighbors;
	
	public static NDServer getInstance(){
		if(_instance == null){
			_instance = new NDServer(Configuration.HELLO_PORT);
		}
		return _instance;
	}

	private NDServer(int port) {
		super(port);
		
		this.requestSeqNo = 0;
		this.neighbors = new ArrayList<String>();
		
		// debug
		System.out.println(" [NDServer] start...");
	}

	@Override
	public void handleMessage(String recv, InetAddress clientIP, int clientPort, DatagramSocket socket) {		
		if(recv != null){
			try {
				// check if the received msg is from myself and ignore it.
				if( !NetworkUtil.isMyIP(clientIP) ){
					NDMessage msg = NDMessage.parse(recv);
					
					// debug
					System.out.println(" [NDServer] recv from "+ clientIP.getHostAddress() +": " + recv);
					
					if(msg.getType().equals(NDMessage.REQUEST)){
						// send back to the requester with a reply type message
						NDMessage replyMsg = new NDMessage(NDMessage.REPLY, msg.getSeqNo());
						TransportUDP.sendMessage(clientIP, Configuration.HELLO_PORT, replyMsg.serialize());
					}
					else if(msg.getType().equals(NDMessage.REPLY)){
						// check the seqNo with the current server's seqNo.
						// if it is the same, add the sender's IP into neighbors arrayList.
						if(this.requestSeqNo == msg.getSeqNo()){
							String clientIPStr = clientIP.getHostAddress();
							if(!neighbors.contains(clientIPStr)){
								neighbors.add(clientIP.getHostName());
							}
						} else {
							// debug
							System.out.println(" - ignoring old seqNo.");
						}
					}
					else {
						// erorr: invalid/unsupported message type
						System.out.println(" [NDServer] Error: invalid msg type.");
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}

	public int getRequestSeqNo() {
		return requestSeqNo;
	}

	public void setRequestSeqNo(int requestSeqNo) {
		this.requestSeqNo = requestSeqNo;
	}

	public ArrayList<String> getNeighbors() {
		return neighbors;
	}
	
	public void resetNeighbors(){
		this.neighbors.clear();
	}
}
