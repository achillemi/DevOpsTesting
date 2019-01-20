package it.alessandrochillemi.tesi.WLGenerator;

import java.util.ArrayList;

public class DiscourseParam extends Param{
	
	public DiscourseParam(){
		
	}
	
	public DiscourseParam(String keyParam, TypeParam typeParam, Position position, EquivalenceClass classParam, String resourceType, ArrayList<String> validValues){
		super(keyParam,typeParam,position,classParam,resourceType,validValues);
		generateValue();
	}
	
	public DiscourseParam(String keyParam, TypeParam typeParam, Position position, EquivalenceClass classParam, String resourceType){
		super(keyParam,typeParam,position,classParam,resourceType);
		generateValue();
	}
	
	public DiscourseParam(String keyParam, TypeParam typeParam, Position position, String resourceType, ArrayList<String> validValues){
		super(keyParam,typeParam,position,resourceType,validValues);
		generateValue();
	}
	
	public DiscourseParam(String keyParam, TypeParam typeParam, Position position, String resourceType){
		super(keyParam,typeParam,position,resourceType);
		generateValue();
	}
	
	public DiscourseParam(Param param){
		super(param);
		generateValue();
	}

	private static final long serialVersionUID = 284320922843475342L;
	
	public void generateValue(){
		if(classParam != null){
			this.value = this.classParam.generateValue(validValues);
		}
	}

}
