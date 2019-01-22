package it.alessandrochillemi.tesi.WLGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.lang3.RandomUtils;

import okhttp3.Response;

public class WLGenerator{
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	public static final String ENVIRONMENT_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
    public static void main( String[] args ){
    	
	    	//Carico le variabili d'ambiente (path della lista di testframe, api_key, api_username, ecc.)
	    	Properties environment = new Properties();
	    	InputStream is = null;
	    	try {
	    		is = new FileInputStream(ENVIRONMENT_PATH);
	    	} catch (FileNotFoundException e1) {
	    		e1.printStackTrace();
	    	}
	    	try {
	    		environment.load(is);
	    	} catch (IOException e1) {
	    		e1.printStackTrace();
	    	}
	    	
	    	//Leggo le variabili d'ambiente
	    	String frameMapPath = environment.getProperty("frame_map_path");
	    	String baseURL = environment.getProperty("base_url");
	    	String apiUsername = environment.getProperty("api_username");
	    	String apiKey = environment.getProperty("api_key");
	    	
	    	FrameMap<DiscoursePreCondition> frameMap = new FrameMap<DiscoursePreCondition>(frameMapPath);
	    	
	    	//Scelgo il frame secondo la probabilità di selezione impostata con l'ausilio di un vettore che memorizza la probabilità cumulata
	    	ArrayList<Double> probSelectionDistribution = frameMap.getProbSelectionDistribution();
	    	ArrayList<Double> cumulativePVector = new ArrayList<Double>();
	    	
	    	cumulativePVector.add(probSelectionDistribution.get(0));
	    	
	    	for(int i = 1; i<probSelectionDistribution.size(); i++){
	    		Double d = cumulativePVector.get(i-1)+probSelectionDistribution.get(i);
	    		cumulativePVector.add(d);
	    	}
	    	
	    	double rand = RandomUtils.nextDouble(0, 1);
	    	
	    	int selectedFrame=-1;		
			for(int index =0; index<probSelectionDistribution.size(); index++){
				if (rand <= cumulativePVector.get(index)) {
					selectedFrame = index;
					break;
				}
			}
	    	
//	    	int selectedFrame = 2523;
			
	    	//Leggo il frame con l'indice scelto
			System.out.println("Selected frame: " + selectedFrame);
			FrameBean<DiscoursePreCondition> frameBean = frameMap.readByKey(selectedFrame);
	    	
	    	//Stampo il frame scelto
	    	frameBean.print();
	    	
	    	//Forzo le precondizioni
	    	System.out.println("\nForcing pre-conditions...");
	    	ArrayList<DiscoursePreCondition> preConditionList = DiscoursePreCondition.getAllDiscoursePreConditions(baseURL,apiUsername,apiKey);
	    	System.out.println("Pre-conditions created!");
	    	
//	    	//Creo una APIRequest con i campi del Frame estratto
	    	APIRequest<DiscoursePreCondition> apiRequest = new APIRequest<DiscoursePreCondition>(frameBean, preConditionList);
	    	apiRequest.generateParamValues();
	    	apiRequest.setBaseURL(baseURL);
	    	apiRequest.setApiUsername(apiUsername);
	    	apiRequest.setApiKey(apiKey);
	    	
//	    	for(Param<DiscoursePreCondition> p : apiRequest.getParamList()){
//	    		p.generateValue(preConditionList);
//	    		System.out.println(p.getClassParam());
//	    		System.out.println(p.getValue());
//	    	}
	    	
	    	Response response = apiRequest.sendRequest();
	    	
	    	String stringResponseBody = null;
	    	try {
	    		stringResponseBody = response.body().string();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	    	
	    	ResponseLogList<DiscoursePreCondition> responseLogList = new ResponseLogList<DiscoursePreCondition>("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/response_log");
	    	ResponseLog<DiscoursePreCondition> responseLog = new ResponseLog<DiscoursePreCondition>(Integer.toString(selectedFrame, 10), response.code(), response.message(), stringResponseBody, apiRequest.getParamList());
	    	
	    	System.out.println("");
	    	responseLog.print();
	    	
	    	responseLogList.add(responseLog);
	    	responseLogList.saveToFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/response_log");
	    	
    }
}
