package it.alessandrochillemi.tesi;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.FrameUtils.APIRequest;
import it.alessandrochillemi.tesi.FrameUtils.Param;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrame;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrameMap;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLog;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseResponseLogList;
import okhttp3.Response;

//Esegue NREQUESTS scegliendo i frame secondo la distribuzione di probabilità di selezione vera
public class WorkloadGenerator {
	
	//Strategy design pattern
	private ITestingStrategy testingStrategy;

	public WorkloadGenerator(ITestingStrategy testingStrategy){
		this.testingStrategy = testingStrategy;
	}

	public void setTestSelectionStrategy(ITestingStrategy testingStrategy) {
		this.testingStrategy = testingStrategy;
	}

	public DiscourseResponseLogList generateRequests(String baseURL, String apiUsername, String apiKey, DiscourseFrameMap frameMap, int NRequests){
		
		//Ottengo la distribuzione di probabilità di selezione vera dei frame
    	ArrayList<Double> trueProbSelectionDistribution = frameMap.getTrueProbSelectionDistribution();
    	
    	DiscourseResponseLogList responseLogList = new DiscourseResponseLogList();
    	
    	for(int i = 0; i<NRequests; i++){
    		System.out.println("\nRichiesta " + (i+1) + "...");

    		//Scelgo un frame secondo l'algoritmo selezionato (pattern Strategy)
    		int selectedFrame = testingStrategy.selectFrame(trueProbSelectionDistribution);	
    		System.out.println("Selected frame: " + selectedFrame);
        	DiscourseFrame frame = frameMap.readByKey(selectedFrame);
        	
        	//Stampo il frame scelto
//        	frame.print();

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
