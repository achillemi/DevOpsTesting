package it.alessandrochillemi.tesi;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLogList;

public class Monitor{
	
	public ArrayList<Double> updateProbSelectionDistribution(ArrayList<Double> oldProbSelectionDistribution, DiscourseResponseLogList userResponseLogList, Double learningRate){
		
		ArrayList<Double> newProbSelectionDistribution = new ArrayList<Double>();
		
		//Ottengo il numero di richieste totali effettuate e lo converto in Double
		Double totalNumberOfRequests = new Double(userResponseLogList.size());
		
		//Scorro tutti i frame
		for(int i = 0; i<oldProbSelectionDistribution.size(); i++){
			//Ottengo il numero di richieste effettuate per il frame i-esimo e lo converto in Double
			Double frameRequests = new Double(userResponseLogList.count(String.valueOf(i)));
			
			//Calcolo la nuova probabilità di selezione
			Double newProbSelection = learningRate*oldProbSelectionDistribution.get(i) + (1d - learningRate)*(frameRequests/totalNumberOfRequests);
			
			newProbSelectionDistribution.add(newProbSelection);
		}
		
		return newProbSelectionDistribution;
	}
	
	public ArrayList<Double> updateProbFailureDistribution(ArrayList<Double> oldProbFailureDistribution, DiscourseResponseLogList userResponseLogList, Double learningRate){
		
		ArrayList<Double> newProbFailureDistribution = new ArrayList<Double>();
		
		//Scorro tutti i frame
		for(int i = 0; i<oldProbFailureDistribution.size(); i++){
			//Ottengo il numero di fallimenti per il frame i-esimo e lo converto in Double
			Double frameFailures = new Double(userResponseLogList.getFrameFailures(String.valueOf(i)));
			
			//Ottengo il numero di richieste effettuate per il frame i-esimo e lo converto in Double
			Double frameRequests = new Double(userResponseLogList.count(String.valueOf(i)));
			
			//Calcolo la nuova probabilità di fallimento
			Double newProbFailure = learningRate*oldProbFailureDistribution.get(i) + (1d - learningRate)*(frameFailures/frameRequests);
			
			newProbFailureDistribution.add(newProbFailure);
		}
		
		return newProbFailureDistribution;
	}
	
	public ArrayList<Double> updateProbCriticalFailureDistribution(ArrayList<Double> oldProbCriticalFailureDistribution, DiscourseResponseLogList userResponseLogList, Double learningRate){

		ArrayList<Double> newProbCriticalFailureDistribution = new ArrayList<Double>();

		//Scorro tutti i frame
		for(int i = 0; i<oldProbCriticalFailureDistribution.size(); i++){
			//Ottengo il numero di fallimenti per il frame i-esimo e lo converto in Double
			Double frameCriticalFailures = new Double(userResponseLogList.getFrameCriticalFailures(String.valueOf(i)));

			//Ottengo il numero di richieste effettuate per il frame i-esimo e lo converto in Double
			Double frameRequests = new Double(userResponseLogList.count(String.valueOf(i)));

			//Calcolo la nuova probabilità di fallimento
			Double newProbFailure = learningRate*oldProbCriticalFailureDistribution.get(i) + (1d - learningRate)*(frameCriticalFailures/frameRequests);

			newProbCriticalFailureDistribution.add(newProbFailure);
		}

		return newProbCriticalFailureDistribution;
	}

}
