package it.alessandrochillemi.tesi.WLGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Classe "bean" che modella i campi di un Frame che devono essere letti o scritti
public class FrameBean implements Serializable{
	
	private HTTPMethod method;										//Metodo della richiesta HTTP per usare l'API
	private String endpoint;										//Endpoint dell'API
	private Integer bodyParamsNumber;								//Numero di parametri nel body della richiesta
	private ArrayList<Param> bodyParamList;							//Lista di parametri nel body della richiesta
	private Integer pathParamsNumber;								//Numero di parametri nel path dell'API
	private ArrayList<Param> pathParamList;							//Lista di parametri nel path dell'API
	private Integer queryParamsNumber;								//Numero di parametri nella query
	private ArrayList<Param> queryParamList;						//Lista di parametri nella query
	private Double probSelection;									//Probabilità di selezione del Frame
	private Double probFailure;										//Probabilità di fallimento del Frame
		
	private static final long serialVersionUID = 5259280897255194440L;
	
	public FrameBean(){
		this.bodyParamList = new ArrayList<Param>();
		this.pathParamList = new ArrayList<Param>();
		this.queryParamList = new ArrayList<Param>();
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
	
	public Integer getBodyParamsNumber() {
		return bodyParamsNumber;
	}

	public void setBodyParamsNumber(Integer bodyParamsNumber) {
		this.bodyParamsNumber = bodyParamsNumber;
	}

	public ArrayList<Param> getBodyParamList() {
		return bodyParamList;
	}

	public void setBodyParamList(ArrayList<Param> bodyParamList) {
		this.bodyParamList = bodyParamList;
	}
	
	public Integer getPathParamsNumber() {
		return pathParamsNumber;
	}

	public void setPathParamsNumber(Integer pathParamsNumber) {
		this.pathParamsNumber = pathParamsNumber;
	}

	public ArrayList<Param> getPathParamList() {
		return pathParamList;
	}

	public void setPathParamList(ArrayList<Param> pathParamList) {
		this.pathParamList = pathParamList;
	}
	
	public Integer getQueryParamsNumber() {
		return queryParamsNumber;
	}

	public void setQueryParamsNumber(Integer queryParamsNumber) {
		this.queryParamsNumber = queryParamsNumber;
	}

	public ArrayList<Param> getQueryParamList() {
		return queryParamList;
	}

	public void setQueryParamList(ArrayList<Param> queryParamList) {
		this.queryParamList = queryParamList;
	}

	public void addBodyParam(Param bodyParam){
		this.bodyParamList.add(bodyParam);
	}
	
	public void addPathParam(Param pathParam){
		this.pathParamList.add(pathParam);
	}
	
	public void addQueryParam(Param queryParam){
		this.queryParamList.add(queryParam);
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
	
	//Generate a list of FrameBeans from a list of class combinations (useful when creating a FrameMap);
	//IMPORTANT: keys of parameters (keysParams) and their classes (each List<String> in classesCombinations) must be in order:
	//bodyParams, pathParams and then queryParams.
	public static ArrayList<FrameBean> generateFrameBeans(HTTPMethod method, String endpoint, Integer bodyParamsNumber, Integer pathParamsNumber, Integer queryParamsNumber, List<String> keysParam, List<DiscourseResourceType> discourseResourceTypes, List<List<String>> classesCombinations, Double probSelection, Double probFailure){
		ArrayList<FrameBean> frameBeansList = new ArrayList<FrameBean>();
		
		for(int i = 0; i<classesCombinations.size(); i++){
			FrameBean frameBean = new FrameBean();
			frameBean.setMethod(method);
			frameBean.setEndpoint(endpoint);
			frameBean.setBodyParamsNumber(bodyParamsNumber);
			frameBean.setPathParamsNumber(pathParamsNumber);
			frameBean.setQueryParamsNumber(queryParamsNumber);
			frameBean.setProbSelection(probSelection);
			frameBean.setProbFailure(probFailure);
			for(int j = 0; j<bodyParamsNumber; j++){
				Param<DiscoursePreCondition> param = new DiscourseParam(keysParam.get(j),EquivalenceClass.valueOf(classesCombinations.get(i).get(j)),discourseResourceTypes.get(j));
				frameBean.addBodyParam(param);
			}
			for(int j = bodyParamsNumber; j<(bodyParamsNumber+pathParamsNumber); j++){
				Param<DiscoursePreCondition> param = new DiscourseParam(keysParam.get(j),EquivalenceClass.valueOf(classesCombinations.get(i).get(j)),discourseResourceTypes.get(j));
				frameBean.addPathParam(param);
			}
			for(int j = (bodyParamsNumber+pathParamsNumber); j<(bodyParamsNumber+pathParamsNumber+queryParamsNumber); j++){
				Param<DiscoursePreCondition> param = new DiscourseParam(keysParam.get(j),EquivalenceClass.valueOf(classesCombinations.get(i).get(j)),discourseResourceTypes.get(j));
				frameBean.addQueryParam(param);
			}
			frameBeansList.add(frameBean);
		}

		return frameBeansList;
		
	}
	
	public void print(){
		System.out.print(method + " " + endpoint + " ");
		for(Param param : bodyParamList){
			System.out.print(param.getKeyParam() + " [" + param.getClassParam() + "] ");
		}
		for(Param param : pathParamList){
			System.out.print(param.getKeyParam() + " [" + param.getClassParam() + "] ");
		}
		for(Param param : queryParamList){
			System.out.print(param.getKeyParam() + " [" + param.getClassParam() + "] ");
		}
		System.out.print("probSel: " + probSelection);
	}
	
}
