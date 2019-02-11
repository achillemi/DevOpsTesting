package it.alessandrochillemi.tesi;

import java.nio.file.Files;
import java.nio.file.Paths;

import it.alessandrochillemi.tesi.FrameUtils.APIRequest;
import it.alessandrochillemi.tesi.FrameUtils.ApplicationFactory;
import it.alessandrochillemi.tesi.FrameUtils.Frame;
import it.alessandrochillemi.tesi.FrameUtils.FrameMap;
import it.alessandrochillemi.tesi.FrameUtils.Param;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFactory;
import okhttp3.Response;

public class Test {

	public static void main(String[] args) {

		String baseURL = "http://localhost:3000";
		String apiKey = "375cdce6b072d4c4e4d77dff0acec2651a071dd5c16e0016f2017d150820d4bf";
		String apiUsername = "a.chillemi";
		
		//Creo una ApplicationFactory per l'applicazione desiderata
		ApplicationFactory applicationFactory = new DiscourseFactory();

		FrameMap frameMap = null;
		//Carico la frame map; se il file non esiste, lo genero a partire dalla descrizione delle API
		if(Files.exists(Paths.get("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_frames.csv"))){
			frameMap = applicationFactory.makeFrameMap("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_frames.csv");
		}
		else{
			System.out.println("\nFrame Map non trovata!");
			return;
		}
		
		int selectedFrame = 4094;

		//Leggo il frame con l'indice scelto
		System.out.println("Selected frame: " + selectedFrame);
		Frame frame = frameMap.readByKey(selectedFrame);

		//Stampo il frame scelto
//		frame.print();

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
//		String responseMessage = response.message();
		response.close();
		
		System.out.println("ok: " + responseCode);
		
	}

}
