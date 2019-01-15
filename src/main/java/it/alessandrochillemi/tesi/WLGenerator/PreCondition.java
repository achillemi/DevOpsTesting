package it.alessandrochillemi.tesi.WLGenerator;

public abstract class PreCondition {
	private String value;
	
	public PreCondition(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
