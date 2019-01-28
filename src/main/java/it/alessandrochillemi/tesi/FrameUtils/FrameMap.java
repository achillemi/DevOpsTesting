package it.alessandrochillemi.tesi.FrameUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public abstract class FrameMap<F extends Frame> {
	
	private enum header{
		METHOD,ENDPOINT,
		P1_KEY,P1_TYPE,P1_CLASS,P1_POSITION,P1_RESOURCE_TYPE,P1_IS_REQUIRED,P1_VALID_VALUES,
		P2_KEY,P2_TYPE,P2_CLASS,P2_POSITION,P2_RESOURCE_TYPE,P2_IS_REQUIRED,P2_VALID_VALUES,
		P3_KEY,P3_TYPE,P3_CLASS,P3_POSITION,P3_RESOURCE_TYPE,P3_IS_REQUIRED,P3_VALID_VALUES,
		P4_KEY,P4_TYPE,P4_CLASS,P4_POSITION,P4_RESOURCE_TYPE,P4_IS_REQUIRED,P4_VALID_VALUES,
		P5_KEY,P5_TYPE,P5_CLASS,P5_POSITION,P5_RESOURCE_TYPE,P5_IS_REQUIRED,P5_VALID_VALUES,
		P6_KEY,P6_TYPE,P6_CLASS,P6_POSITION,P6_RESOURCE_TYPE,P6_IS_REQUIRED,P6_VALID_VALUES,
		PROB_SELECTION,PROB_FAILURE,TRUE_PROB_SELECTION,TRUE_PROB_FAILURE;
	};
	
	protected TreeMap<Integer, F> map;
	
	public FrameMap(){
		this.map = new TreeMap<Integer, F>();
	}
	
	public FrameMap(String path){
		this.map = new TreeMap<Integer, F>();
		readFromCSVFile(path);
	}
	
	public FrameMap(String apiDescriptionsCSVFilePath, Double probSelection, Double probFailure, Double trueProbSelection, Double trueProbFailure){
		this.map = new TreeMap<Integer, F>();
		this.append(generateFromCSV(apiDescriptionsCSVFilePath,probSelection,probFailure,trueProbSelection,trueProbFailure));
	}
	
	public int size(){
		return this.map.size();
	}
	
	public F readByKey(Integer key){
		return this.map.get(key);
	}
	
	public void put(Integer key, F frame){
		this.map.put(key, frame);
	}
	
	public Iterator<Map.Entry<Integer, F>> iterator(){
		return this.map.entrySet().iterator();
	}
	
	public void append(ArrayList<F> list){
		for(F frame : list){
			this.map.put(this.map.isEmpty() ? 0 : this.map.lastKey()+1, frame);
		}
	}
	
	//Get the probability selection for every entry in the FrameMap; the order is preserved, because the underlying Map is a TreeMap.
	public ArrayList<Double> getProbSelectionDistribution(){
		ArrayList<Double> ret = new ArrayList<Double>();

		for(Map.Entry<Integer, F> entry : this.map.entrySet()){
			ret.add(entry.getValue().getProbSelection());
		}
		return ret;
	}

	//Set the probability selection for every entry in the FrameMap
	public void setProbSelectionDistribution(ArrayList<Double> probSelectionDistribution){
		Iterator<Map.Entry<Integer, F>> iter = this.map.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			Entry<Integer, F> entry = iter.next();
			entry.getValue().setProbSelection(probSelectionDistribution.get(i));
			i++;
		}
	}

	//Get the true probability selection for every entry in the FrameMap; the order is preserved, because the underlying Map is a TreeMap.
	public ArrayList<Double> getTrueProbSelectionDistribution(){
		ArrayList<Double> ret = new ArrayList<Double>();

		for(Map.Entry<Integer, F> entry : this.map.entrySet()){
			ret.add(entry.getValue().getTrueProbSelection());
		}
		return ret;
	}

	//Set the true probability selection for every entry in the FrameMap
	public void setTrueProbSelectionDistribution(ArrayList<Double> trueProbSelectionDistribution){
		Iterator<Map.Entry<Integer, F>> iter = this.map.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			Entry<Integer, F> entry = iter.next();
			entry.getValue().setTrueProbSelection(trueProbSelectionDistribution.get(i));
			i++;
		}
	}

	//Get the true probability failure for every entry in the FrameMap; the order is preserved, because the underlying Map is a TreeMap.
	public ArrayList<Double> getTrueProbFailureDistribution(){
		ArrayList<Double> ret = new ArrayList<Double>();

		for(Map.Entry<Integer, F> entry : this.map.entrySet()){
			ret.add(entry.getValue().getTrueProbFailure());
		}
		return ret;
	}

	//Set the true probability failure for every entry in the FrameMap
	public void setTrueProbFailureDistribution(ArrayList<Double> trueProbFailureDistribution){
		Iterator<Map.Entry<Integer, F>> iter = this.map.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			Entry<Integer, F> entry = iter.next();
			entry.getValue().setTrueProbFailure(trueProbFailureDistribution.get(i));
			i++;
		}
	}

	//Get all the frames that have the specified endpoint
	public ArrayList<F> getFramesByEndpoint(HTTPMethod method, String endpoint){
		ArrayList<F> ret = new ArrayList<F>();

		for(Map.Entry<Integer, F> entry : this.map.entrySet()){
			if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
				ret.add(entry.getValue());
			}
		}
		return ret;
	}

	//Update all the frames that have the specified endpoint to the specified frame list;
	//if the specified list has a different number of elements than the ones already present, a message is shown and the operation is not performed.
	public void updateFramesByEndpoint(HTTPMethod method, String endpoint, ArrayList<F> frameBeansList){
		ArrayList<F> oldFrameList = getFramesByEndpoint(method,endpoint);

		if(frameBeansList.size() != oldFrameList.size()){
			System.out.println("The specified frame list has a different number of elements than the existing one!");
		}
		else{
			Iterator<Map.Entry<Integer, F>> iter = this.map.entrySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
				Entry<Integer, F> entry = iter.next();
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
		Iterator<Map.Entry<Integer, F>> iter = this.map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Integer, F> entry = iter.next();
			if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
				iter.remove();
			}
		}
	}
	
	public abstract void readFromCSVFile(String path);
	
	public abstract ArrayList<F> generateFromCSV(String apiDescriptionsCSVFilePath, Double probSelection, Double probFailure, Double trueProbSelection, Double trueProbFailure);
	
	public void writeToCSVFile(String path){
		BufferedWriter writer;
		try {
			writer = Files.newBufferedWriter(Paths.get(path));

			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180.withDelimiter(';').withHeader(header.class));
			
			Iterator<Map.Entry<Integer, F>> iter = this.map.entrySet().iterator();
			
			while (iter.hasNext()) {
				Entry<Integer, F> entry = iter.next();
				
				//Write every frame to a new row of the CSV file
				entry.getValue().writeToCSVRow(csvPrinter);
			}

			csvPrinter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void print(){
		if(this.map != null){
			for(Entry<Integer, F> entry : this.map.entrySet()){
				System.out.print(entry.getKey() + " ");
				entry.getValue().print();
				System.out.print("\n");
			}
		}
	}
	
	//Print the entries with the specified endpoint only
	public void print(HTTPMethod method, String endpoint){
		if(this.map != null){
			for(Entry<Integer, F> entry : this.map.entrySet()){
				if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
					System.out.print(entry.getKey() + " ");
					entry.getValue().print();
					System.out.print("\n");
				}
			}
		}
	}

}
