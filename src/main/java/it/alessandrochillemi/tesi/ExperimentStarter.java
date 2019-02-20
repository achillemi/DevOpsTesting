package it.alessandrochillemi.tesi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;

import it.alessandrochillemi.tesi.frameutils.ApplicationFactory;
import it.alessandrochillemi.tesi.frameutils.FrameMap;
import it.alessandrochillemi.tesi.frameutils.ResponseLogList;
import it.alessandrochillemi.tesi.frameutils.discourse.DiscourseFactory;
import it.alessandrochillemi.tesi.testingstrategies.FirstTestingStrategy;
import it.alessandrochillemi.tesi.testingstrategies.TestingStrategy;
import it.alessandrochillemi.tesi.wlgenerator.WorkloadGenerator;

public class ExperimentStarter {
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	public static String ENVIRONMENT_FILE_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
	//Numero di cicli di test da effettuare
	public static int NCYCLES = 8;
	
	//Numero di test da eseguire a ogni ciclo
	public static int NTESTS = 1000;
	
	//Numero di richieste da inviare a ogni ciclo
	public static int NREQUESTS = 5000;
	
	//Learning rate per l'aggiornamento delle distribuzioni di probabilità
	public static Double LEARNING_RATE = 0.1;

	private static String frameMapFilePath;
	private static String preliminaryResponseLogListFilePath;
	private static String experimentResponsesPath;
	private static String baseURL;
	private static String apiUsername;
	private static String apiKey;

	private static void loadEnvironment(String[] args){
		
		//Carico i parametri inseriti dall'utente
		NCYCLES = Integer.valueOf(args[0]);
		NTESTS = Integer.valueOf(args[1]);
		NREQUESTS = Integer.valueOf(args[2]);
		LEARNING_RATE = Double.valueOf(args[3]);
		ENVIRONMENT_FILE_PATH = args[4];
		
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
		experimentResponsesPath = environment.getProperty("experiment_responses_path");
		baseURL = environment.getProperty("base_url");
		apiUsername = environment.getProperty("api_username");
		apiKey = environment.getProperty("api_key");
	}

	public static void main(String[] args) {
		
		if(args.length != 5){
			System.err.println("\nInserire tutti i parametri!");
			return;
		}
		
		//Carico le variabili d'ambiente
		loadEnvironment(args);
		
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
		
		//Scelgo la strategia di testing
		TestingStrategy testingStrategy = new FirstTestingStrategy();
		
		//Creo un test generator
		TestGenerator testGenerator = new TestGenerator(testingStrategy);
		
		//Creo un workload generator
		WorkloadGenerator workloadGenerator = new WorkloadGenerator(testingStrategy);
		
		//Creo la directory e le sottodirectory nelle quali salvo le risposte
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss"));
		String responseDirectoryString = Paths.get(experimentResponsesPath,new String("esperimento_" + timestamp)).toString();
		File responseDirectory = new File(responseDirectoryString);
		responseDirectory.mkdirs();
		
		String testResponseDirectoryString = Paths.get(responseDirectoryString, "test_responses").toString();
		File testResponseDirectory = new File(testResponseDirectoryString);
		testResponseDirectory.mkdirs();
		
		String userResponseDirectoryString = Paths.get(responseDirectoryString, "user_responses").toString();
		File userResponseDirectory = new File(userResponseDirectoryString);
		userResponseDirectory.mkdirs();
		
		
		//Creo uno stimatore della reliability
		ReliabilityEstimator reliabilityEstimator = new ReliabilityEstimator(testingStrategy);
		
		//Stimo la reliability vera a partire dalle richieste eseguite preventivamente secondo la probabilità di selezione vera per la stima della reliability
		ResponseLogList reliabilityResponseLogList = applicationFactory.makeResponseLogList(preliminaryResponseLogListFilePath);
		reliabilityEstimator.computeTrueReliability(reliabilityResponseLogList);
		reliabilityEstimator.computeTrueReliabilityForCriticalFailures(reliabilityResponseLogList);
		
		//Creo un monitor
		Monitor monitor = new Monitor();
		
		for(int i = 0; i<NCYCLES; i++){
			System.out.println("\nCiclo " + (i+1) + " avviato");
			
			//Eseguo NTESTS test selezionando i frame dalla frame map ottenuta e ottengo le risposte
			ResponseLogList testResponseLogList = testGenerator.generateTests(baseURL, apiUsername, apiKey, frameMap, NTESTS, applicationFactory);
		
			//Salvo le risposte ai test su un file
			String testResponseLogListFileName = "test_response_log_list_cycle"+(i+1)+".csv";
			String testResponseLogListFilePath = Paths.get(testResponseDirectoryString, testResponseLogListFileName).toString();
			testResponseLogList.writeToCSVFile(testResponseLogListFilePath);			
			System.out.println("\nTest eseguiti");

			//Calcolo le reliability
			reliabilityEstimator.computeReliability(testResponseLogList);
			reliabilityEstimator.computeReliabilityForCriticalFailures(testResponseLogList);
			System.out.println("\nReliability calcolata");
			
			//Aggiorno il file contenente le reliability calcolate finora
			reliabilityEstimator.appendToFile(Paths.get(responseDirectoryString,"reliability.csv").toString());
			
			//Eseguo NREQUESTS richieste selezionando i frame dalla frame map e ottengo le risposte
			ResponseLogList userResponseLogList = workloadGenerator.generateRequests(baseURL, apiUsername, apiKey, frameMap, NREQUESTS, applicationFactory);
			
			//Salvo le risposte alle richieste su un file
			String userResponseLogListFileName = "user_response_log_list_cycle"+(i+1)+".csv";
			String userResponseLogListFilePath = Paths.get(userResponseDirectoryString, userResponseLogListFileName).toString();
			userResponseLogList.writeToCSVFile(userResponseLogListFilePath);			
			System.out.println("\nRichieste eseguite");
			
			//Ottengo le attuali probSelection, probFailure e probCriticalFailure
			ArrayList<Double> oldProbSelectionDistribution = frameMap.getProbSelectionDistribution();
			ArrayList<Double> oldProbFailureDistribution = frameMap.getProbFailureDistribution();
			ArrayList<Double> oldProbCriticalFailureDistribution = frameMap.getProbCriticalFailureDistribution();
			
			//Ottengo le probSelection, probFailure e probCriticalFailure per il ciclo successivo
			ArrayList<Double> newProbSelectionDistribution = monitor.updateProbSelectionDistribution(oldProbSelectionDistribution, userResponseLogList, LEARNING_RATE);
			ArrayList<Double> newProbFailureDistribution = monitor.updateProbFailureDistribution(oldProbFailureDistribution, userResponseLogList, LEARNING_RATE);
			ArrayList<Double> newProbCriticalFailureDistribution = monitor.updateProbCriticalFailureDistribution(oldProbCriticalFailureDistribution, userResponseLogList, LEARNING_RATE);
			
			//Aggiorno la frame map con le nuove distribuzioni
			frameMap.setProbSelectionDistribution(newProbSelectionDistribution);
			frameMap.setProbFailureDistribution(newProbFailureDistribution);
			frameMap.setProbCriticalFailureDistribution(newProbCriticalFailureDistribution);
		}
		
		System.out.println("\nTesting terminato!");

	}

}
