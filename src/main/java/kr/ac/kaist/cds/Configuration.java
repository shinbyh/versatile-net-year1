package kr.ac.kaist.cds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import kr.ac.kaist.cds.debug.DebugType;
import kr.ac.kaist.cds.net.NetworkInterfaceType;

public class Configuration {
	public static final String CONFIG_PATH = "qos.config";
	public static final String DEFAULT_RESOLVER_ADDR = "http://147.46.216.250:3333/";
	public static final int HELLO_PORT = 30999;
	public static final int NEIGHBOR_DISCOVER_PERIOD = 1500;
	//public static final String DEFAULT_RESOLVER_ADDR = "http://localhost:8080/qos/";
	
	private static Configuration _instance = new Configuration(CONFIG_PATH);
	private DebugType debugType = null;
	
	private Hashtable<String, String> confMap;
	private HashMap<String, Vector<String>> interfaceMap;
	
	public static Configuration getInstance() {
		return _instance;
	}
	
	private Configuration(String filePath){
		this.confMap = new Hashtable<String, String>();
		this.interfaceMap = new HashMap<String, Vector<String>>();
		loadConfigFromFile(filePath);
	}
	
	private void loadConfigFromFile(String filePath){
		FileReader fr = null;
		BufferedReader br = null;
		try {
			String line;
			int idx;
			String key, value;
			fr = new FileReader(new File(filePath));
			br = new BufferedReader(fr);
			
			// parse line by line
			while( (line = br.readLine()) != null){
				line = line.trim();
				if(line.trim().startsWith("#") || line.trim().startsWith("//") || line.equals("") || !line.contains("=")){
					continue;
				}
				idx = line.indexOf("=");
				key = line.substring(0, idx).trim();
				value = line.substring(idx + 1).trim();

				// add all configurations to confMap
				System.out.println("[Conf] loading configuration: " + key + " = " + value);
				this.confMap.put(key, value);
				
				// handling special settings
				if(key.equals("DebugType")){
					this.debugType = DebugType.valueOf(value);
				}
				
				// handling special settings
				if(key.equals("Interfaces")){
					String[] ifTypeAndName = value.split("/");
					if(this.interfaceMap.containsKey(ifTypeAndName[0])){
						this.interfaceMap.get(ifTypeAndName[0]).add(ifTypeAndName[1]);
					} else {
						Vector<String> vector = new Vector<String>();
						vector.add(ifTypeAndName[1]);
						this.interfaceMap.put(ifTypeAndName[0], vector);
					}
				}
			}
			
			// Check undefined configurations.
			// If undefined, set default values.
			if( !this.confMap.containsKey("ResolverRESTAddr") ){
				this.confMap.put("ResolverRESTAddr", DEFAULT_RESOLVER_ADDR);
			}
			
			if( this.debugType == null){
				this.debugType = DebugType.INFO;
			}
			
			fr.close();
			br.close();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			System.err.println("[Config] Config file not found. Loading defaults...\n");
			loadDefaults();
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("[Conig] Error reading config file. Loading defaults...\n");
			loadDefaults();
		}
	}
	
	private void loadDefaults(){
		System.out.println("[Config] ResolverRESTAddr = " + DEFAULT_RESOLVER_ADDR);
		this.confMap.put("ResolverRESTAddr", DEFAULT_RESOLVER_ADDR);
		
		System.out.println("[Config] DebugType = " + DebugType.INFO.toString());
		this.confMap.put("DebugType", DebugType.INFO.toString());
		this.debugType = DebugType.INFO;
	}
	
	public Vector<String> getInterfaces(NetworkInterfaceType type){
		return this.interfaceMap.get(type.text);
	}
	
	public String getResolverRESTAddr(){
		return this.confMap.get("ResolverRESTAddr");
	}
	
	public DebugType getDebugType(){
		return this.debugType;
	}
	
	public int getHelloPort(){
		return Integer.parseInt(this.confMap.get("HelloPort"));
	}
	
	public String get(String key) {
		String value = this.confMap.get(key);
		
		if(value == null){
			return null;
		}
		else {
			return (this.confMap.get(key));
		}
	}
}
