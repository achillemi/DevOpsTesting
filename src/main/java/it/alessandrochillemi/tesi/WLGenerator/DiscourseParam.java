package it.alessandrochillemi.tesi.WLGenerator;

import java.util.List;

public class DiscourseParam extends Param<DiscoursePreCondition> {
	
	private DiscourseResourceType discourseResourceType;									//Tipo della risorsa

	private static final long serialVersionUID = 1L;

	public DiscourseParam(String keyParam, EquivalenceClass classParam, DiscourseResourceType discourseResourceType){
		super(keyParam,classParam);
		this.discourseResourceType = discourseResourceType;
	}
	
	public DiscourseResourceType getDiscourseResourceType() {
		return discourseResourceType;
	}

	public void setDiscourseResourceType(DiscourseResourceType discourseResourceType) {
		this.discourseResourceType = discourseResourceType;
	}

	public void generateValue(List<DiscoursePreCondition> discoursePreConditionList) {
		for(DiscoursePreCondition discoursePreCondition : discoursePreConditionList){
			if(discoursePreCondition.getDiscourseResourceType().equals(discourseResourceType)){
				this.setValue(discoursePreCondition.getValue());
				break;
			}
		}
	}

}
