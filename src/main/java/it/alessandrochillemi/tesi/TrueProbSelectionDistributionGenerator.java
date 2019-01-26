package it.alessandrochillemi.tesi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrameMap;

public class TrueProbSelectionDistributionGenerator {
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	public static final String ENVIRONMENT_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";

	//Percentuale di variazione della distribuzione di probabilità di selezione vera rispetto alla distribuzione di probabilità "stimata"
	private static final Double VARIATION = 0.3;
	
	//Percentuale dell'insieme dei frame che avranno una variazione proporzionale a +VARIATION; la restante parte di frame avrà una variazione proporzionale a -VARIATION
	private static final Double SET1_SIZE_PROPORTION = 0.6;
	
	private static String frameMapPath;
	
	private static Random random = new Random();

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
	}

	public static void main(String[] args) {
		
		loadEnvironment();
		
		//Carico la FrameMap
    	DiscourseFrameMap frameMap = new DiscourseFrameMap(frameMapPath);
		
    	//Ottengo la distribuzione di probabilità di selezione a partire dalla quale calcolo la distribuzione di probabilità di selezione vera
		ArrayList<Double> estimatedProbSelectionDistribution = frameMap.getProbSelectionDistribution();
		
		//Calcolo la dimensione dei due set (al netto di errori di round off) che avranno una variazione proporzionale rispettivamente a +VARIATION e -VARIATION
		int set1Size = (int) (SET1_SIZE_PROPORTION*frameMap.size());
		int set2Size = frameMap.size() - set1Size;
		
		//Ottengo i due set di numeri casuali
		List<Double> set1 = randomsToTargetSum(set1Size,VARIATION);	
		List<Double> set2 = randomsToTargetSum(set2Size,-VARIATION);
		
		//Concateno le due liste ed eseguo uno shuffling
		List<Double> finalSet = new ArrayList<Double>(set1);
		finalSet.addAll(set2);
		
		Collections.shuffle(finalSet);
		
		//Sommo il set di numeri casuali calcolato alla distribuzione di probabilità di partenza
		ArrayList<Double> trueProbSelectionDistribution = new ArrayList<Double>();
		for(int i = 0; i<finalSet.size(); i++){
			trueProbSelectionDistribution.add(estimatedProbSelectionDistribution.get(i)+finalSet.get(i));
		}
		
		//Se nella distribuzione di probabilità di selezione vera appena calcolata sono presenti numeri negativi, sommo 1 a tutti gli elementi e normalizzo
		if(areThereNegatives(trueProbSelectionDistribution)){
			for(int i = 0; i<trueProbSelectionDistribution.size(); i++){
				trueProbSelectionDistribution.set(i, trueProbSelectionDistribution.get(i)+1.0);
			}
			normalize(trueProbSelectionDistribution);
		}
		
		//Aggiungo la distribuzione di probabilità di selezione calcolata alla frameMap
		frameMap.setTrueProbSelectionDistribution(trueProbSelectionDistribution);
		
		//Salvo la frameMap
		frameMap.writeToCSVFile(frameMapPath);
		
		System.out.println("Tutto ok!");

	}
	
	//Ottiene un array di nRandoms Double la cui somma è pari a targetSum
	private static ArrayList<Double> randomsToTargetSum(int nRandoms, Double targetSum){
		ArrayList<Double> ret = new ArrayList<Double>();
		
		//random numbers
	    Double sum = 0.0;
	    for (int i = 0; i < nRandoms; i++) {
	        Double next = randomInRange(0.0,targetSum);
	        ret.add(next);
	        sum += next;
	    }

	    //scale to the desired target sum
	    double scale = targetSum / sum;
	    for (int i = 0; i < nRandoms; i++) {
	    	ret.set(i, (ret.get(i) * scale));
	    }
	    
	    return ret;
	}
	
	//Ottiene un Double casuale tra min e max
	private static Double randomInRange(Double min, Double max) {
		Double range = max - min;
		Double scaled = random.nextDouble() * range;
		Double shifted = scaled + min;
		return shifted; // == (rand.nextDouble() * (max-min)) + min;
	}

	
	//Ritorna un valore "true" se nell'array passato come argomento è presente almeno un valore negativo
	private static boolean areThereNegatives(ArrayList<Double> doubleList){
		boolean negative = false;
		int i = 0;
		while(!negative && i<doubleList.size()){
			if(doubleList.get(i)<0){
				negative = true;
			}
			i++;
		}
		
		return negative;
	}
	
	//Normalizza un array di Double
	private static void normalize(ArrayList<Double> doubleList){
		Double sum = 0.0;
		for(Double d : doubleList){
			sum += d;
		}
		for(int i = 0; i<doubleList.size(); i++){
			doubleList.set(i, doubleList.get(i)/sum);
		}
	}

}
