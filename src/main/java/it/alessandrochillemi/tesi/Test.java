package it.alessandrochillemi.tesi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang3.RandomUtils;

import it.alessandrochillemi.tesi.frameutils.ApplicationFactory;
import it.alessandrochillemi.tesi.frameutils.Frame;
import it.alessandrochillemi.tesi.frameutils.FrameMap;
import it.alessandrochillemi.tesi.frameutils.Param;
import it.alessandrochillemi.tesi.frameutils.ResponseLogList;
import it.alessandrochillemi.tesi.frameutils.discourse.DiscourseFactory;
import it.alessandrochillemi.tesi.testingstrategies.FirstTestingStrategy;
import it.alessandrochillemi.tesi.testingstrategies.SecondTestingStrategy;
import it.alessandrochillemi.tesi.testingstrategies.TestingStrategy;
import it.alessandrochillemi.tesi.utils.DoubleUtils;
import it.alessandrochillemi.tesi.wlgenerator.TrueProbSelectionGenerator;

@SuppressWarnings("unused")
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

	public static FrameMap generateFramesForFirstStrategy(ApplicationFactory appFactory, String frameMapFilePath){

		//Carico la frame map con le f_vere già calcolate preliminarmente
		FrameMap frameMap = appFactory.makeFrameMap(frameMapFilePath);

		//Carico le distribuzioni di probabilità attuali, che saranno modificate
		ArrayList<Double> probSelectionDistribution = frameMap.getProbSelectionDistribution();
		ArrayList<Double> probFailureDistribution = frameMap.getProbFailureDistribution();
		ArrayList<Double> probCriticalFailureDistribution = frameMap.getProbCriticalFailureDistribution();
		ArrayList<Double> trueProbSelectionDistribution = frameMap.getTrueProbSelectionDistribution();
		ArrayList<Double> trueProbFailureDistribution = frameMap.getTrueProbFailureDistribution();

		TestingStrategy testingStrategy = new FirstTestingStrategy(frameMap);

		int NFrames = frameMap.size();
		System.out.println("Numero di frame: " + NFrames);

		Double failProb = 0.0;
		Double reliability = 0.0;
		Double trueReliability = 0.0;

		Locale currentLocale = Locale.ITALY;
		NumberFormat numberFormatter = NumberFormat.getNumberInstance(currentLocale);
		numberFormatter.setMinimumFractionDigits(16);

		//Generazione probabilità di selezione iniziale stimata (casuale)
		for(int i = 0; i<probSelectionDistribution.size(); i++){
			probSelectionDistribution.set(i, RandomUtils.nextDouble(0,1.0));
		}
		DoubleUtils.normalize(probSelectionDistribution);
		frameMap.setProbSelectionDistribution(probSelectionDistribution);

		//Generazione probabilità di fallimento iniziale stimata (tutte pari a 0)
		for(int i = 0; i<probFailureDistribution.size(); i++){
			probFailureDistribution.set(i, 0.0);
		}
		frameMap.setProbFailureDistribution(probFailureDistribution);

		//Generazione probabilità di fallimento critica iniziale stimata (tutte pari a 0)
		for(int i = 0; i<probCriticalFailureDistribution.size(); i++){
			probCriticalFailureDistribution.set(i, 0.0);
		}
		frameMap.setProbCriticalFailureDistribution(probCriticalFailureDistribution);

		//Generazione probabilità di selezione vera (variazione della probabilità di selezione stimata)
		trueProbSelectionDistribution = TrueProbSelectionGenerator.generateNewProbDistribution(probSelectionDistribution, 0.7, 0.99);
		frameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);

		//Calcolo reliability stimata (p(i)*f_vera(i) perché non ho le f_stimate(i) e non voglio fare un ciclo di test)
		failProb = 0.0;
		for(int i = 0; i<NFrames; i++){
			failProb += probSelectionDistribution.get(i)*trueProbFailureDistribution.get(i);
		}	
		reliability = 1d - failProb;

		//Calcolo reliability vera
		trueReliability = testingStrategy.getTrueReliability();

		System.out.println("TRUE RELIABILITY: " + numberFormatter.format(trueReliability));
		System.out.println("ESTIMATED RELIABILITY: " + numberFormatter.format(reliability));
		System.out.println("RELIABILITY OFFSET: " + numberFormatter.format(Math.abs(reliability-trueReliability)));

		return frameMap;
	}

	public static FrameMap generateFramesForSecondStrategy(ApplicationFactory appFactory, String frameMapFilePath){

		//Carico la frame map con le f_vere già calcolate preliminarmente
		FrameMap frameMap = appFactory.makeFrameMap(frameMapFilePath);

		//Carico le distribuzioni di probabilità attuali, che saranno modificate
		ArrayList<Double> probSelectionDistribution = frameMap.getProbSelectionDistribution();
		ArrayList<Double> probFailureDistribution = frameMap.getProbFailureDistribution();
		ArrayList<Double> probCriticalFailureDistribution = frameMap.getProbCriticalFailureDistribution();
		ArrayList<Double> trueProbSelectionDistribution = frameMap.getTrueProbSelectionDistribution();

		TestingStrategy testingStrategy = new SecondTestingStrategy(frameMap);

		int NFrames = frameMap.size();
		System.out.println("Numero di frame: " + NFrames);

		Double failProb = 0.0;
		Double reliability = 0.0;
		Double trueReliability = 0.0;

		Locale currentLocale = Locale.ITALY;
		NumberFormat numberFormatter = NumberFormat.getNumberInstance(currentLocale);
		numberFormatter.setMinimumFractionDigits(16);

		//Generazione probabilità di selezione iniziale stimata (casuale)
		for(int i = 0; i<probSelectionDistribution.size(); i++){
			probSelectionDistribution.set(i, RandomUtils.nextDouble(0,1.0));
		}
		DoubleUtils.normalize(probSelectionDistribution);
		frameMap.setProbSelectionDistribution(probSelectionDistribution);

		//Generazione probabilità di fallimento iniziale stimata (pari a |classi invalide|/|classi|)
		for(int i = 0; i<NFrames; i++){
			Frame frame = frameMap.readByKey(i);
			Double nClasses = new Double(frame.getParamList().size());
			
			Double nInvalidClasses = 0.0;
			for(Param p : frame.getParamList()){
				if(!frameMap.getApplicationSpecifics().getOracle().isParamValid(p)){
					nInvalidClasses += 1.0;
				}
			}
			
			Double invalidClassesProportion = 0.0;
			if(nClasses != 0.0){
				invalidClassesProportion = nInvalidClasses/nClasses;
			}
			
			probFailureDistribution.set(i, invalidClassesProportion);
		}
		frameMap.setProbFailureDistribution(probFailureDistribution);

		//Generazione probabilità di fallimento critica iniziale stimata (tutte pari a 0)
		for(int i = 0; i<probCriticalFailureDistribution.size(); i++){
			probCriticalFailureDistribution.set(i, 0.0);
		}
		frameMap.setProbCriticalFailureDistribution(probCriticalFailureDistribution);

		//Generazione probabilità di selezione vera (variazione della probabilità di selezione stimata)
		trueProbSelectionDistribution = TrueProbSelectionGenerator.generateNewProbDistribution(probSelectionDistribution, 0.7, 0.99);
		frameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);

		//Calcolo reliability stimata (p(i)*f(i))
		failProb = 0.0;
		for(int i = 0; i<NFrames; i++){
			failProb += probSelectionDistribution.get(i)*probFailureDistribution.get(i);
		}	
		reliability = 1d - failProb;

		//Calcolo reliability vera
		trueReliability = testingStrategy.getTrueReliability();

		System.out.println("TRUE RELIABILITY: " + numberFormatter.format(trueReliability));
		System.out.println("ESTIMATED RELIABILITY: " + numberFormatter.format(reliability));
		System.out.println("RELIABILITY OFFSET: " + numberFormatter.format(Math.abs(reliability-trueReliability)));

		return frameMap;
	}

	public static void main(String[] args) {

		//Carico le variabili d'ambiente
		loadEnvironment();

		ApplicationFactory appFactory = new DiscourseFactory();
		
		FrameMap frameMap = appFactory.makeFrameMap(frameMapFilePath);
		
		TestingStrategy testingStrategy = new SecondTestingStrategy(frameMap);
		
		testingStrategy.computeNewProbSelectionDistribution(true);
		
		ResponseLogList testResponseLogList = appFactory.makeResponseLogList("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/esperimento_23022019_165814/test_responses/test_response_log_list_cycle2.csv");
		
		ReliabilityEstimator reliabilityEstimator = new ReliabilityEstimator(testingStrategy);
		
		System.out.println(reliabilityEstimator.computeReliability(testResponseLogList));
		
	}

}
