package it.alessandrochillemi.tesi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import it.alessandrochillemi.tesi.FrameUtils.ApplicationFactory;
import it.alessandrochillemi.tesi.FrameUtils.FrameMap;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLogList;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFactory;

public class TrueReliabilityRequestGenerator {

	//Percorso nel quale si trova il file con le variabili di ambiente
	public static final String ENVIRONMENT_FILE_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";

	private static String frameMapFilePath;
	private static String reliabilityResponseLogListFilePath;
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
		reliabilityResponseLogListFilePath = environment.getProperty("reliability_response_log_list_file_path");
		baseURL = environment.getProperty("base_url");
		apiUsername = environment.getProperty("api_username");
		apiKey = environment.getProperty("api_key");
	}

	public static void main(String[] args) {

		loadEnvironment();

		//Creo una ApplicationFactory per l'applicazione desiderata
		ApplicationFactory applicationFactory = new DiscourseFactory();

		//Carico la FrameMap
		FrameMap frameMap = applicationFactory.makeFrameMap(frameMapFilePath);

		//Scelgo la strategia di testing
		ITestingStrategy testingStrategy = new FirstTestingStrategy();

		//Creo un workload generator
		WorkloadGenerator workloadGenerator = new WorkloadGenerator(testingStrategy);

		//Eseguo 10000 richieste selezionando i frame dalla frame map ottenuta e ottengo le risposte
		ResponseLogList reliabilityResponseLogList = workloadGenerator.generateRequests(baseURL, apiUsername, apiKey, frameMap, 10000, applicationFactory);

		reliabilityResponseLogList.writeToCSVFile(reliabilityResponseLogListFilePath);			
		System.out.println("\nTest eseguiti");

	}

}
