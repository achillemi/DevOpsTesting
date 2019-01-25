package it.alessandrochillemi.tesi.FrameUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public abstract class FrameMap<F extends Frame<? extends Param>> {
	
	public abstract int size();
	
	public abstract F readByKey(Integer key);
	
	public abstract Iterator<Map.Entry<Integer, F>> iterator();
	
	public abstract void append(ArrayList<F> list);
	
	//Get the estimated probability selection for every entry in the FrameMap; the order is preserved, because the underlying Map is a TreeMap.
	public abstract ArrayList<Double> getProbSelectionDistribution();
	
	//Set the estimated probability selection for every entry in the FrameMap
	public abstract void setProbSelectionDistribution(ArrayList<Double> probSelectionDistribution);
	
	//Get the true probability selection for every entry in the FrameMap; the order is preserved, because the underlying Map is a TreeMap.
	public abstract ArrayList<Double> getTrueProbSelectionDistribution();
	
	//Set the true probability selection for every entry in the FrameMap
	public abstract void setTrueProbSelectionDistribution(ArrayList<Double> trueProbSelectionDistribution);
	
	//Get the true probability failure for every entry in the FrameMap; the order is preserved, because the underlying Map is a TreeMap.
	public abstract ArrayList<Double> getTrueProbFailureDistribution();

	//Set the true probability failure for every entry in the FrameMap
	public abstract void setTrueProbFailureDistribution(ArrayList<Double> trueProbFailureDistribution);

	//Get all the frames that have the specified endpoint
	public abstract ArrayList<F> getFramesByEndpoint(HTTPMethod method, String endpoint);
	
	//Update all the frames that have the specified endpoint to the specified frame list;
	//if the specified list has a different number of elements than the ones already present, a message is shown and the operation is not performed.
	public abstract void updateFramesByEndpoint(HTTPMethod method, String endpoint, ArrayList<F> frameBeansList);
	
	public abstract void deleteFrames(HTTPMethod method, String endpoint);
	
//	public void saveToFile(String path){
//		FileOutputStream fos;
//		try {
//			fos = new FileOutputStream(path);
//	        ObjectOutputStream oos = new ObjectOutputStream(fos);
//	        oos.writeObject(map);
//	        oos.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public abstract void readFromCSVFile(String path);
	
	public abstract void writeToCSVFile(String path);
	
	public abstract void print();
	
	//Print the entries with the specified endpoint only
	public abstract void print(HTTPMethod method, String endpoint);

}
