package it.alessandrochillemi.tesi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrameMap;

public class TrueProbSelectionDistributionGenerator {
	
	//Percentuale dell'insieme dei frame che avranno una variazione proporzionale a +VARIATION; la restante parte di frame avrà una variazione proporzionale a -VARIATION
	private static final Double SET1_SIZE_PROPORTION = 0.6;
	
	private static Random random = new Random();

	public static ArrayList<Double> generateTrueProbSelectionDistribution(DiscourseFrameMap frameMap, Double variation) {
		
    	//Ottengo la distribuzione di probabilità di selezione a partire dalla quale calcolo la distribuzione di probabilità di selezione vera
		ArrayList<Double> estimatedProbSelectionDistribution = frameMap.getProbSelectionDistribution();
		
		//Calcolo la dimensione dei due set (al netto di errori di round off) che avranno una variazione proporzionale rispettivamente a +VARIATION e -VARIATION
		int set1Size = (int) (SET1_SIZE_PROPORTION*frameMap.size());
		int set2Size = frameMap.size() - set1Size;
		
		//Ottengo i due set di numeri casuali
		List<Double> set1 = randomsToTargetSum(set1Size,variation);	
		List<Double> set2 = randomsToTargetSum(set2Size,-variation);
		
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
		
		//Ritorno la distribuzione di probabilità di selezione calcolata
		return trueProbSelectionDistribution;

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
