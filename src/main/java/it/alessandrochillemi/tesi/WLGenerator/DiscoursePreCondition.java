package it.alessandrochillemi.tesi.WLGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DiscoursePreCondition{
	
	private static String baseURL;
	private static String apiUsername;
	private static String apiKey;
	
	private static PreCondition categoryIDPreCondition;
	private static PreCondition topicIDPreCondition;
	private static PreCondition topicSlugPreCondition;
	private static PreCondition username1PreCondition;
	private static PreCondition userID1PreCondition;
	private static PreCondition username2PreCondition;
	private static PreCondition userID2PreCondition;
	private static PreCondition usernameListPreCondition;
	private static PreCondition tagPreCondition;
	private static PreCondition tagGroupIDPreCondition;
	private static PreCondition uploadAvatarIDPreCondition;
	private static PreCondition groupPreCondition;
	private static PreCondition groupIDPreCondition;
	
	private DiscoursePreCondition(){
		
	}
	
	private static void loadEnvironment(){
		//Carico le variabili d'ambiente (base_url, api_key, api_username)
    	Properties environment = new Properties();
    	InputStream is = null;
    	try {
    		is = new FileInputStream(WLGenerator.ENVIRONMENT_PATH);
    	} catch (FileNotFoundException e1) {
    		e1.printStackTrace();
    	}
    	try {
    		environment.load(is);
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	}
    	
    	//Leggo le variabili d'ambiente
    	baseURL = environment.getProperty("base_url");
    	apiUsername = environment.getProperty("api_username");
    	apiKey = environment.getProperty("api_key");
		
	}
	
	public static ArrayList<PreCondition> generateDiscoursePreConditions(){
		ArrayList<PreCondition> ret = new ArrayList<PreCondition>();
		
		generateCategoryDiscoursePreConditions();
		generateTopicDiscoursePreConditions();
		generateUsersDiscoursePreConditions();
		generateTagDiscoursePreConditions();
		generateUploadAvatarIDDiscoursePreConditions();
		generateGroupDiscoursePreConditions();
		
		ret.add(categoryIDPreCondition);
		ret.add(topicIDPreCondition);
		ret.add(topicSlugPreCondition);
		ret.add(username1PreCondition);
		ret.add(username2PreCondition);
		ret.add(userID1PreCondition);
		ret.add(userID2PreCondition);
		ret.add(usernameListPreCondition);
		ret.add(tagPreCondition);
		ret.add(tagGroupIDPreCondition);
		ret.add(uploadAvatarIDPreCondition);
		ret.add(groupPreCondition);
		ret.add(groupIDPreCondition);
		
		return ret;
	}
	
	private static void generateCategoryDiscoursePreConditions(){		
		loadEnvironment();
		
		//Create new category
		HTTPMethod method = HTTPMethod.POST;
		String endpoint = "/categories.json";
		
		ArrayList<Param> paramList = new ArrayList<Param>();
		Param p1 = new Param("name", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		paramList.add(p1);
		Param p2 = new Param("color", TypeParam.COLOR, Param.Position.BODY, EquivalenceClass.COL_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		paramList.add(p2);
		Param p3 = new Param("text_color", TypeParam.COLOR, Param.Position.BODY, EquivalenceClass.COL_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		paramList.add(p3);
		
		APIRequest apiRequest = new APIRequest(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	Response response = apiRequest.sendRequest();
//    	System.out.println("RESPONSE CODE: " + response.code());
//    	System.out.println("RESPONSE MESSAGE: " + response.message());
    	
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
    	
    	String categoryID = jsonResponseBody.getJSONObject("category").get("id").toString();

    	categoryIDPreCondition = new PreCondition(DiscourseResourceType.CATEGORY_ID.toString(),categoryID);
	}
	
	private static void generateTopicDiscoursePreConditions(){		
		loadEnvironment();
		
		//Create new topic
		HTTPMethod method = HTTPMethod.POST;
		String endpoint = "/posts.json";
		
		ArrayList<Param> paramList = new ArrayList<Param>();
		Param p1 = new Param("title", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		paramList.add(p1);
		Param p2 = new Param("raw", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		paramList.add(p2);
		Param p3 = new Param("category", TypeParam.NUMBER, Param.Position.BODY, EquivalenceClass.NUM_VALID, DiscourseResourceType.CATEGORY_ID.toString());
		paramList.add(p3);
		Param p4 = new Param("created_at", TypeParam.DATE, Param.Position.BODY, EquivalenceClass.DATE_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		paramList.add(p4);
		
		ArrayList<PreCondition> preConditionList = new ArrayList<PreCondition>();
		preConditionList.add(categoryIDPreCondition);
		
		APIRequest apiRequest = new APIRequest(method,endpoint,paramList,preConditionList);
		apiRequest.generateValue();
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	Response response = apiRequest.sendRequest();
//    	System.out.println("RESPONSE CODE: " + response.code());
//    	System.out.println("RESPONSE MESSAGE: " + response.message());
    	
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
    	
    	String topicID = jsonResponseBody.get("topic_id").toString();
    	topicIDPreCondition = new PreCondition(DiscourseResourceType.TOPIC_ID.toString(),topicID);
    	
    	String topicSlug = jsonResponseBody.get("topic_slug").toString();
    	topicSlugPreCondition = new PreCondition(DiscourseResourceType.TOPIC_SLUG.toString(),topicSlug);
    	
	}
	
	private static void generateUsersDiscoursePreConditions(){		
		loadEnvironment();
		
		//Create user1
		HTTPMethod method = HTTPMethod.POST;
		String endpoint = "/users";
		
		String username1 = RandomStringUtils.randomAlphanumeric(10,16);
		
		ArrayList<Param> paramList = new ArrayList<Param>();
		Param p1 = new Param("name", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p1.setValue(username1);
		paramList.add(p1);
		Param p2 = new Param("email", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p2.setValue(username1+"@unina.it");
		paramList.add(p2);
		Param p3 = new Param("password", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p3.setValue(UUID.randomUUID().toString());
		paramList.add(p3);
		Param p4 = new Param("username", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p4.setValue(username1);
		paramList.add(p4);
		Param p5 = new Param("active", TypeParam.BOOLEAN, Param.Position.BODY, EquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p5.setValue("true");
		paramList.add(p5);
		Param p6 = new Param("approved", TypeParam.BOOLEAN, Param.Position.BODY, EquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p6.setValue("true");
		paramList.add(p6);
		
		ArrayList<PreCondition> preConditionList = new ArrayList<PreCondition>();
		
		APIRequest apiRequest = new APIRequest(method,endpoint,paramList,preConditionList);
//		apiRequest.generateValue();
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	Response response = apiRequest.sendRequest();
//    	System.out.println("RESPONSE CODE: " + response.code());
//    	System.out.println("RESPONSE MESSAGE: " + response.message());
    	
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
    	
    	String userID1 = jsonResponseBody.get("user_id").toString();
    	username1PreCondition = new PreCondition(DiscourseResourceType.USERNAME1.toString(),username1);
    	userID1PreCondition = new PreCondition(DiscourseResourceType.USER_ID1.toString(),userID1);
    	
    	//Create user2
    	String username2 = RandomStringUtils.randomAlphanumeric(10,16);

    	ArrayList<Param> paramList2 = new ArrayList<Param>();
    	Param p7 = new Param("name", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
    	p7.setValue(username2);
    	paramList2.add(p7);
    	Param p8 = new Param("email", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
    	p8.setValue(username2+"@unina.it");
    	paramList2.add(p8);
    	Param p9 = new Param("password", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
    	p9.setValue(UUID.randomUUID().toString());
    	paramList2.add(p9);
    	Param p10 = new Param("username", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
    	p10.setValue(username2);
    	paramList2.add(p10);
    	Param p11 = new Param("active", TypeParam.BOOLEAN, Param.Position.BODY, EquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE.toString());
    	p11.setValue("true");
    	paramList2.add(p11);
    	Param p12 = new Param("approved", TypeParam.BOOLEAN, Param.Position.BODY, EquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE.toString());
    	p12.setValue("true");
    	paramList2.add(p12);

    	ArrayList<PreCondition> preConditionList2 = new ArrayList<PreCondition>();

    	APIRequest apiRequest2 = new APIRequest(method,endpoint,paramList2,preConditionList2);
    	//apiRequest.generateValue();
    	apiRequest2.setBaseURL(baseURL);
    	apiRequest2.setApiUsername(apiUsername);
    	apiRequest2.setApiKey(apiKey);

    	Response response2 = apiRequest2.sendRequest();
//    	System.out.println("RESPONSE CODE: " + response2.code());
//    	System.out.println("RESPONSE MESSAGE: " + response2.message());

    	String stringResponseBody2 = null;
    	try {
    		stringResponseBody2 = response2.body().string();
    	} catch (IOException e2) {
    		// TODO Auto-generated catch block
    		e2.printStackTrace();
    	}

    	JSONObject jsonResponseBody2 = null;
    	try {
    		jsonResponseBody2 = new JSONObject(stringResponseBody2);
    	} catch (JSONException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}

    	String userID2 = jsonResponseBody2.get("user_id").toString();
    	username2PreCondition = new PreCondition(DiscourseResourceType.USERNAME2.toString(),username2);
    	userID2PreCondition = new PreCondition(DiscourseResourceType.USER_ID2.toString(),userID2);
    	
    	String usernameList = username1+","+username2;
    	usernameListPreCondition = new PreCondition(DiscourseResourceType.USERNAME_LIST.toString(),usernameList);

	}
	
	private static void generateTagDiscoursePreConditions(){		
		loadEnvironment();
		
		//Enable tagging
		HTTPMethod method = HTTPMethod.PUT;
		String endpoint = "/admin/site_settings/tagging_enabled";
		
		ArrayList<Param> paramList = new ArrayList<Param>();
		Param p1 = new Param("tagging_enabled", TypeParam.BOOLEAN, Param.Position.BODY, EquivalenceClass.BOOLEAN_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p1.setValue("true");
		paramList.add(p1);
		
		APIRequest apiRequest = new APIRequest(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	Response response = apiRequest.sendRequest();
		
    	//Create tag group
    	method = HTTPMethod.POST;
		endpoint = "/tag_groups.json";
    	
		String tagGroupName = RandomStringUtils.randomAlphanumeric(5,11);
		String tag1 = RandomStringUtils.randomAlphanumeric(4, 11);
		String tag2 = RandomStringUtils.randomAlphanumeric(4, 11);
		String tagList = tag1+","+tag2;
		
		paramList = new ArrayList<Param>();
		p1 = new Param("name", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p1.setValue(tagGroupName);
		paramList.add(p1);
		Param p2 = new Param("tag_names[]", TypeParam.LIST, Param.Position.BODY, EquivalenceClass.LIST_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p2.setValue(tagList);
		paramList.add(p2);
		
		apiRequest = new APIRequest(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	response = apiRequest.sendRequest();
//    	System.out.println("RESPONSE CODE: " + response.code());
//    	System.out.println("RESPONSE MESSAGE: " + response.message());
    	
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
    	
    	String tag_group_ID = jsonResponseBody.getJSONObject("tag_group").get("id").toString();
    	
    	tagGroupIDPreCondition = new PreCondition(DiscourseResourceType.TAG_GROUP_ID.toString(),tag_group_ID);
    	tagPreCondition = new PreCondition(DiscourseResourceType.TAG.toString(),tag1);
	}
	
	private static void generateUploadAvatarIDDiscoursePreConditions(){		
		loadEnvironment();
		
		String endpoint = "/uploads.json";
		
		File avatar = new File("resources/mario_rossi.jpeg");
		
		//Costruisco l'URL completa, aggiungendo api_key e api_username
		HttpUrl.Builder completeURLBuilder = HttpUrl.parse(baseURL+endpoint).newBuilder()
				.addQueryParameter("api_key", apiKey)
				.addQueryParameter("api_username", apiUsername);
		
		HttpUrl completeURL = completeURLBuilder.build();
		
		OkHttpClient client = new OkHttpClient();
		
		//Costruisco il body della richiesta HTTP
		RequestBody requestBody = null;
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("user_id", userID1PreCondition.getValue())
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
		
//		System.out.println("RESPONSE CODE: " + response.code());
//    	System.out.println("RESPONSE MESSAGE: " + response.message());
    	
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
    	
    	String upload_avatar_ID = jsonResponseBody.get("id").toString();
    	
    	uploadAvatarIDPreCondition = new PreCondition(DiscourseResourceType.UPLOAD_AVATAR_ID.toString(),upload_avatar_ID);
		
	}
	
	private static void generateGroupDiscoursePreConditions(){		
		loadEnvironment();
		
		//Create new group
		HTTPMethod method = HTTPMethod.POST;
		String endpoint = "/admin/groups";
		String group_name = RandomStringUtils.randomAlphanumeric(5,16);
		
		ArrayList<Param> paramList = new ArrayList<Param>();
		Param p1 = new Param("group[name]", TypeParam.STRING, Param.Position.BODY, EquivalenceClass.STR_VALID, DiscourseResourceType.NO_RESOURCE.toString());
		p1.setValue(group_name);
		paramList.add(p1);
		
		APIRequest apiRequest = new APIRequest(method,endpoint,paramList);
		apiRequest.setBaseURL(baseURL);
    	apiRequest.setApiUsername(apiUsername);
    	apiRequest.setApiKey(apiKey);
    	
    	Response response = apiRequest.sendRequest();
//    	System.out.println("RESPONSE CODE: " + response.code());
//    	System.out.println("RESPONSE MESSAGE: " + response.message());
    	
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
    	
    	String group_ID = jsonResponseBody.getJSONObject("basic_group").get("id").toString();
    	
    	groupIDPreCondition = new PreCondition(DiscourseResourceType.GROUP_ID.toString(),group_ID);
    	groupPreCondition = new PreCondition(DiscourseResourceType.GROUP.toString(),group_name);
    	
	}

}
