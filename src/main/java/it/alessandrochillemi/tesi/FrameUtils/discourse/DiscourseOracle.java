package it.alessandrochillemi.tesi.FrameUtils.discourse;

import it.alessandrochillemi.tesi.FrameUtils.Oracle;

public class DiscourseOracle implements Oracle {

	@Override
	public boolean isFailure(boolean valid, int responseCode){
		boolean ret = false;
		//Se il codice di risposta è maggiore o uguale di 500, si tratta sicuramente di un errore
		if(responseCode >= 500){
			ret = true;
		}
		//Se il codice di risposta è compreso tra 400 e 500, si tratta di un fallimento solo se tutti gli input sono validi
		else if(responseCode>= 400){
			//Se tutti i valori sono validi, si tratta di un fallimento
			if(valid){
				ret = true;
			}
		}
		//Se il codice di risposta è compreso tra 200 e 300, si tratta di un fallimento solo se almeno un input è invalido
		else if((responseCode >= 200) && (responseCode < 300)){
			//Se almeno un valore è invalido, si tratta di un fallimento
			if(!valid){
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public boolean isCriticalFailure(boolean valid, int responseCode) {
		boolean ret = false;
		
		//In questo caso conta il fallimento è critico se il response code è maggiore o uguale di 500, a prescindere dalla validità della richiesta
		if(responseCode >= 500){
			ret = true;
		}
		
		return ret;
	}

}
