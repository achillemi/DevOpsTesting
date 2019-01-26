package it.alessandrochillemi.tesi.FrameUtils.discourse;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.FrameUtils.ResponseLog;

public class DiscourseResponseLog extends ResponseLog<DiscourseParam> {

	private ArrayList<DiscourseParam> paramList;
	
	private static final long serialVersionUID = -7764224813306420581L;
	
	public DiscourseResponseLog(){
		this.paramList = new ArrayList<DiscourseParam>();
	}

	public DiscourseResponseLog(String frameID, Integer responseCode, String responseMessage, /*String responseBody,*/ ArrayList<DiscourseParam> paramList) {
		super(frameID,responseCode,responseMessage,paramList);

		this.paramList = paramList;
	}

	public ArrayList<DiscourseParam> getParamList() {
		return this.paramList;
	}

	public void setParamList(ArrayList<DiscourseParam> paramList) {
		this.paramList = paramList;
	}

}
