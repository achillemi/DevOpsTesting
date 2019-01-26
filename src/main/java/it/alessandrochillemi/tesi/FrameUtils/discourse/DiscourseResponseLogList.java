package it.alessandrochillemi.tesi.FrameUtils.discourse;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import it.alessandrochillemi.tesi.FrameUtils.Param.Position;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLogList;

public class DiscourseResponseLogList extends ResponseLogList<DiscourseResponseLog> {
	
	private ArrayList<DiscourseResponseLog> responseLogList;
	
	private enum header{
		FRAME_ID,RESPONSE_CODE,RESPONSE_MESSAGE,
		P1_KEY,P1_TYPE,P1_CLASS,P1_POSITION,P1_RESOURCE_TYPE,P1_IS_REQUIRED,P1_VALID_VALUES,
		P2_KEY,P2_TYPE,P2_CLASS,P2_POSITION,P2_RESOURCE_TYPE,P2_IS_REQUIRED,P2_VALID_VALUES,
		P3_KEY,P3_TYPE,P3_CLASS,P3_POSITION,P3_RESOURCE_TYPE,P3_IS_REQUIRED,P3_VALID_VALUES,
		P4_KEY,P4_TYPE,P4_CLASS,P4_POSITION,P4_RESOURCE_TYPE,P4_IS_REQUIRED,P4_VALID_VALUES,
		P5_KEY,P5_TYPE,P5_CLASS,P5_POSITION,P5_RESOURCE_TYPE,P5_IS_REQUIRED,P5_VALID_VALUES,
		P6_KEY,P6_TYPE,P6_CLASS,P6_POSITION,P6_RESOURCE_TYPE,P6_IS_REQUIRED,P6_VALID_VALUES;
	};
	
	public DiscourseResponseLogList(){
		responseLogList = new ArrayList<DiscourseResponseLog>();
	}
	
	public DiscourseResponseLogList(String path){
		responseLogList = new ArrayList<DiscourseResponseLog>();
		readFromCSVFile(path);
	}
	
	public int size(){
		return this.responseLogList.size();
	}
	
	public void add(DiscourseResponseLog responseLog){
		this.responseLogList.add(responseLog);
	}
	
	public DiscourseResponseLog get(int index){
		return this.responseLogList.get(index);
	}

	//Numero di risposte riferite al frame specificato
	public int count(String frameID){
		int count = 0;
		for(DiscourseResponseLog r : this.responseLogList){
			if(r.getFrameID().equals(frameID)){
				count++;
			}
		}
		return count;
	}

