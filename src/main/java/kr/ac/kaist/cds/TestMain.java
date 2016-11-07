package kr.ac.kaist.cds;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class TestMain {

	public static void main(String[] args){
		String url;
		if(args.length >= 1){
			url = args[0];
		} else {
			url = "http://143.248.53.143:5000/request";
		}
		
		try {
			final Socket socket = IO.socket(url);
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {
					System.out.println("## call!!");
					socket.emit("foo", "hi");
					socket.emit("my event", "hihihi (my event data)");
				   // socket.disconnect();
					
				}
			}).on("event", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					System.out.println("## event!!");
					for(int i=0; i<args.length; i++){
						System.out.println("  args[" + i +"]= " +args[i]);
					}
					//JSONArray arr = (JSONArray)args[0];
				}

			}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

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
					System.out.println(" @@ Reply from Request!!");
					for(int i=0; i<args.length; i++){
						System.out.println("  args[" + i +"]= " +args[i]);
					}
				}
			});
			
			socket.connect();
			//socket.send(args)
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*String jsonMsg = "{"
				+ "\"deviceID\":\"004\""
				+ "}";
				
		try {
			System.out.println("1111111111");
			String respMsg = JSONUtil.sendPOSTMessage("http://192.168.1.1:8080/qos/", jsonMsg);
			System.out.println("22222222");
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		//String output = LinuxUtil.runCmdAndGetOutput("fdaf");
		//System.out.println(output);
		
		/*ArrayList<String> arr = LinuxUtil.runCmdAndGetOutputAsArray("ifconfig");
		
		for(String str : arr){
			
			if(str.contains("inet addr")){
				System.out.println(str);
				
				StringTokenizer st = new StringTokenizer(str, " :");
				while(st.hasMoreTokens())
					System.out.println(st.nextToken());
				
				break;
				
			}
		}*/
		
		/*Configuration.getInstance();
		NetworkInterfaceManager nim = NetworkInterfaceManager.getInstance();
		QoSInterpreter interpreter = new QoSInterpreter(Configuration.getInstance().getResolverAddr());
		
		// test join
		interpreter.join(nim.getNetworkInterfaces());
		
		// test leave
		interpreter.leave("6");
		
		// test update
		//interpreter.update("004", services, resources, contents);
		
		// test request
		ArrayList<QoSRequirement> qosReqs = new ArrayList<QoSRequirement> ();
		qosReqs.add(new QoSRequirement("bandwidth", "bps", 200000.0, Operator.GREATER_OR_EQUAL));
		qosReqs.add(new QoSRequirement("delay", "ms", 100.0, Operator.LESS_OR_EQUAL));
		//interpreter.request("002", ServiceType.STREAMING, qosReqs, "A, B, C", null);
		*/
	}
}
