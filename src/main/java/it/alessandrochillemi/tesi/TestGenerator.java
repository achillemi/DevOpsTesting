package it.alessandrochillemi.tesi;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.FrameUtils.APIRequest;
import it.alessandrochillemi.tesi.FrameUtils.ApplicationFactory;
import it.alessandrochillemi.tesi.FrameUtils.Frame;
import it.alessandrochillemi.tesi.FrameUtils.FrameMap;
import it.alessandrochillemi.tesi.FrameUtils.Param;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLogList;
import okhttp3.Response;

//Esegue NTESTS scegliendo i frame secondo la distribuzione di probabilità di selezione
public class TestGenerator{
	
	//Strategy design pattern
	private ITestingStrategy testingStrategy;
	
	public TestGenerator(ITestingStrategy testingStrategy){
		this.testingStrategy = testingStrategy;
	}
	
	public void setTestSelectionStrategy(ITestingStrategy testingStrategy) {
		this.testingStrategy = testingStrategy;
	}
	
    public ResponseLogList generateTests(String baseURL, String apiUsername, String apiKey, FrameMap frameMap, int NTests, ApplicationFactory applicationFactory){

    	ArrayList<Double> probSelectionDistribution = frameMap.getProbSelectionDistribution();
    	
    	ResponseLogList responseLogList = applicationFactory.makeResponseLogList();
    	
    	for(int i = 0; i<NTests; i++){
    		System.out.println("\nTest " + (i+1) + "...");
    		
    		//Scelgo un frame secondo l'algoritmo selezionato (pattern Strategy)
    		int selectedFrame = testingStrategy.selectFrame(probSelectionDistribution);

    		//Leggo il frame con l'indice scelto
    		System.out.println("Frame selezionato: " + selectedFrame);
    		Frame frame = frameMap.readByKey(selectedFrame);

    		//Stampo il frame scelto
//    		frame.print();

    		//Genero i valori dei parametri applicando le precondizioni
    		for(Param p : frame.getParamList()){
    			p.generateValueWithPreConditions(baseURL,apiUsername,apiKey,true);
    		}

    		//Creo una APIRequest con i campi del Frame estratto
    		APIRequest apiRequest = new APIRequest(frame);
    		apiRequest.setBaseURL(baseURL);
    		apiRequest.setApiUsername(apiUsername);
    		apiRequest.setApiKey(apiKey);

    		//Invio la richiesta
    		Response response = apiRequest.sendRequest();

    		int responseCode = 0;
    		String responseMessage = "";
    		//Se la richiesta è andata a buon fine, salvo i risultati e chiudo la risposta
    		if(response != null){
	    		responseCode = response.code();
	    		responseMessage = response.message();
	    		response.close();  
    		}
    		//Se non è andata a buon fine, la considero un fallimento dell'applicazione e lascio vuoto il messaggio di risposta
    		else{
    			responseCode = 500;
    			responseMessage = "";
    		}

    		//Salvo la risposta nella ResponseLogList
    		ResponseLog responseLog = applicationFactory.makeResponseLog(Integer.toString(selectedFrame, 10), responseCode, responseMessage, apiRequest.getParamList());

//    		System.out.println("");
//    		responseLog.print();

    		responseLogList.add(responseLog);
    	}
    	
    	return responseLogList;
    }
}
