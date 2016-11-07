package kr.ac.kaist.cds.net;

import org.json.JSONException;
import org.json.JSONObject;

public class Neighbor {
	private String iface;
	private String ipv4;
	private String hwAddr;
	
	public Neighbor(String iface, String ipv4, String hwAddr) {
		this.iface = iface;
		this.ipv4 = ipv4;
		this.hwAddr = hwAddr;
	}

	public String getIface() {
		return iface;
	}

	public void setIface(String iface) {
		this.iface = iface;
	}

	public String getIpv4() {
		return ipv4;
	}

	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	public String getHwAddr() {
		return hwAddr;
	}

	public void setHwAddr(String hwAddr) {
		this.hwAddr = hwAddr;
	}
	
	public JSONObject toJSONObject(){
		JSONObject obj = new JSONObject();
		try {
			obj.put("neighborIface", this.iface);
			obj.put("neighborHwAddress", this.hwAddr);
			obj.put("neighborIpv4", this.ipv4);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj;
	}

}
