package it.alessandrochillemi.tesi.WLGenerator.discourse;

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
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import it.alessandrochillemi.tesi.WLGenerator.FrameMap;
import it.alessandrochillemi.tesi.WLGenerator.HTTPMethod;
import it.alessandrochillemi.tesi.WLGenerator.Param.Position;

public class DiscourseFrameMap extends FrameMap<DiscourseFrame>{
	
	private TreeMap<Integer, DiscourseFrame> map;
	
	private enum header{
		METHOD,ENDPOINT,
		P1_KEY,P1_TYPE,P1_CLASS,P1_POSITION,P1_RESOURCE_TYPE,P1_IS_REQUIRED,P1_VALID_VALUES,
		P2_KEY,P2_TYPE,P2_CLASS,P2_POSITION,P2_RESOURCE_TYPE,P2_IS_REQUIRED,P2_VALID_VALUES,
		P3_KEY,P3_TYPE,P3_CLASS,P3_POSITION,P3_RESOURCE_TYPE,P3_IS_REQUIRED,P3_VALID_VALUES,
		P4_KEY,P4_TYPE,P4_CLASS,P4_POSITION,P4_RESOURCE_TYPE,P4_IS_REQUIRED,P4_VALID_VALUES,
		P5_KEY,P5_TYPE,P5_CLASS,P5_POSITION,P5_RESOURCE_TYPE,P5_IS_REQUIRED,P5_VALID_VALUES,
		P6_KEY,P6_TYPE,P6_CLASS,P6_POSITION,P6_RESOURCE_TYPE,P6_IS_REQUIRED,P6_VALID_VALUES,
		PROB_SELECTION,PROB_FAILURE;
	};
	
	public DiscourseFrameMap(){
		this.map = new TreeMap<Integer, DiscourseFrame>();
	}
	
	public DiscourseFrameMap(String path){
		this.map = new TreeMap<Integer, DiscourseFrame>();
		readFromCSVFile(path);
	}
	
	public int size(){
		return this.map.size();
	}
	
	public DiscourseFrame readByKey(Integer key){
		return this.map.get(key);
	}
	
	public Iterator<Map.Entry<Integer, DiscourseFrame>> iterator(){
		return this.map.entrySet().iterator();
	}
	
	public void append(ArrayList<DiscourseFrame> list){
		for(DiscourseFrame frame : list){
			this.map.put(this.map.isEmpty() ? 1 : this.map.lastKey()+1, frame);
		}
	}
	
	//Get the probability selection for every entry in the FrameMap; the order is preserved, because the underlying Map is a TreeMap.
	public ArrayList<Double> getProbSelectionDistribution(){
		ArrayList<Double> ret = new ArrayList<Double>();

		for(Map.Entry<Integer, DiscourseFrame> entry : this.map.entrySet()){
			ret.add(entry.getValue().getProbSelection());
		}
		return ret;
	}
	
