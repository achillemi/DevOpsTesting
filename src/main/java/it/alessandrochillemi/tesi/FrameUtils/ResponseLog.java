package it.alessandrochillemi.tesi.FrameUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseLog<P extends Param<? extends PreCondition>> implements Serializable{
	
	private String frameID;									//ID del Frame a cui si riferisce questa risposta
	private Integer responseCode;							//Codice di risposta della richiesta HTTP
	private String responseMessage;							//Messaggio di risposta della richiesta HTTP
	private String responseBody;							//Body della risposta alla richiesta HTTP
	private ArrayList<P> paramList;							//Lista di parametri usati nella richiesta
	
	private static final long serialVersionUID = 7679179561832569179L;

	public ResponseLog(String frameID, Integer responseCode, String responseMessage, String responseBody, ArrayList<P> paramList) {
		super();
		this.frameID = frameID;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.responseBody = responseBody;
		this.paramList = paramList;
	}
	
	public String getFrameID() {
		return frameID;
	}
	public void setFrameID(String frameID) {
		this.frameID = frameID;
	}
	public Integer getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	public ArrayList<P> getParamList() {
		return paramList;
	}
	public void setParamList(ArrayList<P> paramList) {
		this.paramList = paramList;
	}
	
	public void print(){
		System.out.println("FRAME ID: " + frameID);
		System.out.println("RESPONSE CODE: " + responseCode);
		System.out.println("RESPONSE MESSAGE: " + responseMessage);
//		System.out.println("RESPONSE BODY: " + responseBody);
		System.out.println("PARAMETERS: ");
//		for(Param<T> p : paramList){
//			p.print();
//			System.out.println();
//		}
	}

}
