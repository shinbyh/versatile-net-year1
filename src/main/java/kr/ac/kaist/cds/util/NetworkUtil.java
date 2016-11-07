package kr.ac.kaist.cds.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetworkUtil {
	
	public static boolean isMyIP(InetAddress addr) throws SocketException {
		Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
		while( ifaces.hasMoreElements() ){
			NetworkInterface iface = ifaces.nextElement();
			Enumeration<InetAddress> addresses = iface.getInetAddresses();

			while( addresses.hasMoreElements() ){
				InetAddress temp = addresses.nextElement();
				if(temp.equals(addr)) return true;
			}
		}

		return false;
	}
	
	public static boolean isMyIP(String addr){
		try {
			InetAddress inetAddr = InetAddress.getByName(addr);
			return isMyIP(inetAddr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
