package it.alessandrochillemi.tesi.WLGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Classe "bean" che modella i campi di un Frame che devono essere letti o scritti
public class FrameBean implements Serializable{
	
	private HTTPMethod method;										//Metodo della richiesta HTTP per usare l'API
	private String endpoint;										//Endpoint dell'API
	private ArrayList<? extends Param> paramList;					//Lista di parametri
	private Double probSelection;									//Probabilità di selezione del Frame
	private Double probFailure;										//Probabilità di fallimento del Frame
		
	private static final long serialVersionUID = 5259280897255194440L;
	
	public FrameBean(){
		this.paramList = new ArrayList<Param>();
	}
	
	public FrameBean(FrameBean frameBean){
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

	public ArrayList<? extends Param> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<? extends Param> paramList) {
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
	
	//Generate a list of FrameBeans from a list of class combinations (useful when creating a FrameMap);
	//'paramList' is the List of Params of the API that the resulting list of FrameBeans refers to.
	public static ArrayList<FrameBean> generateFrameBeans(HTTPMethod method, String endpoint, List<? extends Param> paramList, Double probSelection, Double probFailure){
		ArrayList<FrameBean> frameBeansList = new ArrayList<FrameBean>();
		
		ArrayList<TypeParam> types = new ArrayList<TypeParam>();
		for(int k = 0; k<6; k++){
			TypeParam t = null;
			if(k<paramList.size()){
				t = paramList.get(k).getTypeParam();
			}
			types.add(t);
		}
		
		List<List<String>> classesCombinations = EquivalenceClass.cartesianProduct(types.get(0), types.get(1), types.get(2), types.get(3), types.get(4), types.get(5));
		
		for(int i = 0; i<classesCombinations.size(); i++){
			FrameBean frameBean = new FrameBean();
			ArrayList<Param> frameParamList = new ArrayList<Param>();
			frameBean.setMethod(method);
			frameBean.setEndpoint(endpoint);
			frameBean.setProbSelection(probSelection);
			frameBean.setProbFailure(probFailure);
			for(int j = 0; j<paramList.size(); j++){
				Param p1 = new Param(paramList.get(j));
				p1.setClassParam(EquivalenceClass.valueOf(classesCombinations.get(i).get(j)));
				frameParamList.add(p1);
			}
			frameBean.setParamList(frameParamList);
			frameBeansList.add(frameBean);
		}
		
		return frameBeansList;
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
