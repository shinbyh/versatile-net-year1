package kr.ac.kaist.cds.net;

import java.util.ArrayList;
import kr.ac.kaist.cds.Configuration;
import kr.ac.kaist.cds.util.TransportUDP;

public class NDClient {
	public static ArrayList<String> discoverNeighbors(String iface) throws InterruptedException {
		NDServer server = NDServer.getInstance();
		server.resetNeighbors();
		
		NetworkInterface ni = NetworkInterfaceManager.getInstance().getNetworkInterface(iface);
		if(ni == null){
			System.err.println(" [NDClient] Error: no such network interface: " + iface);
			return server.getNeighbors();
		}
		
		String bcastAddr = ni.getIpv4Bcast();
		if(bcastAddr == null){
			System.err.println(" [NDClient] Error: broadcast addr is null. Check the interface name: " + iface);
			return server.getNeighbors();
		}
		
		int seqNo = server.getRequestSeqNo();
		server.setRequestSeqNo(seqNo + 1);
		NDMessage ndReq = new NDMessage(NDMessage.REQUEST, seqNo + 1);
		
		//debug
		System.out.println(" [NDClient] broadcast to " + bcastAddr + " ("+ iface + ")");
		
		TransportUDP.sendMessage(bcastAddr, Configuration.HELLO_PORT, ndReq.serialize());
		
		// wait for a while until reply messages arrive.
		Thread.sleep(1500);
		
		return server.getNeighbors();
	}
}
