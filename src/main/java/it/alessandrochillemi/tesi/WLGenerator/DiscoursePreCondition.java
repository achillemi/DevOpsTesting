package it.alessandrochillemi.tesi.WLGenerator;

public class DiscoursePreCondition extends PreCondition {
	
	private DiscourseResourceType discourseResourceType;

	public DiscoursePreCondition(String value, DiscourseResourceType discourseResourceType) {
		super(value);
		this.discourseResourceType = discourseResourceType;
	}

	public DiscourseResourceType getDiscourseResourceType() {
		return discourseResourceType;
	}

	public void setDiscourseResourceType(DiscourseResourceType discourseResourceType) {
		this.discourseResourceType = discourseResourceType;
	}

}
