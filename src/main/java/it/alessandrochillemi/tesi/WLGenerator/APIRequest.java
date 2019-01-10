package it.alessandrochillemi.tesi.WLGenerator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIRequest {
	
	private String baseURL;											//Indirizzo del dominio che ospita l'applicazione
	private String apiUsername;										//Username di chi vuole usare l'API
	private String apiKey;											//API Key relativa all'username di chi vuole usare l'API
	
	private HTTPMethod method;										//Metodo della richiesta HTTP per usare l'API
	private String endpoint;											//Endpoint dell'API
	
	private Map<String,EquivalenceClass> paramClasses;				//Coppie (chiave,classe di equivalenza) dei parametri
	private Multimap<String,String> paramValues;						/*Coppie (chiave,valore) dei parametri; è una "Multimap"
																	  per permettere chiavi ripetute, come si verifica nel
																	  caso delle liste, che sono scomposte in più parametri
																	  con lo stesso nome*/
		
	public APIRequest(){
		
	}
	
	public APIRequest(FrameBean testFrameBean){
		this.setMethod(testFrameBean.getMethod());
		this.setEndpoint(testFrameBean.getEndpoint());
		this.paramClasses = new HashMap<String,EquivalenceClass>();
		this.paramValues = HashMultimap.create();
		
		//Popolo la HashMap delle classi di equivalenza dei parametri dal testFrameBean
		for(int i = 1; i<=testFrameBean.getParamsNumber(); i++){
			try {
				Method getKeyParamMethod = FrameBean.class.getMethod("getKeyParam"+i, null);
				Method getClassParamMethod = FrameBean.class.getMethod("getClassParam"+i, null);
				
				String key = (String) getKeyParamMethod.invoke(testFrameBean, null);
				EquivalenceClass eqClass = (EquivalenceClass) getClassParamMethod.invoke(testFrameBean, null);
				
				paramClasses.put(key, eqClass);
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Popolo la HashMap dei valori di ogni parametro, generando i valori corrispondenti alle loro classi di equivalenza
		generateParamValues();
	}
	
	public String getBaseURL() {
		return baseURL;
	}
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getApiUsername() {
		return apiUsername;
	}

	public void setApiUsername(String apiUsername) {
		this.apiUsername = apiUsername;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public HTTPMethod getMethod() {
		return method;
	}
	public void setMethod(HTTPMethod method) {
		this.method = method;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Map<String, EquivalenceClass> getParamClasses() {
		return paramClasses;
	}

	public void setParamClasses(Map<String, EquivalenceClass> params) {
		this.paramClasses = params;
	}
	
	private void generateParamValues(){
		for(Map.Entry<String, EquivalenceClass> entry : paramClasses.entrySet()){
			switch(entry.getValue()){
				case BOOLEAN_EMPTY:
					paramValues.put(entry.getKey(), "");
					break;
				case BOOLEAN_NULL:
					paramValues.put(entry.getKey(), "NULL");
					break;
				case BOOLEAN_VALID:
					//Boolean casuale?
					paramValues.put(entry.getKey(), "true");
					break;
				case LIST_EMPTY:
					paramValues.put(entry.getKey(), "");
					break;
				case LIST_NULL:
					paramValues.put(entry.getKey(), "NULL");
					break;
				case LIST_VALID:
					//Come generare un parametro di tipo lista?
					paramValues.put(entry.getKey(), "apple");
					break;
				case NUM_ABSOLUTE_MINUS_ONE:
					paramValues.put(entry.getKey(), "-1");
					break;
				case NUM_ABSOLUTE_ZERO:
					paramValues.put(entry.getKey(), "0");
					break;
				case NUM_EMPTY:
					paramValues.put(entry.getKey(), "");
					break;
				case NUM_NULL:
					paramValues.put(entry.getKey(), "NULL");
					break;
				case NUM_VALID:
					//Number casuale?
					paramValues.put(entry.getKey(), "2345");
					break;
				case NUM_VERY_BIG:
					//Number very big casuale?
					paramValues.put(entry.getKey(), "2147483648");
					break;
				case STR_EMPTY:
					paramValues.put(entry.getKey(), "");
					break;
				case STR_INVALID:
					//Come generare stringhe non valide per colori o date?
					paramValues.put(entry.getKey(), "\n\n\n");
					break;
				case STR_NULL:
					paramValues.put(entry.getKey(), "NULL");
					break;
				case STR_VALID:
					//Stringa casuale?
					paramValues.put(entry.getKey(), "this_is_a_valid_string_!");
					break;
				case STR_VERY_LONG:
					//Stringa very long casuale? Quanto lunga?
					paramValues.put(entry.getKey(), "veryveryveryveryveryverylongstring");
					break;
				default:
					break;
			
			}
		}
	}

	public int sendRequest(){
		//Valore che dovrà essere calcolato in base alla classe di equivalenza
		String trueValue = "5";
		
		//Cerco nell'endpoint eventuali "path parameters" (che saranno racchiusi da parentesi {}) e li sostituisco
		//con i valori calcolati secondo le classi di equivalenza
		Pattern p = Pattern.compile("\\{([\\w ]*)\\}");
		Matcher m = p.matcher(endpoint);
		while(m.find()) {
//		    System.out.println(m.group(1));
			endpoint = m.replaceAll(trueValue);
		}
//		System.out.println(endpoint);
		
		//Costruisco l'URL completa
		HttpUrl completeURL = HttpUrl.parse(baseURL).newBuilder()
				.addPathSegments(endpoint)
				.build();
		
		System.out.println(completeURL);
		
		OkHttpClient client = new OkHttpClient();
		
		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("api_key", apiKey)
				.addFormDataPart("api_username", apiUsername)
				.addFormDataPart("name", "categoria_di_prova2")
				.addFormDataPart("color", "49d9e9")
				.addFormDataPart("text_color", "f0fcfd")	
				.build();
		
		Request request = new Request.Builder()
				.url("http://localhost:3000/categories/5")
				.put(requestBody)
				.addHeader("cache-control", "no-cache")
				.build();
		
		Response response = null;
		try {
			response = client.newCall(request).execute();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response.code();
		
	}

}
