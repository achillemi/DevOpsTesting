package it.alessandrochillemi.tesi.testingstrategies;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;

import it.alessandrochillemi.tesi.frameutils.FrameMap;
import it.alessandrochillemi.tesi.frameutils.ResponseLogList;
import it.alessandrochillemi.tesi.utils.DoubleUtils;

public class SecondTestingStrategy extends TestingStrategy {

	@Override
	public int selectFrame(FrameMap frameMap, boolean testingProfile) {

		ArrayList<Double> probFailureDistribution = frameMap.getProbFailureDistribution();
		ArrayList<Double> probSelectionDistribution = null;

		//Se testingProfile == true, uso la distribuzione di probabilità stimata (ovvero il profilo di testing)
		if(testingProfile){
			probSelectionDistribution = frameMap.getProbSelectionDistribution();
		}
		//Se testingProfile == false, uso la distribuzione di probabilità vera (ovvero il profilo utente)
		else{
			probSelectionDistribution = frameMap.getTrueProbSelectionDistribution();
		}

		//Ottengo una nuova distribuzione di probabilità in cui l'elemento i-esimo è pari a p(i)*f(i)
		ArrayList<Double> newProbSelectionDistribution = new ArrayList<Double>();

		for(int i = 0; i<probFailureDistribution.size(); i++){
			Double d = probSelectionDistribution.get(i)*probFailureDistribution.get(i);
			newProbSelectionDistribution.add(d);
		}

		//Normalizzo la nuova distribuzione di probabilità appena calcolata
		DoubleUtils.normalize(newProbSelectionDistribution);

		//Seleziono il frame secondo la nuova distribuzione di probabilità
		ArrayList<Double> cumulativePVector = new ArrayList<Double>();

		cumulativePVector.add(newProbSelectionDistribution.get(0));

		for(int i = 1; i<newProbSelectionDistribution.size(); i++){
			Double d = cumulativePVector.get(i-1)+newProbSelectionDistribution.get(i);
			cumulativePVector.add(d);
		}

		double rand = RandomUtils.nextDouble(0, 1);

		int selectedFrame=-1;		
		for(int index =0; index<newProbSelectionDistribution.size(); index++){
			if (rand <= cumulativePVector.get(index)) {
				selectedFrame = index;
				break;
			}
		}
		return selectedFrame;
	}

	@Override
	public Double getReliability(ResponseLogList responseLogList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getReliabilityForCriticalFailures(ResponseLogList responseLogList) {
		// TODO Auto-generated method stub
		return null;
	}

}
