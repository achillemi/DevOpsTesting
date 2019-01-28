package it.alessandrochillemi.tesi;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.FrameUtils.ResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLogList;

//Strategia di test (da usare nel design pattern Strategy) che definisce il criterio di selezione dei test e l'algoritmo per il calcolo della reliability
public interface ITestingStrategy {
	//Algoritmo per la selezione di un frame secondo la distribuzione di probabilità specificata
	public int selectFrame(ArrayList<Double> probSelectionDistribution);
	
	//Calcolo della reliability
	public Double getReliability(ResponseLogList<? extends ResponseLog> responseLogList);
	
	//Calcolo della reliability per i fallimenti critici
	public Double getReliabilityForCriticalFailures(ResponseLogList<? extends ResponseLog> responseLogList);
}
