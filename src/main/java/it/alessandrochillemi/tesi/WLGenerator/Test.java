package it.alessandrochillemi.tesi.WLGenerator;

import java.util.ArrayList;
import java.util.Random;

import it.alessandrochillemi.tesi.WLGenerator.discourse.DiscourseFrame;
import it.alessandrochillemi.tesi.WLGenerator.discourse.DiscourseFrameMap;

public class Test {

	public static void main(String[] args) {
//		FrameMap<DiscoursePreCondition> frameMap = new FrameMap<DiscoursePreCondition>("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/frame_map");
		

//		FrameMap<DiscoursePreCondition> frameMap = new FrameMap<DiscoursePreCondition>();
		
		
//		HTTPMethod method = HTTPMethod.POST;
//		String endpoint = "/users";
//		
//		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		
//		//Param 1
//		ArrayList<String> validValuesP1 = new ArrayList<String>(Arrays.asList(new String[] {"active","new","staff","suspended","blocked","suspect"}));
//		DiscourseParam p1 = new DiscourseParam("flag", DiscourseTypeParam.ENUM, Param.Position.PATH, DiscourseResourceType.NO_RESOURCE, validValuesP1);
//		paramList.add(p1);
//		//Param 2
//		DiscourseParam p2 = new DiscourseParam("name", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE);
//		paramList.add(p2);
//		//Param 3
//		ArrayList<String> validValuesP3 = new ArrayList<String>(Arrays.asList(new String[] {"created","last_emailed","seen","username","email","trust_level","days_visited","posts_read","topics_viewed","posts","read_time"}));
//		DiscourseParam p3 = new DiscourseParam("order", DiscourseTypeParam.ENUM, Param.Position.QUERY, DiscourseResourceType.NO_RESOURCE, validValuesP3);
//		paramList.add(p3);
//		//Param 4
//		DiscourseParam p4 = new DiscourseParam("email", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE);
//		paramList.add(p4);
//		//Param 5
//		ArrayList<String> validValuesP5 = new ArrayList<String>(Arrays.asList(new String[] {"1","2","3","4","5","6","7","9","11","12","13","14","15","16"}));
//		DiscourseParam p5 = new DiscourseParam("filter", DiscourseTypeParam.ENUM, Param.Position.QUERY, DiscourseResourceType.NO_RESOURCE,validValuesP5);
//		paramList.add(p5);
//		//Param 6
//		DiscourseParam p6 = new DiscourseParam("password", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE);
//		paramList.add(p6);
//		//Param 7
//		ArrayList<String> validValuesP7 = new ArrayList<String>(Arrays.asList(new String[] {"private_message"}));
//		DiscourseParam p7 = new DiscourseParam("archetype", DiscourseTypeParam.ENUM, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE,validValuesP7);
//		paramList.add(p7);
//		//Param 8
//		DiscourseParam p8 = new DiscourseParam("username", DiscourseTypeParam.STRING, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE);
//		paramList.add(p8);
//		//Param 9
//		DiscourseParam p9 = new DiscourseParam("active", DiscourseTypeParam.BOOLEAN, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE);
//		paramList.add(p9);
//		//Param 10
//		DiscourseParam p10 = new DiscourseParam("approved", DiscourseTypeParam.BOOLEAN, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE);
//		paramList.add(p10);
//
		Double probSelection = 1.0/11062.0;
		Double probFailure = 0.0 + new Random().nextDouble() * (1.0 - 0.0);
//
//		List<FrameBean<DiscoursePreCondition>> frameBeansList = DiscourseEquivalenceClass.generateFrameBeans(method,endpoint,paramList,probSelection,probFailure);
//		
//		frameMap.append(frameBeansList);
		
//		frameMap.deleteFrames(method, endpoint);
//		frameMap.updateFrameBeansByEndpoint(method, endpoint, frameBeansList);
		
//		frameMap.print(method,endpoint);
//		frameMap.print();
		
//		frameMap.readByKey(17531).print();
		
//		frameMap.saveToFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/frame_map");
//		System.out.println("ok!");
		
//		ArrayList<DiscourseFrame> list = new ArrayList<DiscourseFrame>();
//		DiscourseFrame frame = new DiscourseFrame();
//		
//		frame.setMethod(HTTPMethod.GET);
//		frame.setEndpoint("aaa");
//		frame.setParamList(paramList);
//		frame.setProbFailure(0.0);
//		frame.setProbSelection(0.0);
//		
//		list.add(frame);
//
//
		
		
		DiscourseFrameMap f = new DiscourseFrameMap("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_frames.csv");
		
//		ArrayList<DiscourseFrame> discourseFrames = DiscourseFrame.generateFromCSV("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_api_descriptions.csv", probSelection, probFailure);
		
//		f.append(discourseFrames);
		
		f.print(HTTPMethod.POST, "/users");
		
//		f.writeToCSVFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_frames.csv");
		System.out.println("ok");
		
//		Reader in = null;
//		try {
//			in = new FileReader("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_frames.csv");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Iterable<CSVRecord> records = null;
//		try {
//			records = CSVFormat.RFC4180.withDelimiter(';').withFirstRecordAsHeader().parse(in);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		FrameMap<DiscourseFrame> f = new FrameMap<DiscourseFrame>();
//		
//		for (CSVRecord record : records) {
//			ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
//			ArrayList<DiscourseFrame> discourseFrames = new ArrayList<DiscourseFrame>();
//			
//			//Create a list of DiscourseParam from the values of the row
//			//Read API method and endpoint
//			HTTPMethod method = HTTPMethod.valueOf(record.get("METHOD"));
//			String endpoint = record.get("ENDPOINT");
//			
//			//Read the parameter's features for each of the 6 parameters (at most) on a row
//			for(int i = 1; i<=6; i++){
//			    String key = record.get("P"+i+"_KEY");
//			    String type = record.get("P"+i+"_TYPE");
//			    String position = record.get("P"+i+"_POSITION");
//			    String resourceType = record.get("P"+i+"_RESOURCE_TYPE");
//			    String isRequired = record.get("P"+i+"_IS_REQUIRED");
//			    String validValues = record.get("P"+i+"_VALID_VALUES");
//			    
//			    //If P_KEY!=null and P_KEY!="/", create a new parameter and add it to the list; otherwise, it means that there are no more parameters
//			    if(key != null && !key.equals("/")){
//			    	DiscourseTypeParam discourseType = EnumUtils.getEnumIgnoreCase(DiscourseTypeParam.class, type);
//			    	Position discoursePosition = EnumUtils.getEnumIgnoreCase(Position.class, position);
//			    	DiscourseResourceType discourseResourceType = EnumUtils.getEnumIgnoreCase(DiscourseResourceType.class, resourceType);
//			    	boolean discourseIsRequired = Boolean.parseBoolean(isRequired);
//			    	ArrayList<String> discourseValidValues = new ArrayList<String>();
//			    	if(!validValues.equals("/")){
//			    		discourseValidValues.addAll(Arrays.asList(validValues.split(",")));
//			    	}
//			    	
//			    	DiscourseParam p = new DiscourseParam(key,discourseType,discoursePosition,discourseResourceType,discourseIsRequired,discourseValidValues);
//			    	paramList.add(p);
//			    }
//			}
//			//Get the list of DiscourseFrames and add it to the return array
//			discourseFrames = DiscourseEquivalenceClass.generateDiscourseFrames(method, endpoint, paramList, probSelection, probFailure);
//			f.append(discourseFrames);
//		}
		
		
//		System.out.println(f.size()+"\n");
//		f.print();
		
//		String firstEmailPart = RandomStringUtils.randomAlphanumeric(1, RandomUtils.nextInt(1,11-4));
//		String secondEmailPart = RandomStringUtils.randomAlphanumeric(1, (11-3-firstEmailPart.length()));
//		String thirdEmailPart = RandomStringUtils.randomAlphanumeric(1, (11-2-firstEmailPart.length()-secondEmailPart.length()));
//		String value = firstEmailPart+"@"+secondEmailPart+"."+thirdEmailPart;
//		System.out.println(value + " length: " + value.length());
		
	}

}
