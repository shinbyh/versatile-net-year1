package kr.ac.kaist.cds.net;

import java.util.StringTokenizer;

/**
 * A message for neighbor discovery
 * 
 * @author Byoungheon Shin (bhshin@kaist.ac.kr)
 *
 */
public class NDMessage {
	public static final String REQUEST = "NDRequest";
	public static final String REPLY = "NDReply";
	
	private String type;
	private int seqNo;
	
	public NDMessage(String type, int seqNo) {
		this.type = type;
		this.seqNo = seqNo;
	}
	
	public static NDMessage parse(String str){
		StringTokenizer st = new StringTokenizer(str, "/");
		String type = st.nextToken(); // message type
		int seqNo = Integer.parseInt(st.nextToken()); // sequence number
		
		NDMessage msg = new NDMessage(type, seqNo);
		return msg;
	}
	
	public String serialize(){
		StringBuilder builder = new StringBuilder();
		
		// append member variables
		builder.append(this.type);
		builder.append("/");
		builder.append(this.seqNo);
		
		return builder.toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	
}
