package it.alessandrochillemi.tesi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrameMap;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLogList;

public class ExperimentStarter {
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	public static final String ENVIRONMENT_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
	//Percentuale di variazione della distribuzione di probabilità di selezione vera rispetto alla distribuzione di probabilità "stimata"
	private static final Double VARIATION = 0.3;
	
	//Numero di cicli di test da effettuare
	public static final int NCYCLES = 5;
	
	//Numero di test da eseguire a ogni ciclo
	public static final int NTESTS = 100;
	
	//Numero di richieste da inviare a ogni ciclo
	public static final int NREQUESTS = 1000;

	private static String apiDescriptionsFilePath;
	private static String frameMapFilePath;
	private static String testResponseLogListPath;
	private static String userResponseLogListPath;
	private static String reliabilityFilePath;
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
		apiDescriptionsFilePath = environment.getProperty("api_descriptions_file_path");
		frameMapFilePath = environment.getProperty("frame_map_file_path");
		testResponseLogListPath = environment.getProperty("test_response_log_list_path");
		userResponseLogListPath = environment.getProperty("user_response_log_list_path");
		reliabilityFilePath = environment.getProperty("reliability_file_path");
		baseURL = environment.getProperty("base_url");
		apiUsername = environment.getProperty("api_username");
		apiKey = environment.getProperty("api_key");
	}

	public static void main(String[] args) {
		
		//Carico le variabili d'ambiente
		loadEnvironment();
			
		DiscourseFrameMap discourseFrameMap = null;
		//Carico la frame map; se il file non esiste, lo genero a partire dalla descrizione delle API
		if(Files.exists(Paths.get(frameMapFilePath))){
			discourseFrameMap = new DiscourseFrameMap(frameMapFilePath);
		}
		else{
			//Creo una nuova frameMap
			discourseFrameMap = new DiscourseFrameMap(apiDescriptionsFilePath, (1.0/8802.0), 0.0, 0.0, 0.0);
			
			//Ottengo la distribuzione della probabilità di selezione vera a partire dalla frameMap esistente, con una variazione proporzionale a +-Variation
			ArrayList<Double> trueProbSelectionDistribution = TrueProbSelectionDistributionGenerator.generateTrueProbSelectionDistributiion(discourseFrameMap, VARIATION);
			
			//Aggiorno la distribuzione della probabilità di selezione vera della frameMap
			discourseFrameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);
			
			//Salvo la frameMap
			discourseFrameMap.writeToCSVFile(frameMapFilePath);
		}
		
		//Scelgo la strategia di testing
		ITestingStrategy testingStrategy = new FirstTestingStrategy();
		
		//Creo un test generator
		TestGenerator testGenerator = new TestGenerator(testingStrategy);
		
		//Creo un workload generator
		WorkloadGenerator workloadGenerator = new WorkloadGenerator(testingStrategy);
		
		//Creo uno stimatore della reliability
		ReliabilityEstimator reliabilityEstimator = new ReliabilityEstimator(testingStrategy);
		
		for(int i = 0; i<NCYCLES; i++){
			System.out.println("\nCiclo " + (i+1) + " avviato");
			
			//Eseguo NTESTS selezionandoli dalla frame map ottenuta e ottengo le risposte
			DiscourseResponseLogList discourseTestResponseLogList = testGenerator.generateTests(baseURL, apiUsername, apiKey, discourseFrameMap, NTESTS);

			//Salvo le risposte su un file
			String testResponseLogListFileName = "test_response_log_list_cycle"+i+".csv";
			String testResponseLogListFilePath = Paths.get(testResponseLogListPath, testResponseLogListFileName).toString();
			discourseTestResponseLogList.writeToCSVFile(testResponseLogListFilePath);			
			System.out.println("\nTest eseguiti");

			//Calcolo le reliability
			reliabilityEstimator.computeReliability(discourseTestResponseLogList);
			reliabilityEstimator.computeReliabilityForCriticalFailures(discourseTestResponseLogList);
			System.out.println("\nReliability calcolata");
			
			//Eseguo NREQUESTS selezionandole dalla frame map e ottengo le risposte
			DiscourseResponseLogList discourseUserResponseLogList = workloadGenerator.generateRequests(baseURL, apiUsername, apiKey, discourseFrameMap, NREQUESTS);
			
			//Salvo le risposte su un file
			String userResponseLogListFileName = "user_response_log_list_cycle"+i+".csv";
			String userResponseLogListFilePath = Paths.get(userResponseLogListPath, userResponseLogListFileName).toString();
			discourseUserResponseLogList.writeToCSVFile(userResponseLogListFilePath);			
			System.out.println("\nRichieste eseguite");
			
			//Aggiorno il file contenente le reliability calcolate finora
			reliabilityEstimator.appendToFile(reliabilityFilePath);
		}

	}

}
