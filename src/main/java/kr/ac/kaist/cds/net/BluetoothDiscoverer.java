package kr.ac.kaist.cds.net;

import java.util.ArrayList;

public class BluetoothDiscoverer {
	private ArrayList<String> hwAddresses;
	
	public BluetoothDiscoverer(){
		this.hwAddresses = new ArrayList<String>();
	}
	
	public ArrayList<String> discoverBTDevices(){
		this.hwAddresses.clear();
		
		// TODO: hard-code
		// add some hw addresses manually.
		
		this.hwAddresses.add("00:11:22:33:aa:bb");
		this.hwAddresses.add("00:11:22:77:cc:ee");
		
		return hwAddresses;
	}
}
