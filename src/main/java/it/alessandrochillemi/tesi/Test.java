package it.alessandrochillemi.tesi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import it.alessandrochillemi.tesi.frameutils.ApplicationFactory;
import it.alessandrochillemi.tesi.frameutils.Frame;
import it.alessandrochillemi.tesi.frameutils.FrameMap;
import it.alessandrochillemi.tesi.frameutils.Param;
import it.alessandrochillemi.tesi.frameutils.discourse.DiscourseFactory;
import okhttp3.Response;

public class Test {
	//Percorso nel quale si trova il file con le variabili di ambiente
	public static String ENVIRONMENT_FILE_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";

	private static String frameMapFilePath;
	private static String baseURL;
	private static String apiUsername;
	private static String apiKey;

	private static void loadEnvironment(){

		//Carico le variabili d'ambiente (path della lista di testframe, api_key, api_username, ecc.)
		Properties environment = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(ENVIRONMENT_FILE_PATH);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			environment.load(is);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//Leggo le variabili d'ambiente
		frameMapFilePath = environment.getProperty("frame_map_file_path");
		baseURL = environment.getProperty("base_url");
		apiUsername = environment.getProperty("api_username");
		apiKey = environment.getProperty("api_key");
	}

	public static void main(String[] args) {

		//Carico le variabili d'ambiente
		loadEnvironment();

		//Creo una ApplicationFactory per l'applicazione desiderata
		ApplicationFactory applicationFactory = new DiscourseFactory();

		FrameMap frameMap = null;
		//Carico la frame map; se il file non esiste, esco dal programma
		if(Files.exists(Paths.get(frameMapFilePath))){
			frameMap = applicationFactory.makeFrameMap(frameMapFilePath);
		}
		else{
			System.out.println("\nFrame Map non trovata!");
			return;
		}
		
//		ArrayList<Double> trueProbSelectionDistribution = frameMap.getTrueProbSelectionDistribution();
//		ArrayList<Double> trueProbFailureDistribution = frameMap.getTrueProbFailureDistribution();
//		ArrayList<Double> trueProbCriticalFailureDistribution = frameMap.getTrueProbCriticalFailureDistribution();
//		
//		Double failProb = 0.0;
//		for(int i = 0; i<trueProbSelectionDistribution.size(); i++){
//			Double d = trueProbSelectionDistribution.get(i)*trueProbFailureDistribution.get(i);
//			failProb += d;
//		}
//		
//		Double trueReliability = 1d - failProb;
//		
//		Double criticalFailProb = 0.0;
//		for(int i = 0; i<trueProbSelectionDistribution.size(); i++){
//			Double d = trueProbSelectionDistribution.get(i)*trueProbCriticalFailureDistribution.get(i);
//			criticalFailProb += d;
//		}
//		
//		Double trueReliabilityForCriticalFailures = 1d - criticalFailProb;
//		
//		System.out.println("\nReliability: " + trueReliability);
//		System.out.println("\nReliability for critical failures: " + trueReliabilityForCriticalFailures);
		
		int selectedFrame = 1284;
		
		Frame frame = frameMap.readByKey(selectedFrame);

		//Genero i valori dei parametri applicando le precondizioni
		for(Param p : frame.getParamList()){
			p.generateValueWithPreConditions(baseURL,apiUsername,apiKey,true);
		}

		//Creo una APIRequest con i campi del Frame estratto
		APIRequest apiRequest = new APIRequest(frame);
		apiRequest.setBaseURL(baseURL);
		apiRequest.setApiUsername(apiUsername);
		apiRequest.setApiKey(apiKey);

		//Invio la richiesta
		Response response = apiRequest.sendRequest();

		int responseCode = 0;
		String responseMessage = "";
		//Se la richiesta è andata a buon fine, salvo i risultati e chiudo la risposta
		if(response != null){
    		responseCode = response.code();
    		responseMessage = response.message();
    		response.close();  
		}
		//Se non è andata a buon fine, la considero un fallimento dell'applicazione e lascio vuoto il messaggio di risposta
		else{
			responseCode = 500;
			responseMessage = "";
		}

		System.out.println(responseCode);
	}

}
