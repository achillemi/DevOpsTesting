package it.alessandrochillemi.tesi.FrameUtils.discourse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.EnumUtils;

import it.alessandrochillemi.tesi.FrameUtils.Frame;
import it.alessandrochillemi.tesi.FrameUtils.HTTPMethod;
import it.alessandrochillemi.tesi.FrameUtils.Param.Position;

public class DiscourseFrame extends Frame<DiscourseParam>{
	
	private ArrayList<DiscourseParam> paramList;

	private static final long serialVersionUID = -3724239478468103245L;
	
	public DiscourseFrame(){
		this.paramList = new ArrayList<DiscourseParam>();
	}
	
	public DiscourseFrame(HTTPMethod method, String endpoint, ArrayList<DiscourseParam> paramList, Double probSelection, Double probFailure, Double trueProbSelection, Double trueProbFailure){
		super(method,endpoint,paramList,probSelection,probFailure,trueProbSelection,trueProbFailure);
		
		this.paramList = paramList;
	}
	
	public DiscourseFrame(DiscourseFrame frame){
		this.method = frame.getMethod();
		this.endpoint = frame.getEndpoint();
		this.paramList = frame.getParamList();
		this.probSelection = frame.getProbSelection();
		this.probFailure = frame.getProbFailure();
		this.trueProbSelection = frame.getTrueProbSelection();
		this.trueProbFailure = frame.getTrueProbFailure();
	}

	public ArrayList<DiscourseParam> getParamList() {
		return this.paramList;
	}

	public void setParamList(ArrayList<DiscourseParam> paramList) {
		this.paramList = paramList;
	}
	
	//Generate a list of Frames from a CSV containing the API descriptions; probSelection and probFailure are constant initial values assigned to every Frame;
	//they can be manually modified later.
	public static ArrayList<DiscourseFrame> generateFromCSV(String apiDescriptionsCSVFilePath, Double probSelection, Double probFailure, Double trueProbSelection, Double trueProbFailure){
		if(Files.exists(Paths.get(apiDescriptionsCSVFilePath))) {
			ArrayList<DiscourseFrame> ret = new ArrayList<DiscourseFrame>();
			Reader in;
			try {
				//Read the CSV file
				in = new FileReader(apiDescriptionsCSVFilePath);
				Iterable<CSVRecord> records = CSVFormat.RFC4180.withDelimiter(';').withFirstRecordAsHeader().parse(in);
				
				//Iterate over rows
				for (CSVRecord record : records) {
					//Create a list of DiscourseParam from the values of the row
					ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
					
					//Read API method and endpoint
					HTTPMethod method = HTTPMethod.valueOf(record.get("METHOD"));
					String endpoint = record.get("ENDPOINT");
					
					//Read the parameter's features for each of the 6 parameters (at most) on a row
					for(int i = 1; i<=6; i++){
					    String key = record.get("P"+i+"_KEY");
					    String type = record.get("P"+i+"_TYPE");
					    String position = record.get("P"+i+"_POSITION");
					    String resourceType = record.get("P"+i+"_RESOURCE_TYPE");
					    String isRequired = record.get("P"+i+"_IS_REQUIRED");
					    String validValues = record.get("P"+i+"_VALID_VALUES");
					    
					    //If P_KEY!=null and P_KEY!="/", create a new parameter and add it to the list; otherwise, it means that there are no more parameters
					    if(key != null && !key.equals("/")){
					    	DiscourseTypeParam discourseType = EnumUtils.getEnumIgnoreCase(DiscourseTypeParam.class, type);
					    	Position discoursePosition = EnumUtils.getEnumIgnoreCase(Position.class, position);
					    	DiscourseResourceType discourseResourceType = EnumUtils.getEnumIgnoreCase(DiscourseResourceType.class, resourceType);
					    	boolean discourseIsRequired = Boolean.parseBoolean(isRequired);
					    	ArrayList<String> discourseValidValues = new ArrayList<String>();
					    	if(!validValues.equals("/")){
					    		discourseValidValues.addAll(Arrays.asList(validValues.split(",")));
					    	}
					    	
					    	DiscourseParam p = new DiscourseParam(key,discourseType,discoursePosition,discourseResourceType,discourseIsRequired,discourseValidValues);
					    	paramList.add(p);
					    }
					}
					//Get the list of DiscourseFrames and add it to the return array
					ArrayList<DiscourseFrame> discourseFrames = DiscourseEquivalenceClass.generateDiscourseFrames(method, endpoint, paramList, probSelection, probFailure,trueProbSelection,trueProbFailure);
					ret.addAll(discourseFrames);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ret;
		}
		else{
			System.out.println("File does not exist!");
			return null;
		}
	}
	
	@Override
	public void generateParamValuesWithPreConditions(String baseURL, String apiUsername, String apiKey, boolean forceNewPreConditions) {
		for(DiscourseParam p : this.paramList){
			p.generateValueWithPreConditions(baseURL, apiUsername, apiKey, forceNewPreConditions);
		}
		
	}
	
	@Override
	public void print() {
		System.out.println(this.method + " " + this.endpoint + ": ");
		for(int i = 0; i<paramList.size(); i++){
			System.out.print((i+1) + ": [");
			paramList.get(i).print();
			System.out.print("]; ");
		}
		//System.out.print("probSel: " + probSelection);

	}

}
