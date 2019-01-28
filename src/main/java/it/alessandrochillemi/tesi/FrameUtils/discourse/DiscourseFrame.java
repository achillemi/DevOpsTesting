package it.alessandrochillemi.tesi.FrameUtils.discourse;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.EnumUtils;

import it.alessandrochillemi.tesi.FrameUtils.Frame;
import it.alessandrochillemi.tesi.FrameUtils.HTTPMethod;
import it.alessandrochillemi.tesi.FrameUtils.Param;
import it.alessandrochillemi.tesi.FrameUtils.Param.Position;

public class DiscourseFrame extends Frame{

	private static final long serialVersionUID = -3724239478468103245L;
	
	public DiscourseFrame(){
		super();
	}
	
	public DiscourseFrame(HTTPMethod method, String endpoint, ArrayList<Param> paramList, Double probSelection, Double probFailure, Double trueProbSelection, Double trueProbFailure){
		super(method,endpoint,paramList,probSelection,probFailure,trueProbSelection,trueProbFailure);

	}
	
	public DiscourseFrame(Frame frame){
		super(frame);
	}
	
	public DiscourseFrame(CSVRecord record){
		super(record);
	}

	@Override
	public void readFromCSVRow(CSVRecord record) {
		//Read API method and endpoint
		HTTPMethod method = HTTPMethod.valueOf(record.get("METHOD"));
		String endpoint = record.get("ENDPOINT");

		//Create a list of Param from the values of the row
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
		//Read probSelection and probFailure
		Double probSelection = Double.valueOf(record.get("PROB_SELECTION"));
		Double probFailure = Double.valueOf(record.get("PROB_FAILURE"));
		Double trueProbSelection = Double.valueOf(record.get("TRUE_PROB_SELECTION"));
		Double trueProbFailure = Double.valueOf(record.get("TRUE_PROB_FAILURE"));
		
		//Load frame fields
		this.method = method;
		this.endpoint = endpoint;
		this.paramList = paramList;
		this.probSelection = probSelection;
		this.probFailure = probFailure;
		this.trueProbSelection = trueProbSelection;
		this.trueProbFailure = trueProbFailure;
		
	}

}
