package it.alessandrochillemi.tesi.WLGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.lang3.RandomUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
	    	
	    	FrameMap frameMap = new FrameMap(frameMapPath);
	    	
	    	//Scelgo il frame secondo la probabilità di selezione impostata con l'ausilio di un vettore che memorizza la probabilità cumulata
//	    	ArrayList<Double> probSelectionDistribution = frameMap.getProbSelectionDistribution();
//	    	ArrayList<Double> cumulativePVector = new ArrayList<Double>();
//	    	
//	    	cumulativePVector.add(probSelectionDistribution.get(0));
//	    	
//	    	for(int i = 1; i<probSelectionDistribution.size(); i++){
//	    		Double d = cumulativePVector.get(i-1)+probSelectionDistribution.get(i);
//	    		cumulativePVector.add(d);
//	    	}
//	    	
//	    	double rand = RandomUtils.nextDouble(0, 1);
//	    	
//	    	int selectedFrame=-1;		
//			for(int index =0; index<probSelectionDistribution.size(); index++){
//				if (rand <= cumulativePVector.get(index)) {
//					selectedFrame = index;
//					break;
//				}
//			}
	    	
	    	//Leggo il frame con l'indice scelto
//			System.out.println("Selected frame: " + selectedFrame);
//	    	FrameBean frameBean = frameMap.readByKey(selectedFrame);
	    	int selectedFrame = 1042;
			FrameBean frameBean = frameMap.readByKey(selectedFrame);
	    	
	    	//Stampo il frame scelto
	    	frameBean.print();
	    	
//	    	System.out.println("");
	    	
	    	//Forzo le precondizioni
	    	ArrayList<PreCondition> preConditionList = DiscoursePreCondition.generateDiscoursePreConditions();
	    	
//	    	//Creo una APIRequest con i campi del Frame estratto
	    	APIRequest apiRequest = new APIRequest(frameBean, preConditionList);
	    	apiRequest.generateValue();
	    	apiRequest.setBaseURL(baseURL);
	    	apiRequest.setApiUsername(apiUsername);
	    	apiRequest.setApiKey(apiKey);
	    	
	    	Response response = apiRequest.sendRequest();
	    	
	    	String stringResponseBody = null;
	    	try {
	    		stringResponseBody = response.body().string();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	    	
	    	ResponseLogList responseLogList = new ResponseLogList();
	    	ResponseLog responseLog = new ResponseLog(Integer.toString(selectedFrame, 10), response.code(), response.message(), stringResponseBody, apiRequest.getParamList());
	    	
	    	responseLogList.add(responseLog);
	    	System.out.println(responseLogList.count(Integer.toString(selectedFrame, 10)));
	    	
    }
}
