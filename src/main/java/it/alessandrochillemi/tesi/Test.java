package it.alessandrochillemi.tesi;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.utils.DoubleUtils;

public class Test {

	public static void main(String[] args) {

		ArrayList<Double> doubleList = new ArrayList<Double>();
		
		doubleList.add(0.7);
		doubleList.add(0.4);
		doubleList.add(0.5);
		
		DoubleUtils.normalize(doubleList);
		
		Double sum = 0.0;
		for(Double d : doubleList){
			System.out.println("\n" + d);
			sum += d;
		}
		System.out.println("\n" + sum);
	}

}
