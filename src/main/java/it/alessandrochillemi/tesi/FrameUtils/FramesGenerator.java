package it.alessandrochillemi.tesi.FrameUtils;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrame;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFrameMap;

public class FramesGenerator {

	public static void main(String[] args) {
//		HTTPMethod method = HTTPMethod.POST;
//		String endpoint = "/users";
//		
//		ArrayList<DiscourseParam> paramList = new ArrayList<DiscourseParam>();
		
//		//Param 1
//		ArrayList<String> validValuesP1 = new ArrayList<String>(Arrays.asList(new String[] {"active","new","staff","suspended","blocked","suspect"}));
//		DiscourseParam p1 = new DiscourseParam("flag", DiscourseTypeParam.ENUM, Param.Position.PATH, DiscourseResourceType.NO_RESOURCE, validValuesP1);
//		paramList.add(p1);

//		Double probSelection = 1.0/11062.0;
//		Double probFailure = 0.0 + new Random().nextDouble() * (1.0 - 0.0);
//
//		List<FrameBean<DiscoursePreCondition>> frameBeansList = DiscourseEquivalenceClass.generateFrameBeans(method,endpoint,paramList,probSelection,probFailure);
//		
//		frameMap.append(frameBeansList);

//		frameMap.print();
		
//		frameMap.saveToFile("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/frame_map");
//		System.out.println("ok!");

		String apiDescriptionsCSVFilePath = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_api_descriptions.csv";
		String discourseFrameMapCSVFilePath = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/discourse_frames.csv";
	
		Double probSelection = 1.0/8802.0;
		Double probFailure = 0.0;
		Double trueProbSelection = 0.0;
		Double trueProbFailure = 0.0;
		
		ArrayList<DiscourseFrame> discourseFrames = DiscourseFrame.generateFromCSV(apiDescriptionsCSVFilePath, probSelection, probFailure, trueProbSelection, trueProbFailure);
		
		DiscourseFrameMap discourseFrameMap = new DiscourseFrameMap();
		discourseFrameMap.append(discourseFrames);
		
		discourseFrameMap.writeToCSVFile(discourseFrameMapCSVFilePath);
		
		System.out.println("Frames generated!");
		
	}

}
