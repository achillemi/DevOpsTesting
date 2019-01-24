package it.alessandrochillemi.tesi.WLGenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ResponseLogList<R extends ResponseLog<? extends Param<? extends PreCondition>>> {
	private ArrayList<R> responseLogList;
	
	public ResponseLogList(){
		responseLogList = new ArrayList<R>();
	}
	
	@SuppressWarnings("unchecked")
	public ResponseLogList(String path){
		if(Files.exists(Paths.get(path))) { 
			FileInputStream fis;
			try {
				fis = new FileInputStream(path);
				ObjectInputStream ois = new ObjectInputStream(fis);
				this.responseLogList = (ArrayList<R>) ois.readObject();
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
		return responseLogList.size();
	}
	
	public void add(R responseLog){
		responseLogList.add(responseLog);
	}
	
	public R get(int index){
		return responseLogList.get(index);
	}

	//Numero di risposte riferite al frame specificato
	public int count(String frameID){
		int count = 0;
		for(R r : responseLogList){
			if(r.getFrameID().equals(frameID)){
				count++;
			}
		}
		return count;
	}
	
	public void saveToFile(String path){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(responseLogList);
	        oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
