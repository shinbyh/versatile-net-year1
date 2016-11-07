package kr.ac.kaist.cds.net;

import java.util.ArrayList;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;

import kr.ac.kaist.cds.Configuration;
import kr.ac.kaist.cds.util.JSONUtil;

public class NDTestMain {

	public static void main(String[] args) throws InterruptedException, JSONException{
		Configuration.getInstance();
		
		NDServer server = NDServer.getInstance();
		Thread ndServerThread = new Thread(server);
		ndServerThread.start();
		
		NetworkContextManager ncm = new NetworkContextManager();
		JSONArray arr1 = ncm.discoverNeighbors();
		System.out.println(arr1);
	}
}
