package it.alessandrochillemi.tesi.WLGenerator;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.Validate;

public class Param<T extends PreCondition> implements Serializable{
	
	public enum Position{BODY,PATH,QUERY};
	
	protected String keyParam;													//Nome del parametro
	protected TypeParam typeParam;												//Tipo del parametro
	protected Position position;												//Posizione del parametro nella richiesta HTTP
	protected EquivalenceClass classParam;										//Classe di equivalenza del parametro
	protected ResourceType resourceType;										//Tipo della risorsa
	protected ArrayList<String> validValues;									//Eventuale elenco di valori validi per enumerativi
	protected String value;														//Valore del parametro
	
	private static final long serialVersionUID = 1L;
	
	public Param(){
		
	}
	
	public Param(String keyParam, TypeParam typeParam, Position position, EquivalenceClass classParam, ResourceType resourceType, ArrayList<String> validValues){
		//Check that classParam is not null
		Validate.notNull(classParam, "classParam can't be null!");
		
		this.keyParam = keyParam;
		this.typeParam = typeParam;
		this.position = position;
		this.classParam = classParam;
		this.resourceType = resourceType;
		this.validValues = validValues;
		this.value = classParam.generateValue(validValues);
	}
	
	public Param(String keyParam, TypeParam typeParam, Position position, EquivalenceClass classParam, ResourceType resourceType){
		//Check that classParam is not null
		Validate.notNull(classParam, "classParam can't be null!");
		
		this.keyParam = keyParam;
		this.typeParam = typeParam;
		this.position = position;
		this.classParam = classParam;
		this.resourceType = resourceType;
		this.validValues = new ArrayList<String>();
		this.value = classParam.generateValue(validValues);
	}
	
	public Param(Param<T> param){
		this.keyParam = param.getKeyParam();
		this.typeParam = param.getTypeParam();
		this.position = param.getPosition();
		this.classParam = param.getClassParam();
		this.resourceType = param.getResourceType();
		this.validValues = param.getValidValues();
		this.value = param.getValue();
	}

	public String getKeyParam() {
		return keyParam;
	}

	public void setKeyParam(String keyParam) {
		this.keyParam = keyParam;
	}

	public TypeParam getTypeParam() {
		return typeParam;
	}

	public void setTypeParam(TypeParam typeParam) {
		this.typeParam = typeParam;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public EquivalenceClass getClassParam() {
		return classParam;
	}

	public void setClassParam(EquivalenceClass classParam) {
		this.classParam = classParam;
	}
	
	public ArrayList<String> getValidValues() {
		return validValues;
	}

	public void setValidValues(ArrayList<String> validValues) {
		this.validValues = validValues;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
	
	public void generateValue(){
		if(this.classParam != null){
			this.value = this.classParam.generateValue(validValues);
		}
	}
	
	public void generateValue(ArrayList<T> preConditionList) {
		if(classParam != null){
			this.value = classParam.generateValue(validValues);
		}
		if(classParam.toString().endsWith("_VALID")){
			for(PreCondition preCondition : preConditionList){
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
