package it.alessandrochillemi.tesi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import it.alessandrochillemi.tesi.FrameUtils.APIRequest;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrame;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrameMap;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseOracle;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseParam;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLogList;
import it.alessandrochillemi.tesi.TestGenerator.FirstSelectionAlgorithm;
import it.alessandrochillemi.tesi.TestGenerator.ITestSelectionStrategy;
import okhttp3.Response;

public class TrueProbFailureDistributionGenerator {
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	public static final String ENVIRONMENT_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
	private static final int NREQUESTS = 10000;

	private static String frameMapPath;
	private static String responseLogListPath;
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
		responseLogListPath = environment.getProperty("response_log_list_path");
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
    	
    	//Ottengo la distribuzione di probabilità di selezione vera dei frame
    	ArrayList<Double> trueProbSelectionDistribution = frameMap.getTrueProbSelectionDistribution();
    	
    	//Imposto l'algoritmo di selezione dei frame
    	ITestSelectionStrategy testSelectionStrategy = new FirstSelectionAlgorithm();
    	
    	//Creo un oracolo per distinguere i fallimenti dalle richieste andate a buon fine
    	DiscourseOracle oracle = new DiscourseOracle();
    	
    	//Apro la response log list
    	DiscourseResponseLogList responseLogList = new DiscourseResponseLogList(responseLogListPath);
    	
    	
    	for(int i = 1051; i<NREQUESTS; i++){
    		System.out.println("\nRichiesta " + (i+1) + "...");
    		
        	//Scelgo il prossimo frame selezionadolo secondo la distribuzione di probabilità di selezione vera
        	int selectedFrame = testSelectionStrategy.selectFrame(trueProbSelectionDistribution);
        	System.out.println("Selected frame: " + selectedFrame);
        	DiscourseFrame frame = frameMap.readByKey(selectedFrame);
        	
        	//Stampo il frame scelto
//        	frame.print();

        	//Genero i valori dei parametri applicando le precondizioni
        	for(DiscourseParam p : frame.getParamList()){
        		p.generateValueWithPreConditions(baseURL,apiUsername,apiKey,true);
        	}
        	
        	//Creo una APIRequest con i campi del Frame estratto
        	APIRequest<DiscourseParam> apiRequest = new APIRequest<DiscourseParam>(frame);
        	apiRequest.setBaseURL(baseURL);
        	apiRequest.setApiUsername(apiUsername);
        	apiRequest.setApiKey(apiKey);

        	//Invio la richiesta
        	Response response = apiRequest.sendRequest();
        	
        	//Salvo i risultati e chiudo la risposta
        	int responseCode = response.code();
        	String responseMessage = response.message();
//        	String stringResponseBody = null;
//        	try {
//        		stringResponseBody = response.body().string();
//        	} catch (IOException e2) {
//        		// TODO Auto-generated catch block
//        		e2.printStackTrace();
//        	} finally{
//        		response.body().close();
//        	}
        	response.close();
        	
        	//Creo un response log per la risposta ottenuta, lo stampo e lo aggiungo alla response log list
        	DiscourseResponseLog responseLog = new DiscourseResponseLog(Integer.toString(selectedFrame, 10), responseCode, responseMessage, apiRequest.getParamList());
        	
//        	System.out.println("");
//        	responseLog.print();
        	
        	responseLogList.add(responseLog);
        	
        	//Ogni 50 richieste, salvo la response log list aggiornata
	    	if(i%50 == 0){
	    		//Salvo la response log list
	        	responseLogList.writeToCSVFile(responseLogListPath);
	    		System.out.println("\nResponseLogList salvata!");
	    	}
    	}
    	
    	//Salvo la response log list
    	responseLogList.writeToCSVFile(responseLogListPath);
    	
    	//Per ogni frame calcolo il numero di richieste effettuate e il numero di fallimenti effettuati, dopodiché aggiorno la probabilità di fallimento come
    	//la proporzione di fallimenti sul numero di richieste
    	ArrayList<Double> trueProbFailureDistribution = new ArrayList<Double>();
    	int requestsCount = 0;
    	int failuresCount = 0;
    	for(int i = 0; i<frameMap.size(); i++){
    		requestsCount = responseLogList.count(String.valueOf(i));
    		failuresCount = oracle.getFrameFailures(responseLogList, String.valueOf(i));
    		Double trueProbFailure = 0.0;
    		if(requestsCount != 0){
    			trueProbFailure = 1d * failuresCount/requestsCount;
    		}
    		trueProbFailureDistribution.add(trueProbFailure);
    	}
    	
