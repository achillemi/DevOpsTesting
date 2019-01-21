package it.alessandrochillemi.tesi.WLGenerator;

import java.io.Serializable;
import java.util.ArrayList;

//Classe "bean" che modella i campi di un Frame che devono essere letti o scritti
public class FrameBean<T extends PreCondition> implements Serializable{
	
	private HTTPMethod method;																//Metodo della richiesta HTTP per usare l'API
	private String endpoint;																//Endpoint dell'API
	private ArrayList<? extends Param<T>> paramList;										//Lista di parametri
	private Double probSelection;															//Probabilità di selezione del Frame
	private Double probFailure;																//Probabilità di fallimento del Frame
		
	private static final long serialVersionUID = 5259280897255194440L;
	
	public FrameBean(){
		this.paramList = new ArrayList<Param<T>>();
	}
	
	public FrameBean(FrameBean<T> frameBean){
		this.method = frameBean.getMethod();
		this.endpoint = frameBean.getEndpoint();
		this.paramList = frameBean.getParamList();
		this.probSelection = frameBean.probSelection;
		this.probFailure = frameBean.probFailure;
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

	public ArrayList<? extends Param<T>> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<? extends Param<T>> paramList) {
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
	
	public void print(){
		System.out.println(method + " " + endpoint + ": ");
		for(int i = 0; i<paramList.size(); i++){
			System.out.print(i + ": [");
			paramList.get(i).print();
			System.out.print("]; ");
		}
//		System.out.print("probSel: " + probSelection);
	}
	
}