	@Override
	public void readFromCSVFile(String path) {
		if(Files.exists(Paths.get(path))){
			Reader in;
			try {
				//Read the CSV file
				in = new FileReader(path);
				Iterable<CSVRecord> records = CSVFormat.RFC4180.withDelimiter(';').withFirstRecordAsHeader().parse(in);
				for (CSVRecord record : records) {
					//Read Frame ID, Response Code and Response Message
					String frameID = record.get("FRAME_ID");
					int responseCode = Integer.parseInt(record.get("RESPONSE_CODE"));
					String responseMessage = record.get("RESPONSE_MESSAGE");

					//Create a list of DiscourseParam from the values of the row
					ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();

					//Read the features for each of the 6 parameters on a row
					for(int i = 1; i<=6; i++){
						String key = record.get("P"+i+"_KEY");
						String type = record.get("P"+i+"_TYPE");
						String eqClass = record.get("P"+i+"_CLASS");
						String position = record.get("P"+i+"_POSITION");
						String resourceType = record.get("P"+i+"_RESOURCE_TYPE");
						String isRequired = record.get("P"+i+"_IS_REQUIRED");
						String validValues = record.get("P"+i+"_VALID_VALUES");

						//If P_KEY!=null and P_KEY!="/", create a new parameter and add it to the list; otherwise, it means that there are no more parameters
						if(key != null && !key.equals("/")){
							DiscourseTypeParam discourseType = EnumUtils.getEnumIgnoreCase(DiscourseTypeParam.class, type);
							DiscourseEquivalenceClass discourseClass = EnumUtils.getEnumIgnoreCase(DiscourseEquivalenceClass.class, eqClass);
							Position discoursePosition = EnumUtils.getEnumIgnoreCase(Position.class, position);
							DiscourseResourceType discourseResourceType = EnumUtils.getEnumIgnoreCase(DiscourseResourceType.class, resourceType);
							boolean discourseIsRequired = Boolean.parseBoolean(isRequired);
							ArrayList<String> discourseValidValues = new ArrayList<String>();
							if(!validValues.equals("/")){
								discourseValidValues.addAll(Arrays.asList(validValues.split(",")));
							}
							DiscourseParam p = new DiscourseParam(key,discourseType,discoursePosition,discourseClass,discourseResourceType,discourseIsRequired,discourseValidValues);
							paramList.add(p);
						}
					}
					
					//Create a new response log from the record just read
					DiscourseResponseLog discourseResponseLog = new DiscourseResponseLog(frameID,responseCode,responseMessage,paramList);
					
					//Add the frame to the map
					this.responseLogList.add(discourseResponseLog);

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			System.out.println("File does not exist!");
		}
		
	}

	@Override
	public void writeToCSVFile(String path) {
		BufferedWriter writer;
		try {
			writer = Files.newBufferedWriter(Paths.get(path));

			@SuppressWarnings("resource")
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180.withDelimiter(';').withHeader(header.class));

			Iterator<DiscourseResponseLog> iter = this.responseLogList.iterator();
			while (iter.hasNext()) {
				DiscourseResponseLog discourseResponseLog = iter.next();
				String frameID = discourseResponseLog.getFrameID();
				int responseCode = discourseResponseLog.getResponseCode();
				String responseMessage = discourseResponseLog.getResponseMessage();

				ArrayList<String> paramKeys = new ArrayList<String>();
				ArrayList<String> paramTypes = new ArrayList<String>();
				ArrayList<String> paramClasses = new ArrayList<String>();
				ArrayList<String> paramPositions = new ArrayList<String>();
				ArrayList<String> paramResourceTypes = new ArrayList<String>();
				ArrayList<String> paramIsRequireds = new ArrayList<String>();
				ArrayList<String> paramValidValues = new ArrayList<String>();

				for(int i=0;i<6;i++){
					if(i<discourseResponseLog.getParamList().size()){
						paramKeys.add(discourseResponseLog.getParamList().get(i).getKeyParam());
						paramTypes.add(discourseResponseLog.getParamList().get(i).getTypeParam().toString());
						paramClasses.add(discourseResponseLog.getParamList().get(i).getClassParam().toString());
						paramPositions.add(discourseResponseLog.getParamList().get(i).getPosition().toString());
						paramResourceTypes.add(discourseResponseLog.getParamList().get(i).getResourceType().toString());
						paramIsRequireds.add(String.valueOf(discourseResponseLog.getParamList().get(i).isRequired()));
						if(discourseResponseLog.getParamList().get(i).getValidValues().size()>0){
							paramValidValues.add(StringUtils.join(discourseResponseLog.getParamList().get(i).getValidValues(),","));
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

				csvPrinter.printRecord(frameID, responseCode,responseMessage,
						paramKeys.get(0), paramTypes.get(0), paramClasses.get(0), paramPositions.get(0), paramResourceTypes.get(0), paramIsRequireds.get(0), paramValidValues.get(0),
						paramKeys.get(1), paramTypes.get(1), paramClasses.get(1), paramPositions.get(1), paramResourceTypes.get(1), paramIsRequireds.get(1), paramValidValues.get(1),
						paramKeys.get(2), paramTypes.get(2), paramClasses.get(2), paramPositions.get(2), paramResourceTypes.get(2), paramIsRequireds.get(2), paramValidValues.get(2),
						paramKeys.get(3), paramTypes.get(3), paramClasses.get(3), paramPositions.get(3), paramResourceTypes.get(3), paramIsRequireds.get(3), paramValidValues.get(3),
						paramKeys.get(4), paramTypes.get(4), paramClasses.get(4), paramPositions.get(4), paramResourceTypes.get(4), paramIsRequireds.get(4), paramValidValues.get(4),
						paramKeys.get(5), paramTypes.get(5), paramClasses.get(5), paramPositions.get(5), paramResourceTypes.get(5), paramIsRequireds.get(5), paramValidValues.get(5)
						);
			}

			csvPrinter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
