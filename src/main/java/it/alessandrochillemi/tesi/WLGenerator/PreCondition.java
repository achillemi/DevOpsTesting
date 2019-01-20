package it.alessandrochillemi.tesi.WLGenerator;

public class PreCondition{
	private String resourceType;
	private String value;
	
	public PreCondition(){
		
	}
	
	public PreCondition(String resourceType, String value){
		this.resourceType = resourceType;
		this.value = value;
	}
	
	public PreCondition(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	public void print(){
		System.out.println(resourceType + ": " + value);
	}

}
