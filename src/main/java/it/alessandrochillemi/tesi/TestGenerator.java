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

    		//Salvo i risultati e chiudo la risposta
    		int responseCode = response.code();
    		String responseMessage = response.message();
    		response.close();  	

    		//Salvo la risposta nella ResponseLogList
    		ResponseLog responseLog = applicationFactory.makeResponseLog(Integer.toString(selectedFrame, 10), responseCode, responseMessage, apiRequest.getParamList());

//    		System.out.println("");
//    		responseLog.print();

    		responseLogList.add(responseLog);
    	}
    	
    	return responseLogList;
    }
}
