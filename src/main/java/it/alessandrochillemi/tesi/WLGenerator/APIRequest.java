package it.alessandrochillemi.tesi.WLGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private String endpoint;										//Endpoint dell'API
	
	private ArrayList<? extends Param> paramList;					//Lista di parametri della richiesta
	private ArrayList<PreCondition> preConditionList;				//Lista di precondizioni
		
	public APIRequest(){
		
	}
	
	public APIRequest(FrameBean frameBean, ArrayList<PreCondition> preConditionList){
		this.method = frameBean.getMethod();
		this.endpoint = frameBean.getEndpoint();
		this.paramList = frameBean.getParamList();
		this.preConditionList = preConditionList;
	}
	
	public APIRequest(FrameBean frameBean){
		this.method = frameBean.getMethod();
		this.endpoint = frameBean.getEndpoint();
		this.paramList = frameBean.getParamList();
		this.preConditionList = new ArrayList<PreCondition>();
	}
	
	public APIRequest(HTTPMethod method, String endpoint, ArrayList<? extends Param> paramList){
		this.method = method;
		this.endpoint = endpoint;
		this.paramList = paramList;
		this.preConditionList = new ArrayList<PreCondition>();
	}
	
	public APIRequest(HTTPMethod method, String endpoint, ArrayList<? extends Param> paramList, ArrayList<PreCondition> preConditionList){
		this.method = method;
		this.endpoint = endpoint;
		this.paramList = paramList;
		this.preConditionList = preConditionList;
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

	public ArrayList<? extends Param> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<? extends Param> paramList) {
		this.paramList = paramList;
	}

	public ArrayList<PreCondition> getPreConditionList() {
		return preConditionList;
	}

	public void setPreConditionList(ArrayList<PreCondition> preConditionList) {
		this.preConditionList = preConditionList;
	}
	
	public void generateValue(){
		for(Param param : paramList){
			param.generateValue(preConditionList);
		}
	}

	//I parametri devono avere già un valore prima di inviare la richiesta; è possibile assegnare un valore a tutti i parametri con generateValue();
	public Response sendRequest(){
		ArrayList<Param> pathParamList = new ArrayList<Param>();
		ArrayList<Param> queryParamList = new ArrayList<Param>();
		ArrayList<Param> bodyParamList = new ArrayList<Param>();
		
		//Suddivido tutti i parametri in body, path e query parameters
		for(Param param : paramList){
			if(param.getPosition().equals(Param.Position.PATH)){
				pathParamList.add(param);
			}
			if(param.getPosition().equals(Param.Position.QUERY)){
				queryParamList.add(param);
			}
			if(param.getPosition().equals(Param.Position.BODY)){
				bodyParamList.add(param);
			}
		}
		
		//Pattern che identifica i parametri racchiusi da parentesi {}, ovvero i path parameters nell'endpoint
		Pattern p = Pattern.compile("\\{([\\w ]*)\\}");
		
		//Sostituisco eventuali path parameters nell'endpoint con i valori reali; assumo che non possono esistere path parameter di tipo "array"
		Matcher m = p.matcher(endpoint);
		StringBuffer sb = new StringBuffer();
		int i = 0;
		while (m.find()) {
			m.appendReplacement(sb, pathParamList.get(i).getValue());
			i++;
		}
		m.appendTail(sb);
		
		endpoint = sb.toString();
		
		//Costruisco l'URL completa, aggiungendo eventuali query parameters oltre a api_key e api_username
		HttpUrl.Builder completeURLBuilder = HttpUrl.parse(baseURL+endpoint).newBuilder()
				.addQueryParameter("api_key", apiKey)
				.addQueryParameter("api_username", apiUsername);
		for(int j=0; j<queryParamList.size(); j++){
			//Se il parametro termina con [], si tratta di un array e va quindi diviso in più parametri
			if(queryParamList.get(j).getKeyParam().endsWith("[]")){
				//Divido l'array nei suoi elementi (sono separati da virgole)
				List<String> queryParamArrayElements = Arrays.asList(queryParamList.get(j).getValue().split(","));
				
				//Aggiungo un query parameter per ogni valore
				for(String value : queryParamArrayElements){
					completeURLBuilder = completeURLBuilder.addQueryParameter(queryParamList.get(j).getKeyParam(), value);
				}
			}
			else{
				completeURLBuilder = completeURLBuilder.addQueryParameter(queryParamList.get(j).getKeyParam(), queryParamList.get(j).getValue());
			}
		}
		HttpUrl completeURL = completeURLBuilder.build();
		
//		System.out.println(completeURL);	

		OkHttpClient client = new OkHttpClient();
		
		//Costruisco il body della richiesta HTTP, se necessario
		RequestBody requestBody = null;
		if(bodyParamList.size()>0){
			MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
					.setType(MultipartBody.FORM);

			for(int j=0; j<bodyParamList.size(); j++){
				//Se il parametro termina con [], si tratta di un array e va quindi diviso in più parametri
				if(bodyParamList.get(j).getKeyParam().endsWith("[]")){
					//Divido l'array nei suoi elementi (sono separati da virgole)
					List<String> bodyParamArrayElements = Arrays.asList(bodyParamList.get(j).getValue().split(","));
					
					//Aggiungo un body parameter per ogni valore
					for(String value : bodyParamArrayElements){
						requestBodyBuilder = requestBodyBuilder.addFormDataPart(bodyParamList.get(j).getKeyParam(), value);
					}
				}
				else{
					requestBodyBuilder = requestBodyBuilder.addFormDataPart(bodyParamList.get(j).getKeyParam(), bodyParamList.get(j).getValue());
				}
//				System.out.println(bodyParamList.get(j).getKeyParam() + ":");
//				System.out.println(bodyParamList.get(j).getValue());
			}

			requestBody = requestBodyBuilder.build();
		}
		
		//Costruisco la richiesta HTTP
		Request.Builder requestBuilder = new Request.Builder()
				.url(completeURL);
		
		switch(method){
			case DELETE:
				if(requestBody != null){
					requestBuilder = requestBuilder.delete(requestBody);
				}
				else{
					requestBuilder = requestBuilder.delete();
				}
				break;
			case GET:
				requestBuilder = requestBuilder.get();
				break;
			case POST:
				requestBuilder = requestBuilder.post(requestBody);
				break;
			case PUT:
				requestBuilder = requestBuilder.put(requestBody);
				break;
			default:
				break;
		}
		
		Request request = requestBuilder
				.addHeader("cache-control", "no-cache")
				.build();
		
		Response response = null;
		try {
			response = client.newCall(request).execute();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
		
	}

}
