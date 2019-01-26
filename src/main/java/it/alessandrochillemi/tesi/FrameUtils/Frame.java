package it.alessandrochillemi.tesi.FrameUtils;

import java.io.Serializable;
import java.util.ArrayList;

//Classe che modella i campi di un Frame che devono essere letti o scritti
public abstract class Frame<P extends Param> implements Serializable{
	
	protected HTTPMethod method;															//Metodo della richiesta HTTP per usare l'API
	protected String endpoint;																//Endpoint dell'API
	protected ArrayList<P> paramList;														//Lista di parametri
	protected Double probSelection;															//Probabilità di selezione stimata del Frame
	protected Double probFailure;															//Probabilità di fallimento stimata del Frame
	protected Double trueProbSelection;														//Probabilità di selezione reale del Frame
	protected Double trueProbFailure;														//Probabilità di fallimento reale del Frame
		
	private static final long serialVersionUID = 5259280897255194440L;
	
	public Frame(){
		this.paramList = new ArrayList<P>();
	}
	
	public Frame(HTTPMethod method, String endpoint, ArrayList<P> paramList, Double probSelection, Double probFailure, Double trueProbSelection, Double trueProbFailure){
		this.method = method;
		this.endpoint = endpoint;
		this.paramList = paramList;
		this.probSelection = probSelection;
		this.probFailure = probFailure;
		this.trueProbSelection = trueProbSelection;
		this.trueProbFailure = trueProbFailure;
	}
	
	public Frame(Frame<P> frame){
		this.method = frame.getMethod();
		this.endpoint = frame.getEndpoint();
		this.paramList = frame.getParamList();
		this.probSelection = frame.getProbSelection();
		this.probFailure = frame.getProbFailure();
		this.trueProbSelection = frame.getTrueProbSelection();
		this.trueProbFailure = frame.getTrueProbFailure();
	}

	public HTTPMethod getMethod() {
		return method;
	}

	public void setMethod(HTTPMethod method) {
		this.method = method;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public ArrayList<P> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<P> paramList) {
		this.paramList = paramList;
	}

	public Double getProbSelection() {
		return probSelection;
	}

	public void setProbSelection(Double probSelection) {
		this.probSelection = probSelection;
	}

	public Double getProbFailure() {
		return probFailure;
	}

	public void setProbFailure(Double probFailure) {
		this.probFailure = probFailure;
	}

	public Double getTrueProbSelection() {
		return trueProbSelection;
	}

	public void setTrueProbSelection(Double trueProbSelection) {
		this.trueProbSelection = trueProbSelection;
	}

	public Double getTrueProbFailure() {
		return trueProbFailure;
	}

	public void setTrueProbFailure(Double trueProbFailure) {
		this.trueProbFailure = trueProbFailure;
	}

	public abstract void generateParamValuesWithPreConditions(String baseURL, String apiUsername, String apiKey, boolean forceNewPreConditions);
	public abstract void print();
	
}
