package it.alessandrochillemi.tesi.FrameUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

public abstract class ResponseLog implements Serializable{
	
	protected String frameID;									//ID del Frame a cui si riferisce questa risposta
	protected Integer responseCode;								//Codice di risposta della richiesta HTTP
	protected String responseMessage;							//Messaggio di risposta della richiesta HTTP
//	protected String responseBody;								//Body della risposta alla richiesta HTTP
	protected ArrayList<Param> paramList;						//Lista di parametri usati nella richiesta
	
	private static final long serialVersionUID = 7679179561832569179L;
	
	public ResponseLog(){
		this.paramList = new ArrayList<Param>();
	}

	public ResponseLog(String frameID, Integer responseCode, String responseMessage, /*String responseBody,*/ ArrayList<Param> paramList) {
		this.frameID = frameID;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
//		this.responseBody = responseBody;
		this.paramList = paramList;
	}
	
	public ResponseLog(CSVRecord record){
		this.paramList = new ArrayList<Param>();
		readFromCSVRow(record);
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
//	public String getResponseBody() {
//		return responseBody;
//	}
//	public void setResponseBody(String responseBody) {
//		this.responseBody = responseBody;
//	}
	public ArrayList<Param> getParamList() {
		return paramList;
	}
	public void setParamList(ArrayList<Param> paramList) {
		this.paramList = paramList;
	}
	
	public void print(){
		System.out.println("FRAME ID: " + frameID);
		System.out.println("RESPONSE CODE: " + responseCode);
		System.out.println("RESPONSE MESSAGE: " + responseMessage);
//		System.out.println("RESPONSE BODY: " + responseBody);
		System.out.println("PARAMETERS: ");
		for(int i=0; i<paramList.size(); i++){
			System.out.print("\nPARAMETER " + (i+1) + ":");
			paramList.get(i).print();
		}
	}
	
	/*	Carica i campi del ResponseLog da una riga di un file CSV; questo metodo dipende dall'implementazione del ResponseLog (ad esempio DiscourseResponseLog, ecc.),
	 *	perché i campi del CSV vanno letti usando l'EquivalenceClass e il TypeParam appropriati.
	 */ 
	public abstract void readFromCSVRow(CSVRecord record);
	
//	Scrive i campi del ResponseLog su una riga di un file CSV
	public void writeToCSVRow(CSVPrinter csvPrinter){
		String frameID = getFrameID();
		int responseCode = getResponseCode();
		String responseMessage = getResponseMessage();

		ArrayList<String> paramKeys = new ArrayList<String>();
		ArrayList<String> paramTypes = new ArrayList<String>();
		ArrayList<String> paramClasses = new ArrayList<String>();
		ArrayList<String> paramPositions = new ArrayList<String>();
		ArrayList<String> paramResourceTypes = new ArrayList<String>();
		ArrayList<String> paramIsRequireds = new ArrayList<String>();
		ArrayList<String> paramValidValues = new ArrayList<String>();

		for(int i=0;i<6;i++){
			if(i<getParamList().size()){
				paramKeys.add(getParamList().get(i).getKeyParam());
				paramTypes.add(getParamList().get(i).getTypeParam().toString());
				paramClasses.add(getParamList().get(i).getClassParam().toString());
				paramPositions.add(getParamList().get(i).getPosition().toString());
				paramResourceTypes.add(getParamList().get(i).getResourceType().toString());
				paramIsRequireds.add(String.valueOf(getParamList().get(i).isRequired()));
				if(getParamList().get(i).getValidValues().size()>0){
					paramValidValues.add(StringUtils.join(getParamList().get(i).getValidValues(),","));
				}
				else{
					paramValidValues.add("/");
				}
			}
			else{
				paramKeys.add("/");
				paramTypes.add("/");
				paramClasses.add("/");
				paramPositions.add("/");
				paramResourceTypes.add("/");
				paramIsRequireds.add("/");
				paramValidValues.add("/");
			}
		}

		try {
			csvPrinter.printRecord(frameID, responseCode,responseMessage,
					paramKeys.get(0), paramTypes.get(0), paramClasses.get(0), paramPositions.get(0), paramResourceTypes.get(0), paramIsRequireds.get(0), paramValidValues.get(0),
					paramKeys.get(1), paramTypes.get(1), paramClasses.get(1), paramPositions.get(1), paramResourceTypes.get(1), paramIsRequireds.get(1), paramValidValues.get(1),
					paramKeys.get(2), paramTypes.get(2), paramClasses.get(2), paramPositions.get(2), paramResourceTypes.get(2), paramIsRequireds.get(2), paramValidValues.get(2),
					paramKeys.get(3), paramTypes.get(3), paramClasses.get(3), paramPositions.get(3), paramResourceTypes.get(3), paramIsRequireds.get(3), paramValidValues.get(3),
					paramKeys.get(4), paramTypes.get(4), paramClasses.get(4), paramPositions.get(4), paramResourceTypes.get(4), paramIsRequireds.get(4), paramValidValues.get(4),
					paramKeys.get(5), paramTypes.get(5), paramClasses.get(5), paramPositions.get(5), paramResourceTypes.get(5), paramIsRequireds.get(5), paramValidValues.get(5)
					);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
