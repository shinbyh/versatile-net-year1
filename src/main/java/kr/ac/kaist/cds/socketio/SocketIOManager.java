package kr.ac.kaist.cds.socketio;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIOManager {
	
	public SocketIOManager(){
		
	}
	
	public void connect(String url){
		try {
			final Socket socket = IO.socket(url);
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
				System.out.println("## call!!");
				socket.emit("foo", "hi");
				socket.emit("my event", "hihihi (my event data)");
			   // socket.disconnect();
				
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
		 * REQUEST REPLY
		 */
		socket.on("request_reply", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("[SocketIOMgr] reply type");
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
	
	public void send(String channel, String msg){
		
	}
}
