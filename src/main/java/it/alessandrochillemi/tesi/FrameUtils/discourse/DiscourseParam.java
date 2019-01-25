package it.alessandrochillemi.tesi.FrameUtils.discourse;

import java.util.ArrayList;

import it.alessandrochillemi.tesi.FrameUtils.Param;

public class DiscourseParam extends Param<DiscoursePreCondition>{
	
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
		System.out.print("\nKey: " + keyParam);
		System.out.print("\nPosition:  " + position);
		System.out.print("\nClass:  " + classParam);
		System.out.print("\nResource Type:  " + resourceType);
		System.out.print("\nValue:  " + value);
	}

}
