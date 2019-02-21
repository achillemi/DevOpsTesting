package it.alessandrochillemi.tesi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import it.alessandrochillemi.tesi.frameutils.ApplicationFactory;
import it.alessandrochillemi.tesi.frameutils.FrameMap;
import it.alessandrochillemi.tesi.frameutils.discourse.DiscourseFactory;
import it.alessandrochillemi.tesi.testingstrategies.SecondTestingStrategy;
import it.alessandrochillemi.tesi.testingstrategies.TestingStrategy;
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

	public static void main(String[] args) {

		//Carico le variabili d'ambiente
		loadEnvironment();

		ApplicationFactory appFactory = new DiscourseFactory();

		ArrayList<Double> probSelectionDistribution = null;
		ArrayList<Double> trueProbSelectionDistribution = null;
		ArrayList<Double> trueProbFailureDistribution = null;

		TestingStrategy testingStrategy = null;

		int NFrames = 0;

		Double failProb = 0.0;
		Double reliability = 0.0;
		Double trueReliability = 0.0;

		Locale currentLocale = Locale.ITALY;
		NumberFormat numberFormatter = NumberFormat.getNumberInstance(currentLocale);
		numberFormatter.setMinimumFractionDigits(16);

		//Metodo DEL user
		FrameMap deleteUserFrameMap = appFactory.makeFrameMap("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_delete_user_frames.csv");
		System.out.println("\n\n*** METODO DELETE USER ***");
		NFrames = deleteUserFrameMap.size();
		System.out.println("Numero di frame: " + NFrames);
		testingStrategy = new SecondTestingStrategy(deleteUserFrameMap);

		probSelectionDistribution = deleteUserFrameMap.getProbSelectionDistribution();
		trueProbFailureDistribution = deleteUserFrameMap.getTrueProbFailureDistribution();
		failProb = 0.0;
		for(int i = 0; i<NFrames; i++){
			probSelectionDistribution.set(i, (1.0/(new Double(NFrames))));
			failProb += probSelectionDistribution.get(i)*trueProbFailureDistribution.get(i);
		}
		deleteUserFrameMap.setProbSelectionDistribution(probSelectionDistribution);

		trueProbSelectionDistribution = TrueProbSelectionGenerator.generateNewProbDistribution(probSelectionDistribution, 0.7, 0.99);	
		deleteUserFrameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);

		reliability = 1d - failProb;
		trueReliability = testingStrategy.getTrueReliability();

		System.out.println("TRUE RELIABILITY: " + numberFormatter.format(trueReliability));
		System.out.println("ESTIMATED RELIABILITY: " + numberFormatter.format(reliability));
		System.out.println("RELIABILITY OFFSET: " + numberFormatter.format(Math.abs(reliability-trueReliability)));

		deleteUserFrameMap.writeToCSVFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_delete_user_frames.csv");

		//Metodo POST categories
		FrameMap postCategoriesFrameMap = appFactory.makeFrameMap("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_post_categories_frames.csv");
		System.out.println("\n\n*** METODO POST CATEGORIES ***");
		NFrames = postCategoriesFrameMap.size();
		System.out.println("Numero di frame: " + NFrames);
		testingStrategy = new SecondTestingStrategy(postCategoriesFrameMap);

		probSelectionDistribution = postCategoriesFrameMap.getProbSelectionDistribution();
		trueProbFailureDistribution = postCategoriesFrameMap.getTrueProbFailureDistribution();
		failProb = 0.0;
		for(int i = 0; i<NFrames; i++){
			probSelectionDistribution.set(i, (1.0/(new Double(NFrames))));
			failProb += probSelectionDistribution.get(i)*trueProbFailureDistribution.get(i);
		}
		postCategoriesFrameMap.setProbSelectionDistribution(probSelectionDistribution);

		trueProbSelectionDistribution = TrueProbSelectionGenerator.generateNewProbDistribution(probSelectionDistribution, 0.7, 0.99);	
		postCategoriesFrameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);

		reliability = 1d - failProb;
		trueReliability = testingStrategy.getTrueReliability();

		System.out.println("TRUE RELIABILITY: " + numberFormatter.format(trueReliability));
		System.out.println("ESTIMATED RELIABILITY: " + numberFormatter.format(reliability));
		System.out.println("RELIABILITY OFFSET: " + numberFormatter.format(Math.abs(reliability-trueReliability)));

		postCategoriesFrameMap.writeToCSVFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_post_categories_frames.csv");

		//METODO POST users
		FrameMap postUsersFrameMap = appFactory.makeFrameMap("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_post_users_frames.csv");
		System.out.println("\n\n*** METODO POST USERS ***");
		NFrames = postUsersFrameMap.size();
		System.out.println("Numero di frame: " + NFrames);
		testingStrategy = new SecondTestingStrategy(postUsersFrameMap);

		probSelectionDistribution = postUsersFrameMap.getProbSelectionDistribution();
		trueProbFailureDistribution = postUsersFrameMap.getTrueProbFailureDistribution();
		failProb = 0.0;
		for(int i = 0; i<NFrames; i++){
			probSelectionDistribution.set(i, (1.0/(new Double(NFrames))));
			failProb += probSelectionDistribution.get(i)*trueProbFailureDistribution.get(i);
		}
		postUsersFrameMap.setProbSelectionDistribution(probSelectionDistribution);

		trueProbSelectionDistribution = TrueProbSelectionGenerator.generateNewProbDistribution(probSelectionDistribution, 0.7, 0.99);	
		postUsersFrameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);

		reliability = 1d - failProb;
		trueReliability = testingStrategy.getTrueReliability();

		System.out.println("TRUE RELIABILITY: " + numberFormatter.format(trueReliability));
		System.out.println("ESTIMATED RELIABILITY: " + numberFormatter.format(reliability));
		System.out.println("RELIABILITY OFFSET: " + numberFormatter.format(Math.abs(reliability-trueReliability)));

		postUsersFrameMap.writeToCSVFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_post_users_frames.csv");

		//Metodo PUT categories
		FrameMap putCategoriesFrameMap = appFactory.makeFrameMap("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_put_categories_frames.csv");
		System.out.println("\n\n*** METODO PUT CATEGORIES ***");
		NFrames = putCategoriesFrameMap.size();
		System.out.println("Numero di frame: " + NFrames);
		testingStrategy = new SecondTestingStrategy(putCategoriesFrameMap);

		probSelectionDistribution = putCategoriesFrameMap.getProbSelectionDistribution();
		trueProbFailureDistribution = putCategoriesFrameMap.getTrueProbFailureDistribution();
		failProb = 0.0;
		for(int i = 0; i<NFrames; i++){
			probSelectionDistribution.set(i, (1.0/(new Double(NFrames))));
			failProb += probSelectionDistribution.get(i)*trueProbFailureDistribution.get(i);
		}
		putCategoriesFrameMap.setProbSelectionDistribution(probSelectionDistribution);

		trueProbSelectionDistribution = TrueProbSelectionGenerator.generateNewProbDistribution(probSelectionDistribution, 0.7, 0.99);	
		putCategoriesFrameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);

		reliability = 1d - failProb;
		trueReliability = testingStrategy.getTrueReliability();

		System.out.println("TRUE RELIABILITY: " + numberFormatter.format(trueReliability));
		System.out.println("ESTIMATED RELIABILITY: " + numberFormatter.format(reliability));
		System.out.println("RELIABILITY OFFSET: " + numberFormatter.format(Math.abs(reliability-trueReliability)));

		putCategoriesFrameMap.writeToCSVFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_put_categories_frames.csv");

		//Metodo PUT tag groups
		FrameMap putTagGroupsFrameMap = appFactory.makeFrameMap("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_put_tag_groups_frames.csv");
		System.out.println("\n\n*** METODO PUT TAG GROUPS ***");
		NFrames = putTagGroupsFrameMap.size();
		System.out.println("Numero di frame: " + NFrames);
		testingStrategy = new SecondTestingStrategy(putTagGroupsFrameMap);

		probSelectionDistribution = putTagGroupsFrameMap.getProbSelectionDistribution();
		trueProbFailureDistribution = putTagGroupsFrameMap.getTrueProbFailureDistribution();
		failProb = 0.0;
		for(int i = 0; i<NFrames; i++){
			probSelectionDistribution.set(i, (1.0/(new Double(NFrames))));
			failProb += probSelectionDistribution.get(i)*trueProbFailureDistribution.get(i);
		}
		putTagGroupsFrameMap.setProbSelectionDistribution(probSelectionDistribution);

		trueProbSelectionDistribution = TrueProbSelectionGenerator.generateNewProbDistribution(probSelectionDistribution, 0.7, 0.99);	
		putTagGroupsFrameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);

		reliability = 1d - failProb;
		trueReliability = testingStrategy.getTrueReliability();

		System.out.println("TRUE RELIABILITY: " + numberFormatter.format(trueReliability));
		System.out.println("ESTIMATED RELIABILITY: " + numberFormatter.format(reliability));
		System.out.println("RELIABILITY OFFSET: " + numberFormatter.format(Math.abs(reliability-trueReliability)));

		putTagGroupsFrameMap.writeToCSVFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_single_methods_experiments/discourse_put_tag_groups_frames.csv");

		//FrameMap completa
		FrameMap frameMap = appFactory.makeFrameMap(frameMapFilePath);
		System.out.println("\n\n*** FRAME MAP COMPLETA ***");
		NFrames = frameMap.size();
		System.out.println("Numero di frame: " + NFrames);
		testingStrategy = new SecondTestingStrategy(frameMap);

		probSelectionDistribution = frameMap.getProbSelectionDistribution();
		trueProbFailureDistribution = frameMap.getTrueProbFailureDistribution();
		failProb = 0.0;
		for(int i = 0; i<NFrames; i++){
			probSelectionDistribution.set(i, (1.0/(new Double(NFrames))));
			failProb += probSelectionDistribution.get(i)*trueProbFailureDistribution.get(i);
		}

		trueProbSelectionDistribution = TrueProbSelectionGenerator.generateNewProbDistribution(probSelectionDistribution, 0.7, 0.99);
		frameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);

		reliability = 1d - failProb;
		trueReliability = testingStrategy.getTrueReliability();

		System.out.println("TRUE RELIABILITY: " + numberFormatter.format(trueReliability));
		System.out.println("ESTIMATED RELIABILITY: " + numberFormatter.format(reliability));
		System.out.println("RELIABILITY OFFSET: " + numberFormatter.format(Math.abs(reliability-trueReliability)));

	}

}
