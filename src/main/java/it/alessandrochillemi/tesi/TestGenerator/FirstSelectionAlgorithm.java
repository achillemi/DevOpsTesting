package it.alessandrochillemi.tesi.TestGenerator;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;

public class FirstSelectionAlgorithm implements ITestSelectionStrategy {

	//Scelgo il frame secondo la probabilità di selezione impostata con l'ausilio di un vettore che memorizza la probabilità cumulata
	public int selectFrame(ArrayList<Double> probSelectionDistribution) {
    	ArrayList<Double> cumulativePVector = new ArrayList<Double>();
    	
    	cumulativePVector.add(probSelectionDistribution.get(0));
    	
    	for(int i = 1; i<probSelectionDistribution.size(); i++){
    		Double d = cumulativePVector.get(i-1)+probSelectionDistribution.get(i);
    		cumulativePVector.add(d);
    	}
    	
    	double rand = RandomUtils.nextDouble(0, 1);
    	
    	int selectedFrame=-1;		
		for(int index =0; index<probSelectionDistribution.size(); index++){
			if (rand <= cumulativePVector.get(index)) {
				selectedFrame = index;
				break;
			}
		}
		return selectedFrame;
	}

}
