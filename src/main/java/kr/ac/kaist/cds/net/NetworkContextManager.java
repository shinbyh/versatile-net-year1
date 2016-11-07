package kr.ac.kaist.cds.net;

import java.util.ArrayList;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;

import kr.ac.kaist.cds.Configuration;
import kr.ac.kaist.cds.util.JSONUtil;

public class NetworkContextManager {
	
	public NetworkContextManager(){
		
	}

	/**
	 * Get available bandwidth of a specific interface
	 * in bits per second (bps).
	 * 
	 * @param iface An interface name
	 * @return bps
	 */
	public double getAvailableBWInBps(String iface){
		double bw = 2000000.0;
		return bw;
	}
	
	private JSONArray getWifiNeighbors(){
		JSONArray jsonArr = new JSONArray();
		
		try {
			Vector<String> ifaces = Configuration.getInstance().getInterfaces(NetworkInterfaceType.WIFI);
			
			for(String iface : ifaces){
				ArrayList<String> addrs = NDClient.discoverNeighbors(iface);
				for(String ipv4 : addrs){
					//System.out.println(iface + ": " +ipv4);
					
					Neighbor neighbor = new Neighbor(iface, ipv4, null);
					jsonArr.put(neighbor.toJSONObject());
				}
			}
			
			//debug
			System.out.println(jsonArr.toString());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return jsonArr;
	}
	
	private JSONArray getBluetoothNeighbors() throws InterruptedException{
		JSONArray jsonArr = new JSONArray();
		
		BluetoothDiscoverer btdisc = new BluetoothDiscoverer();
		ArrayList<String> hwAddrs = btdisc.discoverBTDevices();
		
		for(String hwAddr : hwAddrs){
			Neighbor neighbor = new Neighbor(NetworkInterfaceType.BLUETOOTH.text, null, hwAddr);
			jsonArr.put(neighbor.toJSONObject());
		}
		
		//debug
		System.out.println("BT neighbors = " + jsonArr.toString());
		
		return jsonArr;
	}
	
	public JSONArray discoverNeighbors(){
		JSONArray arr = new JSONArray();
		
		try {
			JSONArray arrWifi = getWifiNeighbors();
			JSONArray arrBT = getBluetoothNeighbors();
			
			arr = JSONUtil.accumulateJSONArrays(arr, arrWifi);
			arr = JSONUtil.accumulateJSONArrays(arr, arrBT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return arr;
	}
	
	
}
