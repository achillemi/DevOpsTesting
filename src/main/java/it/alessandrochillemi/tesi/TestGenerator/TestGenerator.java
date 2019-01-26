package it.alessandrochillemi.tesi.TestGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import it.alessandrochillemi.tesi.FrameUtils.APIRequest;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrame;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrameMap;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseParam;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLogList;
import okhttp3.Response;

public class TestGenerator{
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	public static final String ENVIRONMENT_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
	private String frameMapPath;
	private String responseLogListPath;
	private String baseURL;
	private String apiUsername;
	private String apiKey;
	
	//Strategy design pattern
	private ITestSelectionStrategy testSelectionStrategy;
	
	public TestGenerator(){
		
	}
	
	public TestGenerator(ITestSelectionStrategy testSelectionStrategy){
		this.testSelectionStrategy = testSelectionStrategy;
	}
	
	public void setTestSelectionStrategy(ITestSelectionStrategy testSelectionStrategy) {
		this.testSelectionStrategy = testSelectionStrategy;
	}

	private void loadEnvironment(){
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
	
    public void generateTests(){
    	//Carico le variabili d'ambiente (path della lista di testframe, api_key, api_username, ecc.)
    	loadEnvironment();

    	DiscourseFrameMap frameMap = new DiscourseFrameMap(frameMapPath);

    	//Scelgo un frame secondo l'algoritmo selezionato (pattern Strategy)
    	ArrayList<Double> probSelectionDistribution = frameMap.getProbSelectionDistribution();
    	int selectedFrame = testSelectionStrategy.selectFrame(probSelectionDistribution);

    	//Leggo il frame con l'indice scelto
    	System.out.println("Selected frame: " + selectedFrame);
    	DiscourseFrame frame = frameMap.readByKey(selectedFrame);

    	//Stampo il frame scelto
    	frame.print();

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
//    	String stringResponseBody = null;
//    	try {
//    		stringResponseBody = response.body().string();
//    	} catch (IOException e2) {
//    		// TODO Auto-generated catch block
//    		e2.printStackTrace();
//    	} finally{
//    		response.body().close();
//    	}
    	response.close();
    	

    	//Salvo la risposta nella ResponseLogList esistente o in una nuova
    	DiscourseResponseLogList responseLogList = null;
    	
    	if(Files.exists(Paths.get(responseLogListPath))){
    		responseLogList = new DiscourseResponseLogList(responseLogListPath);
    	}
    	else{
    		responseLogList = new DiscourseResponseLogList();
    	}
    	
    	DiscourseResponseLog responseLog = new DiscourseResponseLog(Integer.toString(selectedFrame, 10), responseCode, responseMessage, apiRequest.getParamList());
   
    	System.out.println("");
    	responseLog.print();

    	responseLogList.add(responseLog);
    	responseLogList.writeToCSVFile(responseLogListPath);

    }
}