    	//Aggiungo la distribuzione di probabilità di fallimento vera alla frame map
    	frameMap.setTrueProbFailureDistribution(trueProbFailureDistribution);
    	
    	//Salvo la frame map
    	frameMap.writeToCSVFile(frameMapPath);

//    	//Ottengo la distribuzione iniziale
//    	ArrayList<Double> trueProbFailureList = frameMap.getTrueProbFailureDistribution();
//    	
//    	//Scorro tutti i frame della FrameMap
//    	for(int i=0; i<frameMap.size(); i++){
//    		System.out.println("\nFrame " + (i+1) + "...");
//    		
//    		//Ottengo il prossimo frame
//    		DiscourseFrame frame = frameMap.readByKey(i);
//			
//			ResponseLogList<ResponseLog<DiscourseParam>> responseLogList = new ResponseLogList<ResponseLog<DiscourseParam>>();
//
//	    	//Eseguo NREQUESTS richieste per il frame estratto
//	    	for(int j = 0; j<NREQUESTS; j++){
//	    		System.out.println("\nRichiesta " + (j+1) + "...");
//	    		
//	    		//Creo una APIRequest con i campi del Frame estratto
//		    	APIRequest<DiscourseParam> apiRequest = new APIRequest<DiscourseParam>(frame);
//		    	apiRequest.setBaseURL(baseURL);
//		    	apiRequest.setApiUsername(apiUsername);
//		    	apiRequest.setApiKey(apiKey);
//	    		
//	    		//Genero nuovi valori per i parametri applicando le precondizioni
//	    		apiRequest.generateNewParamValuesWithPreConditions(true);
//	    		
//	    		//Invio la richiesta
//	        	Response response = apiRequest.sendRequest();
//	        	
//	        	//Estraggo i campi dalla risposta
//	        	int responseCode = response.code();
//	        	String responseMessage = response.message();
//	        	String stringResponseBody = null;
//	        	try {
//	        		stringResponseBody = response.body().string();
//	        	} catch (IOException e2) {
//	        		// TODO Auto-generated catch block
//	        		e2.printStackTrace();
//	        	} finally{
//	        		response.body().close();
//	        	}
//	        	
//	        	//Salvo i campi della risposta in un ResponseLog e lo aggiungo alla ResponseLogList
//	        	ResponseLog<DiscourseParam> responseLog = new ResponseLog<DiscourseParam>(Integer.toString(i, 10), responseCode, responseMessage, stringResponseBody, apiRequest.getParamList());
//	        	responseLogList.add(responseLog);
//	    	}
//	    	
//	    	//Calcolo il numero di fallimenti per questo frame
//	    	int totalNumberOfFailures = oracle.getTotalNumberOfFailures(responseLogList);
//	    	
//	    	//Ottengo la probabilità di fallimento vera dividendo il numero di fallimenti per il numero di richieste
//	    	Double trueProbFailure = (new Double(totalNumberOfFailures))/(new Double(NREQUESTS));
//	    	
//	    	//Aggiungo la probabilità di fallimento vera del frame alla lista di tutte le probabilità di fallimento vere
//	    	trueProbFailureList.set(i, trueProbFailure);
//	    	
//	    	//Ogni 50 frame, salvo la frameMap aggiornata
//	    	if(i%50 == 0){
//	    		responseLogList.saveToFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/response_log_list");
//	    		
//	    		frameMap.setTrueProbFailureDistribution(trueProbFailureList);
//	    		System.out.println("\nTrue probability selection distribution aggiornata!");
//
//	    		frameMap.writeToCSVFile(frameMapPath);
//	    		System.out.println("\nFrameMap salvata!");
//	    	}
//    	}
//    	
//    	frameMap.setTrueProbFailureDistribution(trueProbFailureList);
//    	System.out.println("\nTrue probability failure distribution aggiornata!");
//    	
//    	frameMap.writeToCSVFile(frameMapPath);
//    	System.out.println("\nFrameMap salvata!");

	}

}
