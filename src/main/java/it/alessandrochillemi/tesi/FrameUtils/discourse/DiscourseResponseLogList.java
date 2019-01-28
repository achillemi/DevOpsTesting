package it.alessandrochillemi.tesi.FrameUtils.discourse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import it.alessandrochillemi.tesi.FrameUtils.ResponseLogList;

public class DiscourseResponseLogList extends ResponseLogList<DiscourseResponseLog> {
	
	public DiscourseResponseLogList(){
		super();
	}
	
	public DiscourseResponseLogList(String path){
		super(path);
	}

	@Override
	public void readFromCSVFile(String path) {
		if(Files.exists(Paths.get(path))){
			Reader in;
			try {
				//Read the CSV file
				in = new FileReader(path);
				Iterable<CSVRecord> records = CSVFormat.RFC4180.withDelimiter(';').withFirstRecordAsHeader().parse(in);
				for (CSVRecord record : records) {
					//Create a new response log from the record
					DiscourseResponseLog discourseResponseLog = new DiscourseResponseLog(record);
					
					//Add the frame to the map
					this.responseLogList.add(discourseResponseLog);

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			System.out.println("File does not exist!");
		}
		
	}

	@Override
	public int getTotalNumberOfFailures() {
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
	public int getTotalNumberOfCriticalFailures() {
		//Conto i fallimenti critici (fallimenti con codice di errore>= 500)
		int countCriticalFailures = 0;

		for(int i = 0; i<responseLogList.size(); i++){
			DiscourseResponseLog d = responseLogList.get(i);
			if(d.getResponseCode()>=500){
				countCriticalFailures++;
			}
		}

		//Ritorno il numero di fallimenti critici
		return countCriticalFailures;
	}

	@Override
	public int getFrameFailures(String frameID) {
		//Creo una response log list nella quale aggiungo solo le risposte relative al frame specificato
		DiscourseResponseLogList frameResponseLogList = new DiscourseResponseLogList();

		for(int i = 0; i<responseLogList.size(); i++){
			DiscourseResponseLog d = responseLogList.get(i);
			if(d.getFrameID().equals(frameID)){
				frameResponseLogList.add(d);
			}
		}

		//Ritorno il numero di fallimenti presenti nella nuova response log list, che sono relativi solo al frame specificato
		return frameResponseLogList.getTotalNumberOfFailures();
	}
	

}
