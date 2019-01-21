package it.alessandrochillemi.tesi.WLGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Test {

	public static void main(String[] args) {
//		FrameMap<DiscoursePreCondition> frameMap = new FrameMap<DiscoursePreCondition>("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/frame_map_new");
		

//		FrameMap<DiscoursePreCondition> frameMap = new FrameMap<DiscoursePreCondition>();
		
		
//		HTTPMethod method = HTTPMethod.DELETE;
//		String endpoint = "/groups/{group_id}/members.json";
//		
//		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		
//		//Param 1
//		ArrayList<String> validValuesP1 = new ArrayList<String>(Arrays.asList(new String[] {"active","new","staff","suspended","blocked","suspect"}));
//		DiscourseParam p1 = new DiscourseParam("flag", DiscourseTypeParam.ENUM, Param.Position.PATH, DiscourseResourceType.NO_RESOURCE, validValuesP1);
//		paramList.add(p1);
//		//Param 2
//		DiscourseParam p2 = new DiscourseParam("user_id", DiscourseTypeParam.NUMBER, Param.Position.BODY, DiscourseResourceType.USER_ID1);
//		paramList.add(p2);
//		//Param 3
//		ArrayList<String> validValuesP3 = new ArrayList<String>(Arrays.asList(new String[] {"created","last_emailed","seen","username","email","trust_level","days_visited","posts_read","topics_viewed","posts","read_time"}));
//		DiscourseParam p3 = new DiscourseParam("order", DiscourseTypeParam.ENUM, Param.Position.QUERY, DiscourseResourceType.NO_RESOURCE, validValuesP3);
//		paramList.add(p3);
//		//Param 4
//		DiscourseParam p4 = new DiscourseParam("group_id", DiscourseTypeParam.NUMBER, Param.Position.PATH, DiscourseResourceType.GROUP_ID);
//		paramList.add(p4);
//		//Param 5
//		ArrayList<String> validValuesP5 = new ArrayList<String>(Arrays.asList(new String[] {"1","2","3","4","5","6","7","9","11","12","13","14","15","16"}));
//		DiscourseParam p5 = new DiscourseParam("filter", DiscourseTypeParam.ENUM, Param.Position.QUERY, DiscourseResourceType.NO_RESOURCE,validValuesP5);
//		paramList.add(p5);
//		//Param 6
//		DiscourseParam p6 = new DiscourseParam("page", DiscourseTypeParam.NUMBER, Param.Position.QUERY, DiscourseResourceType.NO_RESOURCE);
//		paramList.add(p6);
//		//Param 7
//		ArrayList<String> validValuesP7 = new ArrayList<String>(Arrays.asList(new String[] {"private_message"}));
//		DiscourseParam p7 = new DiscourseParam("archetype", DiscourseTypeParam.ENUM, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE,validValuesP7);
//		paramList.add(p7);
//		//Param 8
//		DiscourseParam p8 = new DiscourseParam("show_emails", DiscourseTypeParam.BOOLEAN, Param.Position.QUERY, DiscourseResourceType.NO_RESOURCE);
//		paramList.add(p8);
//		//Param 9
//		DiscourseParam p9 = new DiscourseParam("id", DiscourseTypeParam.NUMBER, Param.Position.PATH, DiscourseResourceType.USER_ID1);
//		paramList.add(p9);
//		//Param 10
//		DiscourseParam p10 = new DiscourseParam("approved", DiscourseTypeParam.BOOLEAN, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE);
//		paramList.add(p10);

//		Double probSelection = 1.0/11062.0;
//		Double probFailure = 0.0 + new Random().nextDouble() * (1.0 - 0.0);
//
//		List<FrameBean<DiscoursePreCondition>> frameBeansList = DiscourseEquivalenceClass.generateFrameBeans(method,endpoint,paramList,probSelection,probFailure);
//		
//		frameMap.append(frameBeansList);
		
//		frameMap.deleteFrames(method, endpoint);
//		frameMap.updateFrameBeansByEndpoint(method, endpoint, frameBeansList);
		
//		frameMap.print(method,endpoint);
//		frameMap.print();
		
//		frameMap.readByKey(17531).print();
		
//		frameMap.saveToFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/frame_map_new");
//		System.out.println("ok!");
		
    	ResponseLogList<DiscoursePreCondition> responseLogList = new ResponseLogList<DiscoursePreCondition>("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/response_log");
    	System.out.println(responseLogList.size());
    	responseLogList.get(0).print();
//    	for(Param<DiscoursePreCondition> p : responseLogList.get(0).getParamList()){
//    		p.print();
//    	}
		
	}

}
