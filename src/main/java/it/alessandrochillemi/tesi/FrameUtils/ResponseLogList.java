package it.alessandrochillemi.tesi.FrameUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public abstract class ResponseLogList<R extends ResponseLog> {
	private enum header{
		FRAME_ID,RESPONSE_CODE,RESPONSE_MESSAGE,
		P1_KEY,P1_TYPE,P1_CLASS,P1_POSITION,P1_RESOURCE_TYPE,P1_IS_REQUIRED,P1_VALID_VALUES,
		P2_KEY,P2_TYPE,P2_CLASS,P2_POSITION,P2_RESOURCE_TYPE,P2_IS_REQUIRED,P2_VALID_VALUES,
		P3_KEY,P3_TYPE,P3_CLASS,P3_POSITION,P3_RESOURCE_TYPE,P3_IS_REQUIRED,P3_VALID_VALUES,
		P4_KEY,P4_TYPE,P4_CLASS,P4_POSITION,P4_RESOURCE_TYPE,P4_IS_REQUIRED,P4_VALID_VALUES,
		P5_KEY,P5_TYPE,P5_CLASS,P5_POSITION,P5_RESOURCE_TYPE,P5_IS_REQUIRED,P5_VALID_VALUES,
		P6_KEY,P6_TYPE,P6_CLASS,P6_POSITION,P6_RESOURCE_TYPE,P6_IS_REQUIRED,P6_VALID_VALUES;
	};
	
	protected ArrayList<R> responseLogList;
	
	public ResponseLogList(){
		responseLogList = new ArrayList<R>();
	}
	
	public ResponseLogList(String path){
		responseLogList = new ArrayList<R>();
		readFromCSVFile(path);
	}
	
	public int size(){
		return this.responseLogList.size();
	}
	
	public void add(R responseLog){
		this.responseLogList.add(responseLog);
	}
	
	public R get(int index){
		return this.responseLogList.get(index);
	}
	
	public abstract void readFromCSVFile(String path);
	
	public void writeToCSVFile(String path) {
		BufferedWriter writer;
		try {
			writer = Files.newBufferedWriter(Paths.get(path));

			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180.withDelimiter(';').withHeader(header.class));

			Iterator<R> iter = this.responseLogList.iterator();
			while (iter.hasNext()) {
				R responseLog = iter.next();
				responseLog.writeToCSVRow(csvPrinter);
			}

			csvPrinter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Numero di risposte riferite al frame specificato
	public int count(String frameID){
		int count = 0;
		for(R r : this.responseLogList){
			if(r.getFrameID().equals(frameID)){
				count++;
			}
		}
		return count;
	}
	
	//Ritorna il numero totale di fallimenti nella lista
	public abstract int getTotalNumberOfFailures();
	
	//Ritorna il numero totale di fallimenti critici nella lista
	public abstract int getTotalNumberOfCriticalFailures();

	//Ritorna il numero di fallimenti per il frame con Frame ID specificato
	public abstract int getFrameFailures(String frameID);
	
	//Ritorna il numero di fallimenti critici per il frame con Frame ID specificato
	public abstract int getFrameCriticalFailures(String frameID);
	
}
