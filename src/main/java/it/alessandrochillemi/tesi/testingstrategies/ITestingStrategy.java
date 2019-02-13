package it.alessandrochillemi.tesi.testingstrategies;

import it.alessandrochillemi.tesi.frameutils.FrameMap;
import it.alessandrochillemi.tesi.frameutils.ResponseLogList;

//Strategia di test (da usare nel design pattern Strategy) che definisce il criterio di selezione dei test e l'algoritmo per il calcolo della reliability
public interface ITestingStrategy {
	//Algoritmo per la selezione di un frame secondo la distribuzione di probabilità di testing o vera
	public int selectFrame(FrameMap frameMap, boolean testingProfile);
	
	//Calcolo della reliability
	public Double getReliability(ResponseLogList responseLogList);
	
	//Calcolo della reliability per i fallimenti critici
	public Double getReliabilityForCriticalFailures(ResponseLogList responseLogList);
}
