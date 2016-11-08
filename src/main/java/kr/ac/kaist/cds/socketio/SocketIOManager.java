package kr.ac.kaist.cds.socketio;

import java.net.URISyntaxException;

import kr.ac.kaist.cds.Configuration;
import kr.ac.kaist.cds.net.NDServer;
import kr.ac.kaist.cds.net.NetworkContextManager;
import kr.ac.kaist.cds.net.NetworkInterface;
import kr.ac.kaist.cds.net.NetworkInterfaceManager;
import kr.ac.kaist.cds.qos.QoSInterpreter;

import org.json.JSONArray;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIOManager {
	Socket socket;
	
	public SocketIOManager(String uri){
		try {
			socket = IO.socket(uri);
			setupListeners(socket);
			socket.connect();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Setup custom listeners for SocketIO
	 * 
	 * @param socket a SocketIO socket instance
	 */
	private void setupListeners(final Socket socket){
		/*
		 * CONNECT
		 */
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				System.out.println("[SocketIO] connecting...");
				//socket.emit("my event", "hihihi (my event data)");
			}
		});
		
		/*
		 * DISCONNECT
		 */
		socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("## event disconnect!!");
			}

		});
		
		/*
		 * MAtchingRequest from Resolver (SNU)
		 */
		socket.on("MatchingRequest", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("[SocketIOMgr] matching request received");
				for(int i=0; i<args.length; i++){
					System.out.println("  args[" + i +"]= " +args[i]);
				}
				
				// TODO: process received messages (JSONObject)
			}
		});
		
		/*
		 * General Events
		 */
		socket.on("event", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("[SocketIOMgr] event type");
				
				for(int i=0; i<args.length; i++){
					System.out.println("  args[" + i +"]= " +args[i]);
				}
				//JSONArray arr = (JSONArray)args[0];
				
				// TODO: process received messages (JSONObject)
			}
		});
	}
	
	public void send(String event, String msg){
		socket.emit(event, msg);
	}
	
	public void join(JSONObject joinJson){
		send("Join", joinJson.toString());
	}
	
	public void update(JSONObject upJson){
		send("Update", upJson.toString());
	}
	
	public void request(JSONObject reqJson){
		send("Request", reqJson.toString());
	}
	
	/**
	 * Testcode for SocketIO
	 * @param args
	 */
	public static void main(String[] args){
		String serverAddr = Configuration.getInstance().get("SocketIOAddr");
		SocketIOManager mgr = new SocketIOManager(serverAddr);
		
		// neighbor discovery server start
		NDServer server = NDServer.getInstance();
		Thread ndServerThread = new Thread(server);
		ndServerThread.start();
		
		// uniqueCodes
		JSONObject joinJSONObj = new JSONObject();
		NetworkInterfaceManager nim = NetworkInterfaceManager.getInstance();
		joinJSONObj.put("uniqueCodes", nim.getUniqueCodes());
		
		// relay
		if(Configuration.getInstance().get("UseRelay").equals("no")){
			joinJSONObj.put("relay", "none");
		}
		
		// neighbors
		NetworkContextManager ncm = new NetworkContextManager();
		joinJSONObj.put("neighbors", ncm.discoverNeighbors());
		
		// debug
		System.out.println(joinJSONObj.toString());
		
		// join the Resolver network.
		mgr.join(joinJSONObj);
		
	}
}
