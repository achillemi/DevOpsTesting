package it.alessandrochillemi.tesi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import it.alessandrochillemi.tesi.FrameUtils.APIRequest;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLogList;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrame;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrameMap;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseOracle;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseParam;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscoursePreCondition;
import okhttp3.Response;

public class TestApp {
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	public static final String ENVIRONMENT_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
	private static final int NREQUESTS = 100;

	private static String frameMapPath;
	private static String baseURL;
	private static String apiUsername;
	private static String apiKey;
	
	private static void loadEnvironment(){
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
		frameMapPath = environment.getProperty("frame_map_path");
		baseURL = environment.getProperty("base_url");
		apiUsername = environment.getProperty("api_username");
		apiKey = environment.getProperty("api_key");
	}

	//Calcolo la probabilità di fallimento vera per ogni frame
	public static void main(String[] args) {
		//Carico le variabili d'ambiente (path della lista di testframe, api_key, api_username, ecc.)
    	loadEnvironment();

    	//Carico la FrameMap
    	DiscourseFrameMap frameMap = new DiscourseFrameMap(frameMapPath);
    	
    	//Creo le precondizioni
    	System.out.println("\nForcing pre-conditions...");
    	ArrayList<DiscoursePreCondition> preConditionList = DiscoursePreCondition.getAllDiscoursePreConditions(baseURL,apiUsername,apiKey);
    	System.out.println("Pre-conditions created!");
    	
    	DiscourseOracle oracle = new DiscourseOracle();
    	ArrayList<Double> trueProbFailureList = new ArrayList<Double>();
    	
    	//Scorro tutti i frame della FrameMap
    	Iterator<Map.Entry<Integer, DiscourseFrame>> iter = frameMap.iterator();
    	while (iter.hasNext()) {
			Entry<Integer, DiscourseFrame> entry = iter.next();
			System.out.println("\nFrame " + entry.getKey() + "...");
			
			ResponseLogList<ResponseLog<DiscourseParam>> responseLogList = new ResponseLogList<ResponseLog<DiscourseParam>>();
			
			//Ottengo il prossimo frame
			DiscourseFrame frame = entry.getValue();
			
			//Genero i valori dei parametri applicando le precondizioni
	    	for(DiscourseParam p : frame.getParamList()){
	    		p.generateValue(preConditionList);
	    	}
	    	
	    	//Creo una APIRequest con i campi del Frame estratto
	    	APIRequest<DiscourseParam> apiRequest = new APIRequest<DiscourseParam>(frame);
	    	apiRequest.setBaseURL(baseURL);
	    	apiRequest.setApiUsername(apiUsername);
	    	apiRequest.setApiKey(apiKey);

	    	//Eseguo NREQUESTS richieste per il frame estratto
	    	for(int i = 0; i<NREQUESTS; i++){
	    		System.out.println("\nRichiesta " + (i+1) + "...");
	    		
	    		//Invio la richiesta
	        	Response response = apiRequest.sendRequest();
	        	
	        	String stringResponseBody = null;
	        	try {
	        		stringResponseBody = response.body().string();
	        	} catch (IOException e2) {
	        		// TODO Auto-generated catch block
	        		e2.printStackTrace();
	        	}
	        	
	        	//Salvo la risposta in un ResponseLog e lo aggiungo alla ResponseLogList
	        	ResponseLog<DiscourseParam> responseLog = new ResponseLog<DiscourseParam>(Integer.toString(entry.getKey(), 10), response.code(), response.message(), stringResponseBody, apiRequest.getParamList());
	        	responseLogList.add(responseLog);
	    	}
	    	
	    	//Calcolo il numero di fallimenti per questo frame
	    	int totalNumberOfFailures = oracle.getTotalNumberOfFailures(responseLogList);
	    	
	    	//Ottengo la probabilità di fallimento vera dividendo il numero di fallimenti per il numero di richieste
	    	Double trueProbFailure = (new Double(totalNumberOfFailures))/(new Double(NREQUESTS));
	    	
	    	//Aggiungo la probabilità di fallimento vera del frame alla lista di tutte le probabilità di fallimento vere
	    	trueProbFailureList.add(trueProbFailure);
	    	
    	}
    	
    	frameMap.setTrueProbSelectionDistribution(trueProbFailureList);
    	System.out.println("\nTrue probability selection distribution aggiornata!");
    	
    	frameMap.writeToCSVFile(frameMapPath);
    	System.out.println("\nFrameMap salvata!");

	}

}
