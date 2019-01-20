package it.alessandrochillemi.tesi.WLGenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class FrameMap {
	
	//A Treemap keeps the entries sorted by the keys' natural ordering
	private TreeMap<Integer, FrameBean> map;
	
	public FrameMap(){
		this.map = new TreeMap<Integer, FrameBean>();
	}
	
	@SuppressWarnings("unchecked")
	public FrameMap(String path){
		if(Files.exists(Paths.get(path))) { 
			FileInputStream fis;
			try {
				fis = new FileInputStream(path);
				ObjectInputStream ois = new ObjectInputStream(fis);
				this.map = (TreeMap<Integer, FrameBean>) ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			System.out.println("File does not exist!");
		}
	}
	
	public int size(){
		return map.size();
	}
	
	public FrameBean readByKey(Integer key){
		return map.get(key);
	}
	
	public Iterator<Map.Entry<Integer, FrameBean>> iterator(){
		return map.entrySet().iterator();
	}
	
	public void append(List<FrameBean> list){
		for(FrameBean frameBean : list){
			map.put(map.isEmpty() ? 1 : map.lastKey()+1, frameBean);
		}
	}
	
	//Get the probability selection for every entry in the FrameMap; the order is preserved, because the underlying Map is a TreeMap.
	public ArrayList<Double> getProbSelectionDistribution(){
		ArrayList<Double> ret = new ArrayList<Double>();
		
		for(Map.Entry<Integer, FrameBean> entry : map.entrySet()){
			ret.add(entry.getValue().getProbSelection());
		}
		return ret;
	}
	
	//Set the probability selection for every entry in the FrameMap
	public void setProbSelectionDistribution(ArrayList<Double> probSelectionDistribution){
		Iterator<Map.Entry<Integer, FrameBean>> iter = map.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
		    Entry<Integer, FrameBean> entry = iter.next();
		    entry.getValue().setProbSelection(probSelectionDistribution.get(i));
		    i++;
		}
	}
	
	//Get all the frames that have the specified endpoint
	public ArrayList<FrameBean> getFrameBeansByEndpoint(HTTPMethod method, String endpoint){
		ArrayList<FrameBean> ret = new ArrayList<FrameBean>();
		
		for(Map.Entry<Integer, FrameBean> entry : map.entrySet()){
			if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
				ret.add(entry.getValue());
			}
		}
		return ret;
	}
	
	//Update all the frames that have the specified endpoint to the specified frame list;
	//if the specified list has a different number of elements than the ones already present, a message is shown and the operation is not performed.
	public void updateFrameBeansByEndpoint(HTTPMethod method, String endpoint, List<FrameBean> frameBeansList){
		ArrayList<FrameBean> oldFrameList = getFrameBeansByEndpoint(method,endpoint);
		
		if(frameBeansList.size() != oldFrameList.size()){
			System.out.println("The specified frame list has a different number of elements than the existing one!");
		}
		else{
			Iterator<Map.Entry<Integer, FrameBean>> iter = map.entrySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
			    Entry<Integer, FrameBean> entry = iter.next();
			    if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
			    	entry.setValue(frameBeansList.get(i));
			    	i++;
			    }
			}
		}

	}
	
//	public void changeClass(){
//		Iterator<Map.Entry<Integer, FrameBean>> iter = map.entrySet().iterator();
//		FrameBean newFrameBean = null;
//		FrameBean oldFrameBean = null;
//		while (iter.hasNext()) {
//		    Entry<Integer, FrameBean> entry = iter.next();
//		    oldFrameBean = entry.getValue();
//		    newFrameBean = new FrameBean(oldFrameBean);
//		    ArrayList<DiscourseParam> discourseParamList = new ArrayList<DiscourseParam>();
//		    for(Param p : oldFrameBean.getParamList()){
//		    	DiscourseParam discourseParam = new DiscourseParam(p);
//		    	discourseParamList.add(discourseParam);
//		    }
//		    newFrameBean.setParamList(discourseParamList);
//		    entry.setValue(newFrameBean);
//		}
//	}
	
	public void deleteFrames(HTTPMethod method, String endpoint){
		Iterator<Map.Entry<Integer, FrameBean>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
		    Entry<Integer, FrameBean> entry = iter.next();
		    if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
		        iter.remove();
		    }
		}
	}
	
	public void saveToFile(String path){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(map);
	        oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void print(){
		if(map != null){
			for(Entry<Integer, FrameBean> entry : map.entrySet()){
				System.out.print(entry.getKey() + " ");
				entry.getValue().print();
				System.out.print("\n");
			}
		}
	}
	
	//Print the entries with the specified endpoint only
	public void print(HTTPMethod method, String endpoint){
		if(map != null){
			for(Entry<Integer, FrameBean> entry : map.entrySet()){
				if(entry.getValue().getMethod().equals(method) && entry.getValue().getEndpoint().equals(endpoint)){
					System.out.print(entry.getKey() + " ");
					entry.getValue().print();
					System.out.print("\n");
				}
			}
		}
	}

}
