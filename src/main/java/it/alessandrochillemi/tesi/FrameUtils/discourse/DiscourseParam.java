package it.alessandrochillemi.tesi.FrameUtils.discourse;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.FrameUtils.Param;

public class DiscourseParam extends Param{
	
	private DiscourseTypeParam typeParam;
	private DiscourseEquivalenceClass classParam;
	private DiscourseResourceType resourceType;
	
	private static final long serialVersionUID = 284320922843475342L;
	
	public DiscourseParam(String keyParam, DiscourseTypeParam typeParam, Param.Position position, DiscourseEquivalenceClass classParam, DiscourseResourceType resourceType, boolean required, ArrayList<String> validValues){
		
		super(keyParam,typeParam,position,classParam,resourceType,required,validValues);
		
		this.typeParam = typeParam;
		this.classParam = classParam;
		this.resourceType = resourceType;
		
		this.generateValue();
	}
	
	public DiscourseParam(String keyParam, DiscourseTypeParam typeParam, Position position, DiscourseEquivalenceClass classParam, DiscourseResourceType resourceType, boolean required){
		
		super(keyParam,typeParam,position,classParam,resourceType,required);

		this.typeParam = typeParam;
		this.classParam = classParam;
		this.resourceType = resourceType;
		
		this.generateValue();
	}
	
	public DiscourseParam(String keyParam, DiscourseTypeParam typeParam, Position position, DiscourseResourceType resourceType, boolean required, ArrayList<String> validValues){
		
		super(keyParam,typeParam,position,resourceType,required,validValues);

		this.typeParam = typeParam;
		this.resourceType = resourceType;

		this.generateValue();
	}
	
	public DiscourseParam(String keyParam, DiscourseTypeParam typeParam, Position position, DiscourseResourceType resourceType, boolean required){
		
		super(keyParam,typeParam,position,resourceType,required);
		
		this.typeParam = typeParam;
		this.resourceType = resourceType;
		
		this.generateValue();
	}
	
	public DiscourseParam(DiscourseParam discourseParam){
		
		super(discourseParam);
		
		this.typeParam = discourseParam.getTypeParam();
		this.classParam = discourseParam.getClassParam();
		this.resourceType = discourseParam.getResourceType();

	}
	
	public DiscourseTypeParam getTypeParam() {
		return this.typeParam;
	}

	public void setTypeParam(DiscourseTypeParam typeParam) {
		this.typeParam = typeParam;
	}

	public DiscourseEquivalenceClass getClassParam() {
		return classParam;
	}

	public void setClassParam(DiscourseEquivalenceClass classParam) {
		this.classParam = classParam;
	}

	public DiscourseResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(DiscourseResourceType resourceType) {
		this.resourceType = resourceType;
	}
	
	public boolean isValid(){
		boolean valid = true;
		//Se la classe di equivalenza è "invalid", il valore è invalido
		if(this.classParam.isInvalid()){
			valid = false;
		}
		//Se la classe di equivalenza è "empty" e il parametro è obbligatorio, il valore è invalido
		else if(this.classParam.isEmpty() && this.isRequired()){
			valid = false;
		}
		return valid;
	}
	
	public void generateValue(){
		if(this.classParam != null){
			this.value = this.classParam.generateValue(validValues);
		}
	}
	
	public void generateValueWithPreConditions(String baseURL, String apiUsername, String apiKey) {
		if(this.isValid()){
			this.value = resourceType.generatePreConditionValue(baseURL,apiUsername,apiKey);
		}
		if(this.value == null){
			this.generateValue();
		}
	}
	
	public void print(){
		System.out.print("\nKey: " + keyParam);
		System.out.print("\nPosition:  " + position);
		System.out.print("\nClass:  " + classParam);
		System.out.print("\nResource Type:  " + resourceType);
		System.out.print("\nValue:  " + value);
	}

}
