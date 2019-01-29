package it.alessandrochillemi.tesi.FrameUtils.discourse;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.EnumUtils;

import it.alessandrochillemi.tesi.FrameUtils.Param;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.Param.Position;

public class DiscourseResponseLog extends ResponseLog {
	
	private static final long serialVersionUID = -7764224813306420581L;
	
	public DiscourseResponseLog(){
		super();
	}

	public DiscourseResponseLog(String frameID, Integer responseCode, String responseMessage, /*String responseBody,*/ ArrayList<Param> paramList) {
		super(frameID,responseCode,responseMessage,paramList);
	}
	
	public DiscourseResponseLog(CSVRecord record){
		super(record);
	}

	@Override
	public void readFromCSVRow(CSVRecord record) {
		//Read Frame ID, Response Code and Response Message
		String frameID = record.get("FRAME_ID");
		int responseCode = Integer.parseInt(record.get("RESPONSE_CODE"));
		String responseMessage = record.get("RESPONSE_MESSAGE");

		//Create a list of DiscourseParam from the values of the row
		ArrayList<Param> paramList = new ArrayList<Param>();

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
				Param p = new Param(key,discourseType,discoursePosition,discourseClass,discourseResourceType,discourseIsRequired,discourseValidValues);
				paramList.add(p);
			}
		}
		
		//Load ResponseLog fields
		this.frameID = frameID;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
//		this.responseBody = responseBody;
		this.paramList = paramList;
		
	}

}
