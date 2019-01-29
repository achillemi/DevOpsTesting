package it.alessandrochillemi.tesi.FrameUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

//Classe che modella i campi di un Frame che devono essere letti o scritti
public abstract class Frame implements Serializable{
	
	protected HTTPMethod method;															//Metodo della richiesta HTTP per usare l'API
	protected String endpoint;																//Endpoint dell'API
	protected ArrayList<Param> paramList;													//Lista di parametri
	protected Double probSelection;															//Probabilità di selezione stimata del Frame
	protected Double probFailure;															//Probabilità di fallimento stimata del Frame
	protected Double probCriticalFailure;													//Probabilità di fallimento critica stimata del Frame
	protected Double trueProbSelection;														//Probabilità di selezione reale del Frame
	protected Double trueProbFailure;														//Probabilità di fallimento reale del Frame
	protected Double trueProbCriticalFailure;												//Probabilità di fallimento critica reale del Frame
		
	private static final long serialVersionUID = 5259280897255194440L;
	
	public Frame(){
		this.paramList = new ArrayList<Param>();
	}
	
	public Frame(HTTPMethod method, String endpoint, ArrayList<Param> paramList, Double probSelection, Double probFailure, Double probCriticalFailure, Double trueProbSelection, Double trueProbFailure, Double trueProbCriticalFailure){
		this.method = method;
		this.endpoint = endpoint;
		this.paramList = paramList;
		this.probSelection = probSelection;
		this.probFailure = probFailure;
		this.probCriticalFailure = probCriticalFailure;
		this.trueProbSelection = trueProbSelection;
		this.trueProbFailure = trueProbFailure;
		this.trueProbCriticalFailure = trueProbCriticalFailure;
	}
	
	public Frame(Frame frame){
		this.method = frame.getMethod();
		this.endpoint = frame.getEndpoint();
		this.paramList = frame.getParamList();
		this.probSelection = frame.getProbSelection();
		this.probFailure = frame.getProbFailure();
		this.trueProbSelection = frame.getTrueProbSelection();
		this.trueProbFailure = frame.getTrueProbFailure();
	}
	
	public Frame(CSVRecord record){
		this.paramList = new ArrayList<Param>();
		readFromCSVRow(record);	
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

	public ArrayList<Param> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<Param> paramList) {
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
	
	public Double getProbCriticalFailure() {
		return probCriticalFailure;
	}

	public void setProbCriticalFailure(Double probCriticalFailure) {
		this.probCriticalFailure = probCriticalFailure;
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
	
	public Double getTrueProbCriticalFailure() {
		return trueProbCriticalFailure;
	}

	public void setTrueProbCriticalFailure(Double trueProbCriticalFailure) {
		this.trueProbCriticalFailure = trueProbCriticalFailure;
	}

	public void generateParamValuesWithPreConditions(String baseURL, String apiUsername, String apiKey, boolean forceNewPreConditions) {
		for(Param p : this.paramList){
			p.generateValueWithPreConditions(baseURL, apiUsername, apiKey, forceNewPreConditions);
		}
		
	}
	
	public void print() {
		System.out.println(this.method + " " + this.endpoint + ": ");
		for(int i = 0; i<paramList.size(); i++){
			System.out.print((i+1) + ": [");
			paramList.get(i).print();
			System.out.print("]; ");
		}
		//System.out.print("probSel: " + probSelection);
	}

	/*	Carica i campi del Frame da una riga di un file CSV; questo metodo dipende dall'implementazione del Frame (ad esempio DiscourseFrame, ecc.),
	 *	perché i campi del CSV vanno letti usando l'EquivalenceClass e il TypeParam appropriati.
	 */ 
	public abstract void readFromCSVRow(CSVRecord record);
	
	//	Scrive i campi del Frame su una riga di un file CSV
	public void writeToCSVRow(CSVPrinter csvPrinter){
		String method = getMethod().toString();
		String endpoint = getEndpoint();

		ArrayList<String> paramKeys = new ArrayList<String>();
		ArrayList<String> paramTypes = new ArrayList<String>();
		ArrayList<String> paramClasses = new ArrayList<String>();
		ArrayList<String> paramPositions = new ArrayList<String>();
		ArrayList<String> paramResourceTypes = new ArrayList<String>();
		ArrayList<String> paramIsRequireds = new ArrayList<String>();
		ArrayList<String> paramValidValues = new ArrayList<String>();

		for(int i=0;i<6;i++){
			if(i<getParamList().size()){
				paramKeys.add(getParamList().get(i).getKeyParam());
				paramTypes.add(getParamList().get(i).getTypeParam().toString());
				paramClasses.add(getParamList().get(i).getClassParam().toString());
				paramPositions.add(getParamList().get(i).getPosition().toString());
				paramResourceTypes.add(getParamList().get(i).getResourceType().toString());
				paramIsRequireds.add(String.valueOf(getParamList().get(i).isRequired()));
				if(getParamList().get(i).getValidValues().size()>0){
					paramValidValues.add(StringUtils.join(getParamList().get(i).getValidValues(),","));
				}
				else{
					paramValidValues.add("/");
				}
			}
			else{
				paramKeys.add("/");
				paramTypes.add("/");
				paramClasses.add("/");
				paramPositions.add("/");
				paramResourceTypes.add("/");
				paramIsRequireds.add("/");
				paramValidValues.add("/");
			}
		}
		
		String probSelection = getProbSelection().toString();
		String probFailure = getProbFailure().toString();
		String probCriticalFailure = getProbCriticalFailure().toString();
		String trueProbSelection = getTrueProbSelection().toString();
		String trueProbFailure = getTrueProbFailure().toString();
		String trueProbCriticalFailure = getTrueProbCriticalFailure().toString();

		try {
			csvPrinter.printRecord(method, endpoint, 
					paramKeys.get(0), paramTypes.get(0), paramClasses.get(0), paramPositions.get(0), paramResourceTypes.get(0), paramIsRequireds.get(0), paramValidValues.get(0),
					paramKeys.get(1), paramTypes.get(1), paramClasses.get(1), paramPositions.get(1), paramResourceTypes.get(1), paramIsRequireds.get(1), paramValidValues.get(1),
					paramKeys.get(2), paramTypes.get(2), paramClasses.get(2), paramPositions.get(2), paramResourceTypes.get(2), paramIsRequireds.get(2), paramValidValues.get(2),
					paramKeys.get(3), paramTypes.get(3), paramClasses.get(3), paramPositions.get(3), paramResourceTypes.get(3), paramIsRequireds.get(3), paramValidValues.get(3),
					paramKeys.get(4), paramTypes.get(4), paramClasses.get(4), paramPositions.get(4), paramResourceTypes.get(4), paramIsRequireds.get(4), paramValidValues.get(4),
					paramKeys.get(5), paramTypes.get(5), paramClasses.get(5), paramPositions.get(5), paramResourceTypes.get(5), paramIsRequireds.get(5), paramValidValues.get(5),
					probSelection,probFailure,probCriticalFailure,trueProbSelection,trueProbFailure,trueProbCriticalFailure);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
