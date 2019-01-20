package it.alessandrochillemi.tesi.WLGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class Test {

	public static void main(String[] args) {
		FrameMap frameMap = new FrameMap("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/frame_map");
		

//		FrameMap frameMap = new FrameMap();
		
		//PROSSIMA API: GET /users/{username}.json
		
		HTTPMethod method = HTTPMethod.PUT;
		String endpoint = "/tag_groups/{id}.json";
//		
		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		
		//Param 1
//		ArrayList<String> validValuesP1 = new ArrayList<String>(Arrays.asList(new String[] {"active","new","staff","suspended","blocked","suspect"}));
//		DiscourseParam p1 = new DiscourseParam("flag", TypeParam.ENUM, Param.Position.PATH, DiscourseResourceType.NO_RESOURCE.toString(), validValuesP1);
//		paramList.add(p1);
		//Param 2
		DiscourseParam p2 = new DiscourseParam("name", TypeParam.STRING, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE.toString());
		paramList.add(p2);
//		//Param 3
//		ArrayList<String> validValuesP3 = new ArrayList<String>(Arrays.asList(new String[] {"created","last_emailed","seen","username","email","trust_level","days_visited","posts_read","topics_viewed","posts","read_time"}));
//		Param p3 = new Param("order", TypeParam.ENUM, Param.Position.QUERY, DiscourseResourceType.NO_RESOURCE.toString(), validValuesP3);
//		paramList.add(p3);
		//Param 4
		DiscourseParam p4 = new DiscourseParam("tag_names[]", TypeParam.LIST, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE.toString());
		paramList.add(p4);
//		//Param 5
//		ArrayList<String> validValuesP5 = new ArrayList<String>(Arrays.asList(new String[] {"1","2","3","4","5","6","7","9","11","12","13","14","15","16"}));
//		Param p5 = new Param("filter", TypeParam.ENUM, Param.Position.QUERY, DiscourseResourceType.NO_RESOURCE.toString(),validValuesP5);
//		paramList.add(p5);
		//Param 6
		DiscourseParam p6 = new DiscourseParam("id", TypeParam.NUMBER, Param.Position.PATH, DiscourseResourceType.TAG_GROUP_ID.toString());
		paramList.add(p6);
//		//Param 7
//		Param p7 = new Param("id", TypeParam.NUMBER, Param.Position.PATH, DiscourseResourceType.USER_ID1.toString());
//		paramList.add(p7);
//		//Param 8
//		Param p8 = new Param("approved", TypeParam.BOOLEAN, Param.Position.BODY, DiscourseResourceType.NO_RESOURCE.toString());
//		paramList.add(p8);

		Double probSelection = 1.0/11062.0;
		Double probFailure = 0.0 + new Random().nextDouble() * (1.0 - 0.0);
//
		List<FrameBean> frameBeansList = FrameBean.generateFrameBeans(method,endpoint,paramList,probSelection,probFailure);
//		
//		frameMap.append(frameBeansList);
		
//		frameMap.deleteFrames(method, endpoint);
		frameMap.updateFrameBeansByEndpoint(method, endpoint, frameBeansList);
		
		frameMap.print(method,endpoint);
//		frameMap.print();
		
//		frameMap.readByKey(17531).print();
		
//		frameMap.saveToFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/frame_map");
//		System.out.println("ok!");
		
//		FrameBean frameBean = frameMap.readByKey(358);
//		ArrayList<? extends Param> discourseParamList = frameBean.getParamList();
//		Param p = discourseParamList.get(0);

	}

}
