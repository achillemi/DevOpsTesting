package it.alessandrochillemi.tesi.WLGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class WLGenerator{
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	private static final String ENVIRONMENT_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
    public static void main( String[] args ){
    	
	    	//Carico le variabili d'ambiente (path della lista di testframe, api_key, api_username, ecc.)
	    	Properties environment = new Properties();
	    	InputStream is = null;
	    	try {
	    		is = new FileInputStream(ENVIRONMENT_PATH);
	    	} catch (FileNotFoundException e1) {
	    		e1.printStackTrace();
	    	}
	    	try {
	    		environment.load(is);
	    	} catch (IOException e1) {
	    		e1.printStackTrace();
	    	}
	    	
	    	//Leggo le variabili d'ambiente
	    	String testFrameListPath = environment.getProperty("testframe_list_path");
	    	String baseURL = environment.getProperty("base_url");
	    	String apiUsername = environment.getProperty("api_username");
	    	String apiKey = environment.getProperty("api_key");
	    
	    	//Scelgo un numero casuale tra gli indici della lista di TestFrame
	    	int randomTestFrameIndex = ThreadLocalRandom.current().nextInt(1, (TestFrameCSVListIO.getCSVLength(testFrameListPath) + 1));
	    	
	    	//Leggo il TestFrame con l'indice scelto
	    	FrameBean testFrameBean = TestFrameCSVListIO.readRowFromCSV(testFrameListPath, randomTestFrameIndex);
	    	
	    	//Creo una APIRequest con i campi del TestFrame estratto
	    	APIRequest apiRequest = new APIRequest(testFrameBean);
	    	apiRequest.setBaseURL(baseURL);
	    	apiRequest.setApiUsername(apiUsername);
	    	apiRequest.setApiKey(apiKey);
	    	
	    	System.out.println(Integer.MAX_VALUE);
	    	
//	    	apiRequest.sendRequest();
	    	
	    	
    }
}
