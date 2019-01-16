package it.alessandrochillemi.tesi.WLGenerator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class Param implements Serializable{
	private String keyParam;													//Nome del parametro
	private EquivalenceClass classParam;										//Classe di equivalenza del parametro
	private ArrayList<String> validValues;										//Eventuale elenco di valori validi per un enumerativo
	private String value;														//Valore del parametro
	private String resourceType;												//Tipo della risorsa
	
	private static final long serialVersionUID = 1L;
	
	public Param(){
		
	}
	
	public Param(String keyParam, EquivalenceClass classParam, String resourceType){
		this.keyParam = keyParam;
		this.classParam = classParam;
		this.validValues = new ArrayList<String>();
		this.resourceType = resourceType;
		generateValue();
	}
	
	public Param(String keyParam, EquivalenceClass classParam, String resourceType, ArrayList<String> validValues){
		this.keyParam = keyParam;
		this.classParam = classParam;
		this.validValues = validValues;
		this.resourceType = resourceType;
		generateValue();
	}

	public String getKeyParam() {
		return keyParam;
	}

	public void setKeyParam(String keyParam) {
		this.keyParam = keyParam;
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

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public void generateValue(){
		int nextInt = 0;
		long nextLong = 0L;
		Date date = null;
		StringBuilder stringBuilder = null;
		
		if(classParam != null){
			switch(classParam){
				case BOOLEAN_EMPTY:
					this.value="";
					break;
				case BOOLEAN_NULL:
					this.value="NULL";
					break;
				case BOOLEAN_VALID:
					this.value = String.valueOf(RandomUtils.nextBoolean());
					break;
				case COL_EMPTY:
					this.value="";
					break;
				case COL_INVALID:
					//Generate random alphanumeric string with random length from 1 to 10, different than 6 to make the color invalid
					do{
						nextInt = RandomUtils.nextInt(1, 11);
					} while(nextInt == 6);
					
					this.value = RandomStringUtils.random(nextInt, true, true);
					break;
				case COL_NULL:
					this.value="NULL";
					break;
				case COL_VALID:
					//Generate random hexadecimal number from 0x000000 to 0xFFFFFF and save it as a string with leading # and 0s
					nextInt = RandomUtils.nextInt(0, 0xffffff + 1);
					this.value = String.format("#%06x", nextInt);
					break;
				case COL_VERY_LONG:
					//Generate random alphanumeric string with 2^16+1 characters (color parameters are still strings eventually)
					nextInt = 65537;
					this.value = RandomStringUtils.random(nextInt, true, true);
					break;
				case DATE_EMPTY:
					this.value="";
					break;
				case DATE_INVALID:
					//Generate a new Date from a random Long representing milliseconds from Jan 1, 1970.
					nextLong = RandomUtils.nextLong(0, 48L * 365 * 24 * 60 * 60 * 1000);
					date = new Date(nextLong);
					this.value = new SimpleDateFormat("yyyy-MM-dd").format(date);
					
					stringBuilder = new StringBuilder(value);
					
					/* Set the first month character to a random integer from 2 to 9 to make the date invalid;
					  for example, if the random generated date is 2017-10-10 and the random integer is 5,
					  the invalid date will be 2017-50-10 */
					stringBuilder.setCharAt(5, Character.forDigit(RandomUtils.nextInt(2, 10), 10));
					
					this.value = stringBuilder.toString();
					break;
				case DATE_NULL:
					this.value="NULL";
					break;
				case DATE_VALID:
					//Generate a new Date from a random Long representing milliseconds from Jan 1, 1970.
					nextLong = RandomUtils.nextLong(0, 48L * 365 * 24 * 60 * 60 * 1000);
					date = new Date(nextLong);
					this.value = new SimpleDateFormat("yyyy-MM-dd").format(date);
					break;
				case DATE_VERY_LONG:
					//Generate random alphanumeric string with 2^16+1 characters (date parameters are still strings eventually)
					nextInt = 65537;
					this.value = RandomStringUtils.random(nextInt, true, true);
					break;
				case LIST_EMPTY:
					this.value="";
					break;
				case LIST_NULL:
					this.value="NULL";
					break;
				case LIST_VALID:
					//Generate a list of random length (from 1 to 10) made of random alphanumeric strings with random lenghts (from 0 to 10)
					nextInt = RandomUtils.nextInt(1, 11);
					stringBuilder = new StringBuilder();
					for(int i = 0; i<nextInt; i++){
						stringBuilder.append(RandomStringUtils.random(RandomUtils.nextInt(1, 11),true,true));
						stringBuilder.append(",");
					}
					this.value = stringBuilder.toString();
					break;
				case NUM_ABSOLUTE_MINUS_ONE:
					this.value="-1";
					break;
				case NUM_ABSOLUTE_ZERO:
					this.value="0";
					break;
				case NUM_EMPTY:
					this.value="";
					break;
				case NUM_NULL:
					this.value="NULL";
					break;
				case NUM_VALID:
					//Generate random integer from 1 to Integer.MAX_VALUE
					this.value = String.valueOf(RandomUtils.nextInt(1, Integer.MAX_VALUE));
					break;
				case NUM_VERY_BIG:
					//value = Integer.MAX_VALUE + 1
					this.value="2147483648";
					break;
				case STR_EMPTY:
					this.value="";
					break;
				case STR_INVALID:
					//Generate random string with length from 1 to 2^16 with non-printable characters (ASCII code from 0 to 31)
					this.value = RandomStringUtils.random(RandomUtils.nextInt(1, 65537), 0, 31, false, false);
					break;
				case STR_NULL:
					this.value="NULL";
					break;
				case STR_VALID:
					//Generate random alphanumeric string with length from 1 to 2^16
					this.value = RandomStringUtils.random(RandomUtils.nextInt(1, 65537), true, true);
					break;
				case STR_VERY_LONG:
					//Generate random alphanumeric string with 2^16+1 characters
					nextInt = 65537;
					this.value = RandomStringUtils.random(nextInt, true, true);
					break;
				case CONSTANT:
					if(this.keyParam.equals("archetype")){
						this.value = "private_message";
					}
					break;
				default:
					break;
			
			}
		}
	}
	
	public void generateValue(List<PreCondition> discoursePreConditionList) {
		for(PreCondition discoursePreCondition : discoursePreConditionList){
			if(discoursePreCondition.getResourceType().equals(this.getResourceType())){
				this.setValue(discoursePreCondition.getValue());
				break;
			}
		}
	}
	
}
