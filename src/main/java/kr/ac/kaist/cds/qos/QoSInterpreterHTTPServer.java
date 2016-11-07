package kr.ac.kaist.cds.qos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * 
 * @author bhshin
 *
 */
public class QoSInterpreterHTTPServer {
	private boolean initiated = true;
	private HttpServer server = null;

	public QoSInterpreterHTTPServer(int port){
		initiateModules();
		
		try {
			this.server = HttpServer.create(new InetSocketAddress(port), 0);
			this.server.createContext("/qos/", getHttpHandler());
			this.server.setExecutor(Executors.newCachedThreadPool());
			this.server.start();
			System.out.println("[QosIntp] HTTP listening on port " + this.server.getAddress().getPort());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public HttpHandler getHttpHandler(){
		return new HttpHandler(){

			@Override
			public void handle(HttpExchange httpExchange) throws IOException {
				System.out.println("[QoSIntp] recv from: " + httpExchange.getRemoteAddress());
				
				if(!initiated){
					sendErrorJSONObject("Server not initialized", httpExchange);
					return;
				}
				
				if(httpExchange.getRequestMethod().equals("POST")){
					BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
					String line = null;
					String queryStr = "";
					while((line = br.readLine()) != null){
						queryStr += line;
					}
					
					try {
						JSONObject queryObj = new JSONObject(queryStr);
						
						// debug
						System.out.println(queryStr);
						
						//TODO: dispatch messages
						// e.g. Bluetooth 노드의 join 요청 메세지
						// e.g. 같은 기기 내의 다른 모듈이 보내는 resource update request 메세지
						// e.g. 같은 기기 내의 다른 모듈이 보내는 service request ?
						
						
						// make reply message
						JSONObject respObj = new JSONObject();
						respObj.put("response", "ok");
						byte[] respOutput = respObj.toString().getBytes();
						httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, respOutput.length);
						httpExchange.getResponseBody().write(respOutput);
						httpExchange.getResponseBody().close();
						
						
					} catch (JSONException e) {
						sendErrorJSONObject("JSONObject parsing error", httpExchange);
						e.printStackTrace();
					}
				}
				else if(httpExchange.getRequestMethod().equals("GET")){
					sendErrorJSONObject("GET request not supported", httpExchange);
				}
			}
		};
	}
	
	private void initiateModules(){		
		this.initiated = true;
	}
	
	private void sendErrorJSONObject(String msg, HttpExchange httpExchange) throws IOException{
		System.err.println("[QoSIntp] " + msg);
		
		// Create a JSONObject
		JSONObject errObj = new JSONObject();
		try{
			errObj.put("errorMsg", msg);
			errObj.put("response", "null");
		}
		catch (JSONException e1){
			e1.printStackTrace();
		}
		
		// Create a response message.
		byte[] output = errObj.toString().getBytes();
		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, output.length);
		httpExchange.getResponseBody().write(output);
		httpExchange.getResponseBody().close();
		
		return;
	}
	
	public static void main(String[] args){
		QoSInterpreterHTTPServer service = new QoSInterpreterHTTPServer(8080);
	}
}
