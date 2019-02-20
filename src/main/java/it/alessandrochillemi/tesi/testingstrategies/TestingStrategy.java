package it.alessandrochillemi.tesi.testingstrategies;

import it.alessandrochillemi.tesi.frameutils.FrameMap;
import it.alessandrochillemi.tesi.frameutils.ResponseLogList;

//Strategia di test (da usare nel design pattern Strategy) che definisce il criterio di selezione dei test e l'algoritmo per il calcolo della reliability
public abstract class TestingStrategy {
	//Algoritmo per la selezione di un frame secondo la distribuzione di probabilità di testing o vera
	public abstract int selectFrame(FrameMap frameMap, boolean testingProfile);

	//Calcolo della reliability
	public abstract Double getReliability(ResponseLogList responseLogList);

	//Calcolo della reliability per i fallimenti critici
	public abstract Double getReliabilityForCriticalFailures(ResponseLogList responseLogList);

	//Calcolo della reliability vera
	public Double getTrueReliability(ResponseLogList responseLogList){
		Double totalFailures = new Double(responseLogList.getTotalNumberOfFailures());

		Double totalRequests = new Double(responseLogList.size());

		Double trueReliability = 1d - totalFailures/totalRequests;

		return trueReliability;
	}

	//Calcolo della reliability vera per i fallimenti critici
	public Double getTrueReliabilityForCriticalFailures(ResponseLogList responseLogList){
		Double totalCriticalFailures = new Double(responseLogList.getTotalNumberOfCriticalFailures());

		Double totalRequests = new Double(responseLogList.size());

		Double trueReliabilityForCriticalFailures = 1d - totalCriticalFailures/totalRequests;

		return trueReliabilityForCriticalFailures;
	}
}