	//Set the probability selection for every entry in the FrameMap
	public void setProbSelectionDistribution(ArrayList<Double> probSelectionDistribution){
		Iterator<Map.Entry<Integer, DiscourseFrame>> iter = this.map.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			Entry<Integer, DiscourseFrame> entry = iter.next();
			entry.getValue().setProbSelection(probSelectionDistribution.get(i));
			i++;
		}
	}
	
	//Get all the frames that have the specified endpoint
	public ArrayList<DiscourseFrame> getFramesByEndpoint(HTTPMethod method, String endpoint){
		ArrayList<DiscourseFrame> ret = new ArrayList<DiscourseFrame>();

		for(Map.Entry<Integer, DiscourseFrame> entry : this.map.entrySet()){
			if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
				ret.add(entry.getValue());
			}
		}
		return ret;
	}

	//Update all the frames that have the specified endpoint to the specified frame list;
	//if the specified list has a different number of elements than the ones already present, a message is shown and the operation is not performed.
	public void updateFramesByEndpoint(HTTPMethod method, String endpoint, ArrayList<DiscourseFrame> frameBeansList){
		ArrayList<DiscourseFrame> oldFrameList = getFramesByEndpoint(method,endpoint);

		if(frameBeansList.size() != oldFrameList.size()){
			System.out.println("The specified frame list has a different number of elements than the existing one!");
		}
		else{
			Iterator<Map.Entry<Integer, DiscourseFrame>> iter = this.map.entrySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
				Entry<Integer, DiscourseFrame> entry = iter.next();
				if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
					entry.setValue(frameBeansList.get(i));
					i++;
				}
			}
		}

	}
	
	//	public void changeClass(){
	//	Iterator<Map.Entry<Integer, FrameBean>> iter = map.entrySet().iterator();
	//	FrameBean newFrameBean = null;
	//	FrameBean oldFrameBean = null;
	//	while (iter.hasNext()) {
	//	    Entry<Integer, FrameBean> entry = iter.next();
	//	    oldFrameBean = entry.getValue();
	//	    newFrameBean = new FrameBean(oldFrameBean);
	//	    ArrayList<DiscourseParam> discourseParamList = new ArrayList<DiscourseParam>();
	//	    for(Param p : oldFrameBean.getParamList()){
	//	    	DiscourseParam discourseParam = new DiscourseParam(p);
	//	    	discourseParamList.add(discourseParam);
	//	    }
	//	    newFrameBean.setParamList(discourseParamList);
	//	    entry.setValue(newFrameBean);
	//	}
	//}
	
	public void deleteFrames(HTTPMethod method, String endpoint){
		Iterator<Map.Entry<Integer, DiscourseFrame>> iter = this.map.entrySet().iterator();
		while (iter.hasNext()) {
		    Entry<Integer, DiscourseFrame> entry = iter.next();
		    if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
		        iter.remove();
		    }
		}
	}
	
	public void readFromCSVFile(String path){
		if(Files.exists(Paths.get(path))){
			Reader in;
			try {
				//Read the CSV file
				in = new FileReader(path);
				Iterable<CSVRecord> records = CSVFormat.RFC4180.withDelimiter(';').withFirstRecordAsHeader().parse(in);
				for (CSVRecord record : records) {
					//Read API method and endpoint
					HTTPMethod method = HTTPMethod.valueOf(record.get("METHOD"));
					String endpoint = record.get("ENDPOINT");

					//Create a list of DiscourseParam from the values of the row
					ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();

					//Read the parameter's features for each of the 6 parameters on a row
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
					//Read probSelection and probFailure
					Double probSelection = Double.valueOf(record.get("PROB_SELECTION"));
					Double probFailure = Double.valueOf(record.get("PROB_FAILURE"));
					
					//Create a new frame from the record just read
					DiscourseFrame discourseFrame = new DiscourseFrame(method,endpoint,paramList,probSelection,probFailure);
					
					//Add the frame to the map
					this.map.put(this.map.isEmpty() ? 1 : this.map.lastKey()+1, discourseFrame);

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
	
	public void writeToCSVFile(String path){
		BufferedWriter writer;
		try {
			writer = Files.newBufferedWriter(Paths.get(path));

			@SuppressWarnings("resource")
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180.withDelimiter(';').withHeader(header.class));

			Iterator<Map.Entry<Integer, DiscourseFrame>> iter = this.map.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<Integer, DiscourseFrame> entry = iter.next();
				String method = entry.getValue().getMethod().toString();
				String endpoint = entry.getValue().getEndpoint();

				ArrayList<String> paramKeys = new ArrayList<String>();
				ArrayList<String> paramTypes = new ArrayList<String>();
				ArrayList<String> paramClasses = new ArrayList<String>();
				ArrayList<String> paramPositions = new ArrayList<String>();
				ArrayList<String> paramResourceTypes = new ArrayList<String>();
				ArrayList<String> paramIsRequireds = new ArrayList<String>();
				ArrayList<String> paramValidValues = new ArrayList<String>();

				for(int i=0;i<6;i++){
					if(i<entry.getValue().getParamList().size()){
						paramKeys.add(entry.getValue().getParamList().get(i).getKeyParam());
						paramTypes.add(entry.getValue().getParamList().get(i).getTypeParam().toString());
						paramClasses.add(entry.getValue().getParamList().get(i).getClassParam().toString());
						paramPositions.add(entry.getValue().getParamList().get(i).getPosition().toString());
						paramResourceTypes.add(entry.getValue().getParamList().get(i).getResourceType().toString());
						paramIsRequireds.add(String.valueOf(entry.getValue().getParamList().get(i).isRequired()));
						if(entry.getValue().getParamList().get(i).getValidValues().size()>0){
							paramValidValues.add(StringUtils.join(entry.getValue().getParamList().get(i).getValidValues(),","));
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
				
				String probSelection = entry.getValue().getProbSelection().toString();
				String probFailure = entry.getValue().getProbFailure().toString();

				csvPrinter.printRecord(method, endpoint, 
						paramKeys.get(0), paramTypes.get(0), paramClasses.get(0), paramPositions.get(0), paramResourceTypes.get(0), paramIsRequireds.get(0), paramValidValues.get(0),
						paramKeys.get(1), paramTypes.get(1), paramClasses.get(1), paramPositions.get(1), paramResourceTypes.get(1), paramIsRequireds.get(1), paramValidValues.get(1),
						paramKeys.get(2), paramTypes.get(2), paramClasses.get(2), paramPositions.get(2), paramResourceTypes.get(2), paramIsRequireds.get(2), paramValidValues.get(2),
						paramKeys.get(3), paramTypes.get(3), paramClasses.get(3), paramPositions.get(3), paramResourceTypes.get(3), paramIsRequireds.get(3), paramValidValues.get(3),
						paramKeys.get(4), paramTypes.get(4), paramClasses.get(4), paramPositions.get(4), paramResourceTypes.get(4), paramIsRequireds.get(4), paramValidValues.get(4),
						paramKeys.get(5), paramTypes.get(5), paramClasses.get(5), paramPositions.get(5), paramResourceTypes.get(5), paramIsRequireds.get(5), paramValidValues.get(5),
						probSelection,probFailure);
			}

			csvPrinter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void print(){
		if(this.map != null){
			for(Entry<Integer, DiscourseFrame> entry : this.map.entrySet()){
				System.out.print(entry.getKey() + " ");
				entry.getValue().print();
				System.out.print("\n");
			}
		}
	}
	
	//Print the entries with the specified endpoint only
	public void print(HTTPMethod method, String endpoint){
		if(this.map != null){
			for(Entry<Integer, DiscourseFrame> entry : this.map.entrySet()){
				if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
					System.out.print(entry.getKey() + " ");
					entry.getValue().print();
					System.out.print("\n");
				}
			}
		}
	}

}
