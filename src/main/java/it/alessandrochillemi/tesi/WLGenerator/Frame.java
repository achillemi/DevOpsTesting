package it.alessandrochillemi.tesi.WLGenerator;

import java.io.Serializable;
import java.util.ArrayList;

//Classe "bean" che modella i campi di un Frame che devono essere letti o scritti
public abstract class Frame<P extends Param<C>, C extends PreCondition> implements Serializable{
	
	protected HTTPMethod method;															//Metodo della richiesta HTTP per usare l'API
	protected String endpoint;																//Endpoint dell'API
	protected ArrayList<P> paramList;														//Lista di parametri
	protected Double probSelection;															//Probabilità di selezione del Frame
	protected Double probFailure;															//Probabilità di fallimento del Frame
		
	private static final long serialVersionUID = 5259280897255194440L;
	
	public Frame(){
		this.paramList = new ArrayList<P>();
	}
	
	public Frame(HTTPMethod method, String endpoint, ArrayList<P> paramList, Double probSelection, Double probFailure){
		this.method = method;
		this.endpoint = endpoint;
		this.paramList = paramList;
		this.probSelection = probSelection;
		this.probFailure = probFailure;
	}
	
	public Frame(Frame<P,C> frame){
		this.method = frame.getMethod();
		this.endpoint = frame.getEndpoint();
		this.paramList = frame.getParamList();
		this.probSelection = frame.probSelection;
		this.probFailure = frame.probFailure;
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

	public abstract void generateParamValues(ArrayList<C> preConditionList);
	public abstract void print();
	
}
