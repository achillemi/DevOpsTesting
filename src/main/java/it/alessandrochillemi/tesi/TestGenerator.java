package it.alessandrochillemi.tesi;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.FrameUtils.APIRequest;
import it.alessandrochillemi.tesi.FrameUtils.Param;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrame;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrameMap;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLogList;
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
	
    public DiscourseResponseLogList generateTests(String baseURL, String apiUsername, String apiKey, DiscourseFrameMap frameMap, int NTests){

    	ArrayList<Double> probSelectionDistribution = frameMap.getProbSelectionDistribution();
    	
    	DiscourseResponseLogList responseLogList = new DiscourseResponseLogList();
    	
    	for(int i = 0; i<NTests; i++){
    		System.out.println("\nTest " + (i+1) + "...");
    		
    		//Scelgo un frame secondo l'algoritmo selezionato (pattern Strategy)
    		int selectedFrame = testingStrategy.selectFrame(probSelectionDistribution);

    		//Leggo il frame con l'indice scelto
    		System.out.println("Selected frame: " + selectedFrame);
    		DiscourseFrame frame = frameMap.readByKey(selectedFrame);

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
    		DiscourseResponseLog responseLog = new DiscourseResponseLog(Integer.toString(selectedFrame, 10), responseCode, responseMessage, apiRequest.getParamList());

//    		System.out.println("");
//    		responseLog.print();

    		responseLogList.add(responseLog);
    	}
    	
    	return responseLogList;
    }
}
