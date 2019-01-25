package it.alessandrochillemi.tesi.FrameUtils.discourse;

import it.alessandrochillemi.tesi.FrameUtils.Oracle;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLogList;

public class DiscourseOracle extends Oracle<ResponseLogList<ResponseLog<DiscourseParam>>> {

	@Override
	public int getTotalNumberOfFailures(ResponseLogList<ResponseLog<DiscourseParam>> responseLogList) {
		int failuresCount = 0;
		//Analizzo ogni responseLog sulla base delle classi di equivalenza dei parametri utilizzati per capire se si tratta di un fallimento o meno
		for(int i = 0; i<responseLogList.size(); i++){
			ResponseLog<DiscourseParam> responseLog = responseLogList.get(i);
			
			//Determino la "validità" della richiesta sulla base dei parametri utilizzati: se ne è stato utilizzato almeno un non valido, considero la richiesta non valida
			boolean valid = true;
			int j = 0;
			//Scorro i parametri finché non ne trovo almeno uno non valido o termina la lista
			while(valid && j<responseLog.getParamList().size()){
				//Se la classe di equivalenza è "invalid", il valore è invalido
				if(responseLog.getParamList().get(j).getClassParam().isInvalid()){
					valid = false;
				}
				//Se la classe di equivalenza è "empty" e il parametro è obbligatorio, il valore è invalido
				else if(responseLog.getParamList().get(j).getClassParam().isEmpty() && responseLog.getParamList().get(j).isRequired()){
					valid = false;
				}
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

}
