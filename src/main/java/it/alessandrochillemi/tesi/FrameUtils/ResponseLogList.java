package it.alessandrochillemi.tesi.FrameUtils;

public abstract class ResponseLogList<R extends ResponseLog<? extends Param>> {
	
	public abstract int size();
	
	public abstract void add(R responseLog);
	
	public abstract R get(int index);

	//Numero di risposte riferite al frame specificato
	public abstract int count(String frameID);
	
	public abstract void readFromCSVFile(String path);
	
	public abstract void writeToCSVFile(String path);
	
//	public void saveToFile(String path){
//		FileOutputStream fos;
//		try {
//			fos = new FileOutputStream(path);
//	        ObjectOutputStream oos = new ObjectOutputStream(fos);
//	        oos.writeObject(responseLogList);
//	        oos.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
