package it.alessandrochillemi.tesi.FrameUtils;

public abstract class PreCondition{
	protected ResourceType resourceType;
	protected String value;
	
	public PreCondition(){
		
	}
	
	public PreCondition(ResourceType resourceType, String value){
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

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
	
	public abstract void generatePreConditionValue(String baseURL, String apiUsername, String apiKey);
	
	public abstract void print();

}
