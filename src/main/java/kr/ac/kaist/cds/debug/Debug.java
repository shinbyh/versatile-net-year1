package kr.ac.kaist.cds.debug;

import kr.ac.kaist.cds.Configuration;

public class Debug {
	public static final boolean UTIL_LINUX = true;
	public static final boolean UTIL_JSON = true;
	
	public static void println(DebugType type, String msg){
		if(Configuration.getInstance().getDebugType().ordinal() <= type.ordinal())
			System.out.println(msg);
	}
}
