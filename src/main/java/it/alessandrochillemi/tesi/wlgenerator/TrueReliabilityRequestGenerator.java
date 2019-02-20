package it.alessandrochillemi.tesi.wlgenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import it.alessandrochillemi.tesi.frameutils.ApplicationFactory;
import it.alessandrochillemi.tesi.frameutils.FrameMap;
import it.alessandrochillemi.tesi.frameutils.ResponseLogList;
import it.alessandrochillemi.tesi.frameutils.discourse.DiscourseFactory;
import it.alessandrochillemi.tesi.testingstrategies.FirstTestingStrategy;
import it.alessandrochillemi.tesi.testingstrategies.TestingStrategy;

public class TrueReliabilityRequestGenerator {

	//Percorso nel quale si trova il file con le variabili di ambiente
	public static final String ENVIRONMENT_FILE_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";

	private static String frameMapFilePath;
	private static String preliminaryResponseLogListFilePath;
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
		preliminaryResponseLogListFilePath = environment.getProperty("preliminary_response_log_list_file_path");
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
		TestingStrategy testingStrategy = new FirstTestingStrategy(frameMap);

		//Creo un workload generator
		WorkloadGenerator workloadGenerator = new WorkloadGenerator(testingStrategy);

		//Eseguo 10000 richieste selezionando i frame dalla frame map ottenuta e ottengo le risposte
		ResponseLogList reliabilityResponseLogList = workloadGenerator.generateRequests(baseURL, apiUsername, apiKey, frameMap, 10000, applicationFactory);

		reliabilityResponseLogList.writeToCSVFile(preliminaryResponseLogListFilePath);			
		System.out.println("\nTest eseguiti");

	}

}
