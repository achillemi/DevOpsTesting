package it.alessandrochillemi.tesi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Test {
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	public static final String ENVIRONMENT_FILE_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
	private static String apiDescriptionsFilePath;
	private static String frameMapFilePath;
	private static String reliabilityResponseLogListFilePath;
	private static String testResponseLogListPath;
	private static String userResponseLogListPath;
	private static String reliabilityFilePath;
	private static String baseURL;
	private static String apiUsername;
	private static String apiKey;

	private static void loadEnvironment(){
		//Carico le variabili d'ambiente (path della lista di testframe, api_key, api_username, ecc.)
		Properties environment = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(ENVIRONMENT_FILE_PATH);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			environment.load(is);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//Leggo le variabili d'ambiente
		apiDescriptionsFilePath = environment.getProperty("api_descriptions_file_path");
		frameMapFilePath = environment.getProperty("frame_map_file_path");
		reliabilityResponseLogListFilePath = environment.getProperty("reliability_response_log_list_file_path");
		testResponseLogListPath = environment.getProperty("test_response_log_list_path");
		userResponseLogListPath = environment.getProperty("user_response_log_list_path");
		reliabilityFilePath = environment.getProperty("reliability_file_path");
		baseURL = environment.getProperty("base_url");
		apiUsername = environment.getProperty("api_username");
		apiKey = environment.getProperty("api_key");
	}

	public static void main(String[] args) {
		
		loadEnvironment();
		
		if(Files.exists(Paths.get(frameMapFilePath))){
			System.out.println("esiste!");
		}
		else{
			System.out.println("non esiste!");
		}
		
  

	}

}
