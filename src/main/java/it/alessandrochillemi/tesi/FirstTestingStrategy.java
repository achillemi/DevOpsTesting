package it.alessandrochillemi.tesi;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;

import it.alessandrochillemi.tesi.FrameUtils.ResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLogList;

public class FirstTestingStrategy implements ITestingStrategy {

	//Algoritmo per la selezione di un frame secondo la distribuzione di probabilità specificata
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

	//Calcolo della reliability
	public Double getReliability(ResponseLogList<? extends ResponseLog> responseLogList) {
		Double failProb = (new Double(responseLogList.getTotalNumberOfFailures()))/(new Double(responseLogList.size()));
		Double reliability = 1 - failProb;
		return reliability;
	}

	//Calcolo della reliability per i fallimenti critici
	public Double getReliabilityForCriticalFailures(ResponseLogList<? extends ResponseLog> responseLogList) {
		Double criticalFailProb = (new Double(responseLogList.getTotalNumberOfCriticalFailures()))/(new Double(responseLogList.size()));
		Double reliabilityForCriticalFailures = 1 - criticalFailProb;
		return reliabilityForCriticalFailures;
	}

}
