package it.alessandrochillemi.tesi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import it.alessandrochillemi.tesi.FrameUtils.ApplicationFactory;
import it.alessandrochillemi.tesi.FrameUtils.FrameMap;
import it.alessandrochillemi.tesi.FrameUtils.ResponseLogList;
import it.alessandrochillemi.tesi.FrameUtils.discourse.DiscourseFactory;

public class Test {

	private static Random random = new Random();

	public static void main(String[] args) {

		//Creo una ApplicationFactory per l'applicazione desiderata
		ApplicationFactory applicationFactory = new DiscourseFactory();

		FrameMap frameMap = applicationFactory.makeFrameMap("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/frames_test.csv");

		ArrayList<Double> oldProbSelection = frameMap.getProbSelectionDistribution();

		//Scelgo la strategia di testing
		ITestingStrategy testingStrategy = new FirstTestingStrategy();

		ResponseLogList reliabilityResponseLogList = applicationFactory.makeResponseLogList("/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/response_log_list_test.csv");

		Monitor monitor = new Monitor();
		
		ArrayList<Double> newProbSelection = monitor.updateProbSelectionDistribution(oldProbSelection, reliabilityResponseLogList, 0.5);
		
		System.out.println("\nOLD PROBABILITY: ");
		Double oldSum = 0.0;
		for(int i = 0; i<oldProbSelection.size(); i++){
			System.out.println(oldProbSelection.get(i));
			oldSum += oldProbSelection.get(i);
		}
		System.out.println("\nOLD PROBABILITY SUM: " + oldSum);
		
		System.out.println("\nNEW PROBABILITY: ");
		Double newSum = 0.0;
		for(int i = 0; i<newProbSelection.size(); i++){
			System.out.println(newProbSelection.get(i));
			newSum += newProbSelection.get(i);
		}
		System.out.println("\nNEW PROBABILITY SUM: " + newSum);
		
	}

}
