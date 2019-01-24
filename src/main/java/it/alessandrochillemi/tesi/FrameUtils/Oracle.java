package it.alessandrochillemi.tesi.FrameUtils;

public abstract class Oracle<R extends ResponseLogList<? extends ResponseLog<? extends Param<? extends PreCondition>>>>{
	
	public abstract int getTotalNumberOfFailures(R responseLogList);

}
