package it.alessandrochillemi.tesi.FrameUtils;

public abstract class Oracle<R extends ResponseLogList<? extends ResponseLog<? extends Param>>>{
	
	//Ritorna il numero totale di fallimenti nella lista
	public abstract int getTotalNumberOfFailures(R responseLogList);
	
	//Ritorna il numero di fallimenti per il frame con Frame ID specificato
	public abstract int getFrameFailures(R responseLogList, String frameID);

}
