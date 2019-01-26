package it.alessandrochillemi.tesi.FrameUtils.discourse;

import it.alessandrochillemi.tesi.FrameUtils.Oracle;

public class DiscourseOracle extends Oracle<DiscourseResponseLogList> {

	@Override
	public int getTotalNumberOfFailures(DiscourseResponseLogList responseLogList) {
		int failuresCount = 0;
		//Analizzo ogni responseLog sulla base delle classi di equivalenza dei parametri utilizzati per capire se si tratta di un fallimento o meno
		for(int i = 0; i<responseLogList.size(); i++){
			DiscourseResponseLog responseLog = responseLogList.get(i);
			
			//Determino la "validità" della richiesta sulla base dei parametri utilizzati: se ne è stato utilizzato almeno un non valido, considero la richiesta non valida
			boolean valid = true;
			int j = 0;
			//Scorro i parametri finché non ne trovo almeno uno non valido o termina la lista
			while(valid && j<responseLog.getParamList().size()){
				valid = responseLog.getParamList().get(j).isValid();
				j++;
			}
			
			//Se il codice di risposta è maggiore o uguale di 500, si tratta sicuramente di un errore
			if(responseLog.getResponseCode() >= 500){
				failuresCount++;
			}
			//Se il codice di risposta è compreso tra 400 e 500, si tratta di un fallimento solo se tutti gli input sono validi
			else if(responseLog.getResponseCode()>= 400){
				//Se tutti i valori sono validi, si tratta di un fallimento
				if(valid){
					failuresCount++;
				}
			}
			//Se il codice di risposta è compreso tra 200 e 300, si tratta di un fallimento solo se almeno un input è invalido
			else if((responseLog.getResponseCode() >= 200) && (responseLog.getResponseCode() < 300)){
				//Se almeno un valore è invalido, si tratta di un fallimento
				if(!valid){
					failuresCount++;
				}
			}
		}
		return failuresCount;
	}

	@Override
	public int getFrameFailures(DiscourseResponseLogList responseLogList, String frameID) {
		//Creo una response log list nella quale aggiungo solo le risposte relative al frame specificato
		DiscourseResponseLogList frameResponseLogList = new DiscourseResponseLogList();
		
		for(int i = 0; i<responseLogList.size(); i++){
			DiscourseResponseLog d = responseLogList.get(i);
			if(d.getFrameID().equals(frameID)){
				frameResponseLogList.add(d);
			}
		}
		
		//Ritorno il numero di fallimenti presenti nella nuova response log list, che sono relativi solo al frame specificato
		return this.getTotalNumberOfFailures(frameResponseLogList);
	}

}
