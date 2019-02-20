package it.alessandrochillemi.tesi.testingstrategies;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;

import it.alessandrochillemi.tesi.frameutils.FrameMap;
import it.alessandrochillemi.tesi.frameutils.ResponseLog;
import it.alessandrochillemi.tesi.frameutils.ResponseLogList;
import it.alessandrochillemi.tesi.utils.DoubleUtils;

public class SecondTestingStrategy extends TestingStrategy {

	private ArrayList<Double> estimatedProbSelectionDistribution;
	private ArrayList<Double> newTestingProbSelectionDistribution;
	private ArrayList<Double> newUserProbSelectionDistribution;	
	
	public SecondTestingStrategy(FrameMap frameMap){
		super(frameMap);
		
		estimatedProbSelectionDistribution = new ArrayList<Double>();
		newTestingProbSelectionDistribution = new ArrayList<Double>();
		newUserProbSelectionDistribution = new ArrayList<Double>();
	}

	@Override
	public int selectFrame(boolean testingProfile) {
		ArrayList<Double> newProbSelectionDistribution = null;
		//Se testingProfile == true, uso la nuova distribuzione di probabilità per il testing
		if(testingProfile){
			newProbSelectionDistribution = this.newTestingProbSelectionDistribution;
		}
		//Altrimenti, uso la nuova distribuzione di probabilità per l'utente
		else{
			newProbSelectionDistribution = this.newUserProbSelectionDistribution;
		}

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
		int T = responseLogList.size();

		Double failProb = 0.0;

		//Scorro la lista di risposte
		for(int i = 0; i<T; i++){
			//Ottengo la risposta corrente
			ResponseLog responseLog = responseLogList.get(i);

			//Ottengo l'ID del frame relativo alla risposta corrente
			int frameID = Integer.valueOf(responseLog.getFrameID());

			//Ottengo la probabilità di selezione stimata relativa al frame considerato
			Double estimatedProbSelection = estimatedProbSelectionDistribution.get(frameID);

			//Ottengo la nuova probabilità di selezione (già calcolata al momento della selezione dei frame) relativa al frame considerato
			Double newTestingProbSelection = newTestingProbSelectionDistribution.get(frameID);

			//Creo un valore pari a 1 se la risposta indica un fallimento, pari a 0 in caso contrario
			Double isFailure = responseLog.isFailure() ? 1.0 : 0.0;

			failProb += estimatedProbSelection*isFailure/( (new Double(T)) * newTestingProbSelection);

		}

		Double reliability = 1d - failProb;

		return reliability;
	}

	@Override
	public Double getReliabilityForCriticalFailures(ResponseLogList responseLogList) {
		int T = responseLogList.size();

		Double failProb = 0.0;

		//Scorro la lista di risposte
		for(int i = 0; i<T; i++){
			//Ottengo la risposta corrente
			ResponseLog responseLog = responseLogList.get(i);

			//Ottengo l'ID del frame relativo alla risposta corrente
			int frameID = Integer.valueOf(responseLog.getFrameID());

			//Ottengo la probabilità di selezione stimata relativa al frame considerato
			Double estimatedProbSelection = estimatedProbSelectionDistribution.get(frameID);

			//Ottengo la nuova probabilità di selezione (già calcolata al momento della selezione dei frame) relativa al frame considerato
			Double newTestingProbSelection = newTestingProbSelectionDistribution.get(frameID);

			//Creo un valore pari a 1 se la risposta indica un fallimento, pari a 0 in caso contrario
			Double isCriticalFailure = responseLog.isCriticalFailure() ? 1.0 : 0.0;

			failProb += estimatedProbSelection*isCriticalFailure/( (new Double(T)) * newTestingProbSelection);

		}

		Double reliabilityForCriticalFailures = 1d - failProb;

		return reliabilityForCriticalFailures;
	}

	//Aggiorno la distribuzione della probabilità di selezione in modo che per il frame i-esimo sia pari a p(i)*f(i)
	public void computeNewProbSelectionDistribution(boolean testingProfile){
		ArrayList<Double> probFailureDistribution = frameMap.getProbFailureDistribution();
		ArrayList<Double> probSelectionDistribution = null;

		//Se testingProfile == true, uso la distribuzione di probabilità stimata (ovvero il profilo di testing)
		if(testingProfile){
			probSelectionDistribution = frameMap.getProbSelectionDistribution();

			//Memorizzo la distribuzione di probabilità di selezione stimata
			this.estimatedProbSelectionDistribution = probSelectionDistribution;
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

		//Se testingProfile == true, la nuova distribuzione di probabilità appena calcolata va usata per il testing
		if(testingProfile){
			this.newTestingProbSelectionDistribution = newProbSelectionDistribution;
		}
		//Se testingProfile == false, la nuova distribuzione di probabilità appena calcolata va usata per la generazione del workload
		else{
			this.newUserProbSelectionDistribution = newProbSelectionDistribution;
		}
	}

}
