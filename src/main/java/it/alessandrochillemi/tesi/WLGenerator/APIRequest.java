package it.alessandrochillemi.tesi.WLGenerator;

import java.io.IOException;
import java.util.ArrayList;
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
	
	private ArrayList<Param> bodyParamList;							//Lista di parametri nel body della richiesta
	private ArrayList<Param> pathParamList;							//Lista di parametri nel path dell'API
	private ArrayList<Param> queryParamList;						//Lista di parametri nella query
		
	public APIRequest(){
		
	}
	
	public APIRequest(FrameBean frameBean){
		this.method = frameBean.getMethod();
		this.endpoint = frameBean.getEndpoint();
		this.bodyParamList = frameBean.getBodyParamList();
		this.pathParamList = frameBean.getPathParamList();
		this.queryParamList = frameBean.getQueryParamList();
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

	public ArrayList<Param> getBodyParamList() {
		return bodyParamList;
	}

	public void setBodyParamList(ArrayList<Param> bodyParamList) {
		this.bodyParamList = bodyParamList;
	}

	public ArrayList<Param> getPathParamList() {
		return pathParamList;
	}

	public void setPathParamList(ArrayList<Param> pathParamList) {
		this.pathParamList = pathParamList;
	}

	public ArrayList<Param> getQueryParamList() {
		return queryParamList;
	}

	public void setQueryParamList(ArrayList<Param> queryParamList) {
		this.queryParamList = queryParamList;
	}

	public Response sendRequest(){
		
		//Pattern che identifica i parametri racchiusi da parentesi {}, ovvero i path parameters
		Pattern p = Pattern.compile("\\{([\\w ]*)\\}");
		
		//Sostituisco eventuali path parameters nell'endpoint con i valori reali
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
			completeURLBuilder = completeURLBuilder.addQueryParameter(queryParamList.get(j).getKeyParam(), queryParamList.get(j).getValue());
		}
		HttpUrl completeURL = completeURLBuilder.build();
		
		System.out.println(completeURL);
		

		OkHttpClient client = new OkHttpClient();
		
		//Costruisco il body della richiesta HTTP, se necessario
		RequestBody requestBody = null;
		if(bodyParamList.size()>0){
			MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
					.setType(MultipartBody.FORM);

			for(int j=0; j<bodyParamList.size(); j++){
				requestBodyBuilder = requestBodyBuilder.addFormDataPart(bodyParamList.get(j).getKeyParam(), bodyParamList.get(j).getValue());
			}

			requestBody = requestBodyBuilder.build();
		}
		
//		RequestBody requestBody = new MultipartBody.Builder()
//				.setType(MultipartBody.FORM)
//				.addFormDataPart("api_key", apiKey)
//				.addFormDataPart("api_username", apiUsername)
//				.addFormDataPart("name", "categoria_di_prova2")
//				.addFormDataPart("color", "49d9e9")
//				.addFormDataPart("text_color", "f0fcfd")	
//				.build();
		
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
