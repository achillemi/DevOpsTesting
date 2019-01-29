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
	public static final String ENVIRONMENT_FILE_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
	//Percentuale di variazione della distribuzione di probabilità di selezione vera rispetto alla distribuzione di probabilità "stimata"
	private static final Double VARIATION = 0.3;
	
	//Numero di cicli di test da effettuare
	public static final int NCYCLES = 5;
	
	//Numero di test da eseguire a ogni ciclo
	public static final int NTESTS = 100;
	
	//Numero di richieste da inviare a ogni ciclo
	public static final int NREQUESTS = 1000;
	
	//Learning rate per l'aggiornamento delle distribuzioni di probabilità
	public static final Double LEARNING_RATE = 0.5;

	private static String apiDescriptionsFilePath;
	private static String frameMapFilePath;
	private static String reliabilityResponseLogListFilePath;
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
		apiDescriptionsFilePath = environment.getProperty("api_descriptions_file_path");
		frameMapFilePath = environment.getProperty("frame_map_file_path");
		reliabilityResponseLogListFilePath = environment.getProperty("reliability_response_log_list_file_path");
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
			discourseFrameMap = new DiscourseFrameMap(apiDescriptionsFilePath, (1.0/8802.0), 0.0, 0.0, 0.0, 0.0, 0.0);
			
			//Ottengo la distribuzione della probabilità di selezione vera a partire dalla frameMap esistente, con una variazione proporzionale a +-Variation
			ArrayList<Double> trueProbSelectionDistribution = TrueProbSelectionDistributionGenerator.generateTrueProbSelectionDistribution(discourseFrameMap, VARIATION);
			
			//Aggiorno la distribuzione della probabilità di selezione vera della frameMap
			discourseFrameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);
			
			//Salvo la frameMap
			discourseFrameMap.writeToCSVFile(frameMapFilePath);
			
			//Se ho creato una nuova frameMap, termino il programma per permettere di fare delle richieste preventive secondo la distribuzione della probabilità
			//di selezione vera per stimare la reliability vera.
			System.out.println("\nNuova Frame Map creata!");
			return;
		}
		
		//Scelgo la strategia di testing
		ITestingStrategy testingStrategy = new FirstTestingStrategy();
		
		//Creo un test generator
		TestGenerator testGenerator = new TestGenerator(testingStrategy);
		
		//Creo un workload generator
		WorkloadGenerator workloadGenerator = new WorkloadGenerator(testingStrategy);
		
		//Creo uno stimatore della reliability
		ReliabilityEstimator reliabilityEstimator = new ReliabilityEstimator(testingStrategy);
		
		//Stimo la reliability vera a partire dalle richieste eseguite preventivamente secondo la probabilità di selezione vera per la stima della reliability
		DiscourseResponseLogList reliabilityDiscourseResponseLogList = new DiscourseResponseLogList(reliabilityResponseLogListFilePath);
		reliabilityEstimator.computeTrueReliability(reliabilityDiscourseResponseLogList);
		reliabilityEstimator.computeTrueReliabilityForCriticalFailures(reliabilityDiscourseResponseLogList);
		
		//Creo un monitor
		Monitor monitor = new Monitor();
		
		for(int i = 0; i<NCYCLES; i++){
			System.out.println("\nCiclo " + (i+1) + " avviato");
			
			//Eseguo NTESTS selezionandoli dalla frame map ottenuta e ottengo le risposte
			DiscourseResponseLogList discourseTestResponseLogList = testGenerator.generateTests(baseURL, apiUsername, apiKey, discourseFrameMap, NTESTS);

			//Salvo le risposte ai test su un file
			String testResponseLogListFileName = "test_response_log_list_cycle"+(i+1)+".csv";
			String testResponseLogListFilePath = Paths.get(testResponseLogListPath, testResponseLogListFileName).toString();
			discourseTestResponseLogList.writeToCSVFile(testResponseLogListFilePath);			
			System.out.println("\nTest eseguiti");

			//Calcolo le reliability
			reliabilityEstimator.computeReliability(discourseTestResponseLogList);
			reliabilityEstimator.computeReliabilityForCriticalFailures(discourseTestResponseLogList);
			System.out.println("\nReliability calcolata");
			
			//Aggiorno il file contenente le reliability calcolate finora
			reliabilityEstimator.appendToFile(reliabilityFilePath);
			
			//Eseguo NREQUESTS selezionandole dalla frame map e ottengo le risposte
			DiscourseResponseLogList discourseUserResponseLogList = workloadGenerator.generateRequests(baseURL, apiUsername, apiKey, discourseFrameMap, NREQUESTS);
			
			//Salvo le risposte alle richieste su un file
			String userResponseLogListFileName = "user_response_log_list_cycle"+(i+1)+".csv";
			String userResponseLogListFilePath = Paths.get(userResponseLogListPath, userResponseLogListFileName).toString();
			discourseUserResponseLogList.writeToCSVFile(userResponseLogListFilePath);			
			System.out.println("\nRichieste eseguite");
			
			//Ottengo le attuali probSelection, probFailure e probCriticalFailure
			ArrayList<Double> oldProbSelectionDistribution = discourseFrameMap.getProbSelectionDistribution();
			ArrayList<Double> oldProbFailureDistribution = discourseFrameMap.getProbFailureDistribution();
			ArrayList<Double> oldProbCriticalFailureDistribution = discourseFrameMap.getProbCriticalFailureDistribution();
			
			//Ottengo le probSelection, probFailure e probCriticalFailure per il ciclo successivo
			ArrayList<Double> newProbSelectionDistribution = monitor.updateProbSelectionDistribution(oldProbSelectionDistribution, discourseUserResponseLogList, LEARNING_RATE);
			ArrayList<Double> newProbFailureDistribution = monitor.updateProbFailureDistribution(oldProbFailureDistribution, discourseUserResponseLogList, LEARNING_RATE);
			ArrayList<Double> newProbCriticalFailureDistribution = monitor.updateProbCriticalFailureDistribution(oldProbCriticalFailureDistribution, discourseUserResponseLogList, LEARNING_RATE);
			
			//Aggiorno la frame map con le nuove distribuzioni
			discourseFrameMap.setProbSelectionDistribution(newProbSelectionDistribution);
			discourseFrameMap.setProbFailureDistribution(newProbFailureDistribution);
			discourseFrameMap.setProbCriticalFailureDistribution(newProbCriticalFailureDistribution);
		}
		
		System.out.println("\nTesting terminato!");

	}

}
