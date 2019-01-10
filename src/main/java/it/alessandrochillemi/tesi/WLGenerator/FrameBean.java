package it.alessandrochillemi.tesi.WLGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Classe "bean" che modella i campi di un Frame che devono essere letti o scritti
public class FrameBean implements Serializable{
	
	private HTTPMethod method;										//Metodo della richiesta HTTP per usare l'API
	private String endpoint;										//Endpoint dell'API
	private Integer paramNumber;									//Numero di parametri
	private ArrayList<Param> paramList;
	private Double probSelection;									//Probabilità di selezione del TestFrame
	private Double probFailure;										//Probabilità di fallimento del TestFrame
		
	private static final long serialVersionUID = 5259280897255194440L;
	
	public FrameBean(){
		this.paramList = new ArrayList<Param>();
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
	
	public Integer getParamNumber() {
		return paramNumber;
	}

	public void setParamNumber(Integer paramNumber) {
		this.paramNumber = paramNumber;
	}

	public List<Param> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<Param> paramList) {
		this.paramList = paramList;
	}
	
	public Param getParam(int index) {
		return this.paramList.get(index);
	}
	
	public void addParam(Param param){
		if(this.paramList.size() < this.paramNumber){
			this.paramList.add(param);
		}
		else{
			System.out.println("Param list already full!");
		}
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
		System.out.print(method + " " + endpoint + " ");
		for(Param param : paramList){
			System.out.print(param.getKeyParam() + " ");
		}
	}
	
}
