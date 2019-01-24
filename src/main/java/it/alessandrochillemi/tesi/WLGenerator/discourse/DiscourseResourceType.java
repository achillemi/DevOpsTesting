package it.alessandrochillemi.tesi.WLGenerator.discourse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import it.alessandrochillemi.tesi.WLGenerator.APIRequest;
import it.alessandrochillemi.tesi.WLGenerator.HTTPMethod;
import it.alessandrochillemi.tesi.WLGenerator.Param;
import it.alessandrochillemi.tesi.WLGenerator.ResourceType;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public enum DiscourseResourceType implements ResourceType{
	CATEGORY_ID,
	POST_ID,
	TOPIC_ID,
	TOPIC_SLUG,
	USERNAME1,
	USER_ID1,
	USERNAME2,
	USER_ID2,
	USERNAME_LIST,
	TAG,
	TAG_GROUP_ID,
	GROUP,
	GROUP_ID,
	UPLOAD_AVATAR_ID,
	NO_RESOURCE;
	
	private String categoryIDValue;
	private String topicIDValue;
	private String topicSlugValue;
	private String userID1Value;
	private String username1Value;
	private String userID2Value;
	private String username2Value;
	private String usernameListValue;
	private String tagGroupIDValue;
	private String tagValue;
	private String uploadAvatarIDValue;
	private String groupIDValue;
	private String groupValue;
	private String postIDValue;
	

	public String generatePreConditionValue(String baseURL, String apiUsername, String apiKey) {
		String value = null;
		if(this != null){
			switch(this){
			case CATEGORY_ID:
				if(categoryIDValue == null){
					generateCategoryDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = categoryIDValue;
				break;
			case GROUP:
				if(groupValue == null){
					generateGroupDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = groupValue;
				break;
			case GROUP_ID:
				if(groupIDValue == null){
					generateGroupDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = groupIDValue;
				break;
			case NO_RESOURCE:
				value = null;
				break;
			case POST_ID:
				if(postIDValue == null){
					generatePostDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = postIDValue;
				break;
			case TAG:
				if(tagValue == null){
					generateTagDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = tagValue;
				break;
			case TAG_GROUP_ID:
				if(tagGroupIDValue == null){
					generateTagDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = tagGroupIDValue;
				break;
			case TOPIC_ID:
				if(topicIDValue == null){
					generateTopicDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = topicIDValue;
				break;
			case TOPIC_SLUG:
				if(topicSlugValue == null){
					generateTopicDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = topicSlugValue;
				break;
			case UPLOAD_AVATAR_ID:
				if(uploadAvatarIDValue == null){
					generateUploadAvatarIDDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = uploadAvatarIDValue;
				break;
			case USERNAME1:
				if(username1Value == null){
					generateUsersDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = username1Value;
				break;
			case USERNAME2:
				if(username2Value == null){
					generateUsersDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = username2Value;
				break;
			case USERNAME_LIST:
				if(usernameListValue == null){
					generateUsersDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = usernameListValue;
				break;
			case USER_ID1:
				if(userID1Value == null){
					generateUsersDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = userID1Value;
				break;
			case USER_ID2:
				if(userID2Value == null){
					generateUsersDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
				}
				value = userID2Value;
				break;
			default:
				break;
			
			}
		}
		return value;
	}
	
	private void generateCategoryDiscoursePreConditionValues(String baseURL, String apiUsername, String apiKey){				
		//Create new category
		HTTPMethod method = HTTPMethod.POST;
		String endpoint = "/categories.json";
		
		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		DiscourseParam p1 = new DiscourseParam("name", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		paramList.add(p1);
		DiscourseParam p2 = new DiscourseParam("color", DiscourseTypeParam.COLOR, Param.Position.BODY, DiscourseEquivalenceClass.COL_VALID, DiscourseResourceType.NO_RESOURCE,true);
		paramList.add(p2);
		DiscourseParam p3 = new DiscourseParam("text_color", DiscourseTypeParam.COLOR, Param.Position.BODY, DiscourseEquivalenceClass.COL_VALID, DiscourseResourceType.NO_RESOURCE,true);
		paramList.add(p3);
		
		APIRequest<DiscourseParam> apiRequest = new APIRequest<DiscourseParam>(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	Response response = apiRequest.sendRequest();
    	
    	String stringResponseBody = null;
    	try {
    		stringResponseBody = response.body().string();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    	JSONObject jsonResponseBody = null;
    	try {
			jsonResponseBody = new JSONObject(stringResponseBody);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	categoryIDValue = jsonResponseBody.getJSONObject("category").get("id").toString();
	}
	
	private void generateTopicDiscoursePreConditionValues(String baseURL, String apiUsername, String apiKey){
		//Create new topic
		HTTPMethod method = HTTPMethod.POST;
		String endpoint = "/posts.json";
		
		//Set parameters
		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		DiscourseParam p1 = new DiscourseParam("title", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		paramList.add(p1);
		DiscourseParam p2 = new DiscourseParam("raw", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		paramList.add(p2);
		DiscourseParam p3 = new DiscourseParam("category", DiscourseTypeParam.NUMBER, Param.Position.BODY, DiscourseEquivalenceClass.NUM_VALID, DiscourseResourceType.CATEGORY_ID,true);
		paramList.add(p3);
		DiscourseParam p4 = new DiscourseParam("created_at", DiscourseTypeParam.DATE, Param.Position.BODY, DiscourseEquivalenceClass.DATE_VALID, DiscourseResourceType.NO_RESOURCE,true);
		paramList.add(p4);
		
		//Generate pre-conditions required for creating a new topic
		ArrayList<DiscoursePreCondition> preConditionList = new ArrayList<DiscoursePreCondition>();	
		if(categoryIDValue == null){
			generateCategoryDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
		}
		preConditionList.add(new DiscoursePreCondition(DiscourseResourceType.CATEGORY_ID,categoryIDValue));
		
		//Apply pre-conditions to params
		for(DiscourseParam p : paramList){
			p.generateValue(preConditionList);
		}
		
		//Create an API request
		APIRequest<DiscourseParam> apiRequest = new APIRequest<DiscourseParam>(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	//Send the request
    	Response response = apiRequest.sendRequest();
    	
    	String stringResponseBody = null;
    	try {
    		stringResponseBody = response.body().string();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    	JSONObject jsonResponseBody = null;
    	try {
			jsonResponseBody = new JSONObject(stringResponseBody);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	topicIDValue = jsonResponseBody.get("topic_id").toString();
    	topicSlugValue = jsonResponseBody.get("topic_slug").toString();
    	
	}
	
	private void generateUsersDiscoursePreConditionValues(String baseURL, String apiUsername, String apiKey){		
		//Create user1
		HTTPMethod method = HTTPMethod.POST;
		String endpoint = "/users";
		
		String username1 = RandomStringUtils.randomAlphanumeric(10,16);
		
		//Set params
		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		DiscourseParam p1 = new DiscourseParam("name", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p1.setValue(username1);
		paramList.add(p1);
		DiscourseParam p2 = new DiscourseParam("email", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p2.setValue(username1+"@unina.it");
		paramList.add(p2);
		DiscourseParam p3 = new DiscourseParam("password", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p3.setValue(UUID.randomUUID().toString());
		paramList.add(p3);
		DiscourseParam p4 = new DiscourseParam("username", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p4.setValue(username1);
		paramList.add(p4);
		DiscourseParam p5 = new DiscourseParam("active", DiscourseTypeParam.BOOLEAN, Param.Position.BODY, DiscourseEquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p5.setValue("true");
		paramList.add(p5);
		DiscourseParam p6 = new DiscourseParam("approved", DiscourseTypeParam.BOOLEAN, Param.Position.BODY, DiscourseEquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p6.setValue("true");
		paramList.add(p6);
		
		//Create an API Request
		APIRequest<DiscourseParam> apiRequest = new APIRequest<DiscourseParam>(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	//Send the request
    	Response response = apiRequest.sendRequest();
    	
    	String stringResponseBody = null;
    	try {
    		stringResponseBody = response.body().string();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    	JSONObject jsonResponseBody = null;
    	try {
			jsonResponseBody = new JSONObject(stringResponseBody);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	userID1Value = jsonResponseBody.get("user_id").toString();
    	username1Value = username1;
    	
    	//Create user2
    	String username2 = RandomStringUtils.randomAlphanumeric(10,16);

    	//Set params
    	paramList = new ArrayList<DiscourseParam>();
		p1 = new DiscourseParam("name", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p1.setValue(username2);
		paramList.add(p1);
		p2 = new DiscourseParam("email", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p2.setValue(username2+"@unina.it");
		paramList.add(p2);
		p3 = new DiscourseParam("password", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p3.setValue(UUID.randomUUID().toString());
		paramList.add(p3);
		p4 = new DiscourseParam("username", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p4.setValue(username2);
		paramList.add(p4);
		p5 = new DiscourseParam("active", DiscourseTypeParam.BOOLEAN, Param.Position.BODY, DiscourseEquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p5.setValue("true");
		paramList.add(p5);
		p6 = new DiscourseParam("approved", DiscourseTypeParam.BOOLEAN, Param.Position.BODY, DiscourseEquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p6.setValue("true");
		paramList.add(p6);

		//Create an API Request
    	apiRequest = new APIRequest<DiscourseParam>(method,endpoint,paramList);
    	apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);

    	//Send the request
    	response = apiRequest.sendRequest();

    	stringResponseBody = null;
    	try {
    		stringResponseBody = response.body().string();
    	} catch (IOException e2) {
    		// TODO Auto-generated catch block
    		e2.printStackTrace();
    	}

    	jsonResponseBody = null;
    	try {
    		jsonResponseBody = new JSONObject(stringResponseBody);
    	} catch (JSONException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}

    	userID2Value = jsonResponseBody.get("user_id").toString();
    	username2Value = username2;
    	
    	usernameListValue = username1+","+username2;
	}
	
	private void generateTagDiscoursePreConditionValues(String baseURL, String apiUsername, String apiKey){
		//Enable tagging
		HTTPMethod method = HTTPMethod.PUT;
		String endpoint = "/admin/site_settings/tagging_enabled";

		//Set params
		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		DiscourseParam p1 = new DiscourseParam("tagging_enabled", DiscourseTypeParam.BOOLEAN, Param.Position.BODY, DiscourseEquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p1.setValue("true");
		paramList.add(p1);

		//Create an API Request
		APIRequest<DiscourseParam> apiRequest = new APIRequest<DiscourseParam>(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
		apiRequest.setApiUsername(apiUsername);
		apiRequest.setApiKey(apiKey);

		//Send the request
		Response response = apiRequest.sendRequest();

		//Create tag group
		method = HTTPMethod.POST;
		endpoint = "/tag_groups.json";

		String tagGroupName = RandomStringUtils.randomAlphanumeric(5,11);
		String tag1 = RandomStringUtils.randomAlphanumeric(4, 11);
		String tag2 = RandomStringUtils.randomAlphanumeric(4, 11);
		String tagList = tag1+","+tag2;

		//Set params
		paramList = new ArrayList<DiscourseParam>();
		p1 = new DiscourseParam("name", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p1.setValue(tagGroupName);
		paramList.add(p1);
		DiscourseParam p2 = new DiscourseParam("tag_names[]", DiscourseTypeParam.LIST, Param.Position.BODY, DiscourseEquivalenceClass.LIST_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p2.setValue(tagList);
		paramList.add(p2);

		//Create an API Request
		apiRequest = new APIRequest<DiscourseParam>(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
		apiRequest.setApiUsername(apiUsername);
		apiRequest.setApiKey(apiKey);

		//Send the request
		response = apiRequest.sendRequest();

		String stringResponseBody = null;
		try {
			stringResponseBody = response.body().string();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		JSONObject jsonResponseBody = null;
		try {
			jsonResponseBody = new JSONObject(stringResponseBody);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		tagGroupIDValue = jsonResponseBody.getJSONObject("tag_group").get("id").toString();
		tagValue = tag1;
	}
	
	private void generateUploadAvatarIDDiscoursePreConditionValues(String baseURL, String apiUsername, String apiKey){			
		String endpoint = "/uploads.json";
		
		File avatar = new File("resources/mario_rossi.jpeg");
		
		//Build complete URL, adding api_key and api_username
		HttpUrl.Builder completeURLBuilder = HttpUrl.parse(baseURL+endpoint).newBuilder()
				.addQueryParameter("api_key", apiKey)
				.addQueryParameter("api_username", apiUsername);
		
		HttpUrl completeURL = completeURLBuilder.build();
		
		OkHttpClient client = new OkHttpClient();
		
		//Generate pre-conditions required for uploading an avatar
		if(userID1Value == null){
			generateUsersDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
		}
		
		//Costruisco il body della richiesta HTTP
		RequestBody requestBody = null;
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("user_id", userID1Value)
				.addFormDataPart("type", "avatar")
				.addFormDataPart("files[]", avatar.getName(),RequestBody.create(MediaType.parse("image/jpeg"), avatar))
				.addFormDataPart("synchronous", "true");
		
		requestBody = requestBodyBuilder.build();
		
		Request.Builder requestBuilder = new Request.Builder()
				.url(completeURL)
				.post(requestBody);
		
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
    	
    	String stringResponseBody = null;
    	try {
    		stringResponseBody = response.body().string();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    	JSONObject jsonResponseBody = null;
    	try {
			jsonResponseBody = new JSONObject(stringResponseBody);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	uploadAvatarIDValue = jsonResponseBody.get("id").toString();
	}
	
	private void generateGroupDiscoursePreConditionValues(String baseURL, String apiUsername, String apiKey){		
		
		//Create new group
		HTTPMethod method = HTTPMethod.POST;
		String endpoint = "/admin/groups";
		String group_name = RandomStringUtils.randomAlphanumeric(5,16);
		
		//Set params
		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		DiscourseParam p1 = new DiscourseParam("group[name]", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		p1.setValue(group_name);
		paramList.add(p1);
		
		//Create an API Request
		APIRequest<DiscourseParam> apiRequest = new APIRequest<DiscourseParam>(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	//Send the request
    	Response response = apiRequest.sendRequest();
    	
    	String stringResponseBody = null;
    	try {
    		stringResponseBody = response.body().string();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    	JSONObject jsonResponseBody = null;
    	try {
			jsonResponseBody = new JSONObject(stringResponseBody);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	groupIDValue = jsonResponseBody.getJSONObject("basic_group").get("id").toString();
    	groupValue = group_name;
    	
	}
	
	private void generatePostDiscoursePreConditionValues(String baseURL, String apiUsername, String apiKey){		
		
		//Create new post
		HTTPMethod method = HTTPMethod.POST;
		String endpoint = "/posts.json";
		
		//Set params
		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		DiscourseParam p1 = new DiscourseParam("topic_id", DiscourseTypeParam.NUMBER, Param.Position.BODY, DiscourseEquivalenceClass.NUM_VALID, DiscourseResourceType.TOPIC_ID,true);
		paramList.add(p1);
		DiscourseParam p2 = new DiscourseParam("raw", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseEquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE,true);
		paramList.add(p2);
		DiscourseParam p4 = new DiscourseParam("created_at", DiscourseTypeParam.DATE, Param.Position.BODY, DiscourseEquivalenceClass.DATE_VALID, DiscourseResourceType.NO_RESOURCE,true);
		paramList.add(p4);
		
		//Generate pre-conditions required for creating a new post
		ArrayList<DiscoursePreCondition> preConditionList = new ArrayList<DiscoursePreCondition>();	
		if(topicIDValue == null){
			generateTopicDiscoursePreConditionValues(baseURL,apiUsername,apiKey);
		}
		preConditionList.add(new DiscoursePreCondition(DiscourseResourceType.TOPIC_ID,topicIDValue));
		
		//Apply pre-conditions to params
		for(DiscourseParam p : paramList){
			p.generateValue(preConditionList);
		}
		
		//Create an API Request
		APIRequest<DiscourseParam> apiRequest = new APIRequest<DiscourseParam>(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	//Send the request
    	Response response = apiRequest.sendRequest();
    	
    	String stringResponseBody = null;
    	try {
    		stringResponseBody = response.body().string();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    	JSONObject jsonResponseBody = null;
    	try {
			jsonResponseBody = new JSONObject(stringResponseBody);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	postIDValue = jsonResponseBody.get("id").toString();
    	
	}

}
