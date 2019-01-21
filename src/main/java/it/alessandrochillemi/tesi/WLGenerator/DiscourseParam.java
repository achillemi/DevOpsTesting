package it.alessandrochillemi.tesi.WLGenerator;

import java.util.ArrayList;

import org.apache.commons.lang3.Validate;

public class DiscourseParam extends Param<DiscoursePreCondition>{
	
	private DiscourseTypeParam typeParam;					//Tipo del parametro
	private DiscourseEquivalenceClass classParam;			//Classe di equivalenza del parametro
	
	private static final long serialVersionUID = 284320922843475342L;
	
	public DiscourseParam(){
		
	}
	
	public DiscourseParam(String keyParam, DiscourseTypeParam typeParam, Position position, DiscourseEquivalenceClass classParam, DiscourseResourceType resourceType, ArrayList<String> validValues){
		//Check that classParam is not null
		Validate.notNull(classParam, "classParam can't be null!");
		
		this.keyParam = keyParam;
		this.typeParam = typeParam;
		this.position = position;
		this.classParam = classParam;
		this.resourceType = resourceType;
		this.validValues = validValues;
		generateValue();
	}
	
	public DiscourseParam(String keyParam, DiscourseTypeParam typeParam, Position position, DiscourseEquivalenceClass classParam, DiscourseResourceType resourceType){
		//Check that classParam is not null
		Validate.notNull(classParam, "classParam can't be null!");

		this.keyParam = keyParam;
		this.typeParam = typeParam;
		this.position = position;
		this.classParam = classParam;
		this.resourceType = resourceType;
		this.validValues = new ArrayList<String>();
		generateValue();
	}
	
	public DiscourseParam(String keyParam, DiscourseTypeParam typeParam, Position position, DiscourseResourceType resourceType, ArrayList<String> validValues){
		this.keyParam = keyParam;
		this.typeParam = typeParam;
		this.position = position;
		this.resourceType = resourceType;
		this.validValues = validValues;
		generateValue();
	}
	
	public DiscourseParam(String keyParam, DiscourseTypeParam typeParam, Position position, DiscourseResourceType resourceType){
		this.keyParam = keyParam;
		this.typeParam = typeParam;
		this.position = position;
		this.resourceType = resourceType;
		this.validValues = new ArrayList<String>();
		generateValue();
	}
	
	public DiscourseParam(DiscourseParam discourseParam){
		this.keyParam = discourseParam.getKeyParam();
		this.typeParam = discourseParam.getTypeParam();
		this.position = discourseParam.getPosition();
		this.classParam = discourseParam.getClassParam();
		this.resourceType = discourseParam.getResourceType();
		this.validValues = discourseParam.getValidValues();
		this.value = discourseParam.getValue();
		
	}
	
	public DiscourseTypeParam getTypeParam() {
		return typeParam;
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

	public void generateValue(){
		if(this.classParam != null){
			this.value = this.classParam.generateValue(validValues);
		}
	}
	
	public void generateValue(ArrayList<DiscoursePreCondition> preConditionList) {
		if(classParam != null){
			this.value = this.classParam.generateValue(validValues);
		}
		if(classParam.toString().endsWith("_VALID")){
			for(DiscoursePreCondition preCondition : preConditionList){
				if(preCondition.getResourceType().equals(this.getResourceType())){
					if(preCondition.getValue() != null){
						this.setValue(preCondition.getValue());
					}
				}
			}
		}
	}
	
	public void print(){
		System.out.print(keyParam + " " + position + " " + classParam + " " + resourceType + " " + value);
	}

}
