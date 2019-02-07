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

		FrameMap frameMap = applicationFactory.makeFrameMap("discourse_frames.csv");
		
	}

}
