package it.alessandrochillemi.tesi.FrameUtils.discourse;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import it.alessandrochillemi.tesi.FrameUtils.EquivalenceClass;
import it.alessandrochillemi.tesi.FrameUtils.HTTPMethod;

//Possibili classi di equivalenza per ogni parametro
public enum DiscourseEquivalenceClass implements EquivalenceClass{
	STR_NULL,STR_EMPTY,STR_VERY_LONG,STR_INVALID,STR_VALID,
	COL_EMPTY,COL_INVALID,COL_VALID,
	DATE_EMPTY,DATE_INVALID,DATE_VALID,
	EMAIL_EMPTY,EMAIL_INVALID,EMAIL_VALID,
	NUM_EMPTY,NUM_ABSOLUTE_MINUS_ONE,NUM_ABSOLUTE_ZERO,NUM_VERY_BIG,NUM_INVALID,NUM_VALID,
	LIST_NULL,LIST_EMPTY,LIST_VALID,
	BOOLEAN_EMPTY,BOOLEAN_INVALID,BOOLEAN_VALID,
	ENUM_EMPTY,ENUM_INVALID,ENUM_VALID;

	private static String[] stringEquivalenceClasses = new String[] {"STR_NULL","STR_EMPTY","STR_VERY_LONG","STR_INVALID","STR_VALID"};
	private static String[] colorEquivalenceClasses = new String[] {"COL_EMPTY","COL_INVALID","COL_VALID"};
	private static String[] dateEquivalenceClasses = new String[] {"DATE_EMPTY","DATE_INVALID","DATE_VALID"};
	private static String[] emailEquivalenceClasses = new String[] {"EMAIL_EMPTY","EMAIL_INVALID","EMAIL_VALID"};
	private static String[] numberEquivalenceClasses = new String[] {"NUM_EMPTY","NUM_ABSOLUTE_MINUS_ONE","NUM_ABSOLUTE_ZERO","NUM_VERY_BIG","NUM_INVALID","NUM_VALID"};
	private static String[] listEquivalenceClasses = new String[] {"LIST_NULL","LIST_EMPTY","LIST_VALID"};
	private static String[] booleanEquivalenceClasses = new String[] {"BOOLEAN_EMPTY","BOOLEAN_INVALID","BOOLEAN_VALID"};
	private static String[] enumEquivalenceClasses = new String[] {"ENUM_EMPTY","ENUM_INVALID","ENUM_VALID"};
	
	//Massimo numero di caratteri per ogni parametro
	private static final int MAX_LENGTH = 1001;
	
	public boolean isInvalid(){
		if(this.toString().endsWith("_INVALID")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isEmpty(){
		if(this.toString().endsWith("_EMPTY")){
			return true;
		}
		else{
			return false;
		}
	}

	//Genera la lista di tutte le possibili combinazioni di classi di equivalenza tra i gruppi selezionati;
	//i gruppi di classi di equivalenza sono selezionati automaticamente in base al tipo specificato in ingresso
	//per ogni parametro; viene usata la libreria Google Guava in combinazione con Java 8, prendendo spunto dalla risposta
	//seguente indirizzo: https://stackoverflow.com/a/37490796/5863657
	public static List<List<String>> cartesianProduct(DiscourseTypeParam typeParam1, DiscourseTypeParam typeParam2, DiscourseTypeParam typeParam3, DiscourseTypeParam typeParam4, DiscourseTypeParam typeParam5, DiscourseTypeParam typeParam6){
		List<String[]> elements = new LinkedList<String[]>();

		if(typeParam1 != null){
			switch(typeParam1){
			case STRING:
				elements.add(stringEquivalenceClasses);
				break;
			case COLOR:
				elements.add(colorEquivalenceClasses);
				break;
			case DATE:
				elements.add(dateEquivalenceClasses);
				break;
			case EMAIL:
				elements.add(emailEquivalenceClasses);
				break;
			case NUMBER:
				elements.add(numberEquivalenceClasses);
				break;
			case LIST:
				elements.add(listEquivalenceClasses);
				break;
			case BOOLEAN:
				elements.add(booleanEquivalenceClasses);
				break;
			case ENUM:
				elements.add(enumEquivalenceClasses);
				break;
			default:
				break;
			}
			if(typeParam2 != null){
				switch(typeParam2){
				case STRING:
					elements.add(stringEquivalenceClasses);
					break;
				case COLOR:
					elements.add(colorEquivalenceClasses);
					break;
				case DATE:
					elements.add(dateEquivalenceClasses);
					break;
				case EMAIL:
					elements.add(emailEquivalenceClasses);
					break;
				case NUMBER:
					elements.add(numberEquivalenceClasses);
					break;
				case LIST:
					elements.add(listEquivalenceClasses);
					break;
				case BOOLEAN:
					elements.add(booleanEquivalenceClasses);
					break;
				case ENUM:
					elements.add(enumEquivalenceClasses);
					break;
				default:
					break;
				}
				if(typeParam3 != null){
					switch(typeParam3){
					case STRING:
						elements.add(stringEquivalenceClasses);
						break;
					case COLOR:
						elements.add(colorEquivalenceClasses);
						break;
					case DATE:
						elements.add(dateEquivalenceClasses);
						break;
					case EMAIL:
						elements.add(emailEquivalenceClasses);
						break;
					case NUMBER:
						elements.add(numberEquivalenceClasses);
						break;
					case LIST:
						elements.add(listEquivalenceClasses);
						break;
					case BOOLEAN:
						elements.add(booleanEquivalenceClasses);
						break;
					case ENUM:
						elements.add(enumEquivalenceClasses);
						break;
					default:
						break;
					}
					if(typeParam4 != null){
						switch(typeParam4){
						case STRING:
							elements.add(stringEquivalenceClasses);
							break;
						case COLOR:
							elements.add(colorEquivalenceClasses);
							break;
						case DATE:
							elements.add(dateEquivalenceClasses);
							break;
						case EMAIL:
							elements.add(emailEquivalenceClasses);
							break;
						case NUMBER:
							elements.add(numberEquivalenceClasses);
							break;
						case LIST:
							elements.add(listEquivalenceClasses);
							break;
						case BOOLEAN:
							elements.add(booleanEquivalenceClasses);
							break;
						case ENUM:
							elements.add(enumEquivalenceClasses);
							break;
						default:
							break;
						}
						if(typeParam5 != null){
							switch(typeParam5){
							case STRING:
								elements.add(stringEquivalenceClasses);
								break;
							case COLOR:
								elements.add(colorEquivalenceClasses);
								break;
							case DATE:
								elements.add(dateEquivalenceClasses);
								break;
							case EMAIL:
								elements.add(emailEquivalenceClasses);
								break;
							case NUMBER:
								elements.add(numberEquivalenceClasses);
								break;
							case LIST:
								elements.add(listEquivalenceClasses);
								break;
							case BOOLEAN:
								elements.add(booleanEquivalenceClasses);
								break;
							case ENUM:
								elements.add(enumEquivalenceClasses);
								break;
							default:
								break;
							}
							if(typeParam6 != null){
								switch(typeParam6){
								case STRING:
									elements.add(stringEquivalenceClasses);
									break;
								case COLOR:
									elements.add(colorEquivalenceClasses);
									break;
								case DATE:
									elements.add(dateEquivalenceClasses);
									break;
								case EMAIL:
									elements.add(emailEquivalenceClasses);
									break;
								case NUMBER:
									elements.add(numberEquivalenceClasses);
									break;
								case LIST:
									elements.add(listEquivalenceClasses);
									break;
								case BOOLEAN:
									elements.add(booleanEquivalenceClasses);
									break;
								case ENUM:
									elements.add(enumEquivalenceClasses);
									break;
								default:
									break;
								}
							}
						}
					}
				}
			}
		}

		List<ImmutableList<String>> immutableElements = new LinkedList<>();
		elements.forEach(array -> {
			immutableElements.add(ImmutableList.copyOf(array));
		});

		List<List<String>> cartesianProduct = Lists.cartesianProduct(immutableElements);

		return cartesianProduct;	
	}
	
	//Generate a list of DiscourseFrame from a list of class combinations (useful when creating a FrameMap);
	//'paramList' is the List of Params of the API that the resulting list of FrameBeans refers to.
	public static ArrayList<DiscourseFrame> generateDiscourseFrames(HTTPMethod method, String endpoint, ArrayList<DiscourseParam> paramList, Double probSelection, Double probFailure, Double trueProbSelection, Double trueProbFailure){
		ArrayList<DiscourseFrame> framesList = new ArrayList<DiscourseFrame>();
		
		ArrayList<DiscourseTypeParam> types = new ArrayList<DiscourseTypeParam>();
		for(int k = 0; k<6; k++){
			DiscourseTypeParam t = null;
			if(k<paramList.size()){
				t = paramList.get(k).getTypeParam();
			}
			types.add(t);
		}
		
		List<List<String>> classesCombinations = DiscourseEquivalenceClass.cartesianProduct(types.get(0), types.get(1), types.get(2), types.get(3), types.get(4), types.get(5));
		
		for(int i = 0; i<classesCombinations.size(); i++){
			DiscourseFrame frame = new DiscourseFrame();
			ArrayList<DiscourseParam> frameParamList = new ArrayList<DiscourseParam>();
			frame.setMethod(method);
			frame.setEndpoint(endpoint);
			frame.setProbSelection(probSelection);
			frame.setProbFailure(probFailure);
			frame.setTrueProbSelection(trueProbSelection);
			frame.setTrueProbFailure(trueProbFailure);
			for(int j = 0; j<paramList.size(); j++){
				DiscourseParam p1 = new DiscourseParam(paramList.get(j));
				p1.setClassParam(DiscourseEquivalenceClass.valueOf(classesCombinations.get(i).get(j)));
				frameParamList.add(p1);
			}
			frame.setParamList(frameParamList);
			framesList.add(frame);
		}
		
		return framesList;
	}
	
	public String generateValue(ArrayList<String> validValues){
		String value = null;
		int nextInt = 0;
		long nextLong = 0L;
		String nextString = new String();
		Date date = null;
		StringBuilder stringBuilder = null;
		
		if(this != null){
			switch(this){
				case BOOLEAN_EMPTY:
					value = "";
					break;
				case BOOLEAN_INVALID:
					//Generate a random string of random length from 1 to MAX_LENGTH, different than "true" or "false"
					do{
						nextString = RandomStringUtils.random(RandomUtils.nextInt(1, MAX_LENGTH), true, true);
					} while(nextString.equals("true") || nextString.equals("false"));
					value = nextString;
					break;
				case BOOLEAN_VALID:
					value = String.valueOf(RandomUtils.nextBoolean());
					break;
				case COL_EMPTY:
					value="";
					break;
				case COL_INVALID:
					//Generate random string with random length from 1 to MAX_LENGTH, different than 6 to make the color invalid
					do{
						nextString = RandomStringUtils.random(RandomUtils.nextInt(1, MAX_LENGTH), true, true);
					} while(nextString.length() == 6);
					value = nextString;
					break;
				case COL_VALID:
					//Generate random hexadecimal number from 0x000000 to 0xFFFFFF and save it as a string with leading # and 0s
					nextInt = RandomUtils.nextInt(0, 0xffffff + 1);
					value = String.format("%06x", nextInt);
					break;
				case DATE_EMPTY:
					value="";
					break;
				case DATE_INVALID:
					//Generate a new Date from a random Long representing milliseconds from Jan 1, 1970.
					nextLong = RandomUtils.nextLong(0, 48L * 365 * 24 * 60 * 60 * 1000);
					date = new Date(nextLong);
					value = new SimpleDateFormat("yyyy-MM-dd").format(date);
					
					stringBuilder = new StringBuilder(value);
					
					/* Set the first month character to a random integer from 2 to 9 to make the date invalid;
					  for example, if the random generated date is 2017-10-10 and the random integer is 5,
					  the invalid date will be 2017-50-10 */
					stringBuilder.setCharAt(5, Character.forDigit(RandomUtils.nextInt(2, 10), 10));
					
					value = stringBuilder.toString();
					break;
				case DATE_VALID:
					//Generate a new Date from a random Long representing milliseconds from Jan 1, 1970.
					nextLong = RandomUtils.nextLong(0, 48L * 365 * 24 * 60 * 60 * 1000);
					date = new Date(nextLong);
					value = new SimpleDateFormat("yyyy-MM-dd").format(date);
					break;
				case EMAIL_EMPTY:
					value="";
					break;
				case EMAIL_INVALID:
					//Generate random string with random length from 1 to MAX_LENGTH that doesn't contain a '@'
					do{
						nextString = RandomStringUtils.random(RandomUtils.nextInt(1, MAX_LENGTH), true, true);
					} while(nextString.contains("@"));
					value = nextString;
					break;
				case EMAIL_VALID:
					//Generate random alphanumeric string in the form of x@y.z (where x, y and z are strings of random lengths) of total length up to 2^16
					String firstEmailPart = RandomStringUtils.randomAlphanumeric(1, RandomUtils.nextInt(1,MAX_LENGTH-4));
					String secondEmailPart = RandomStringUtils.randomAlphanumeric(1, (MAX_LENGTH-3-firstEmailPart.length()));
					String thirdEmailPart = RandomStringUtils.randomAlphanumeric(1, (MAX_LENGTH-2-firstEmailPart.length()-secondEmailPart.length()));
					value = firstEmailPart+"@"+secondEmailPart+"."+thirdEmailPart;
					break;
				case LIST_EMPTY:
					value="";
					break;
				case LIST_NULL:
					value="NULL";
					break;
				case LIST_VALID:
					//Generate a list of random length (from 1 to 10) made of random alphanumeric strings with random lengths (from 0 to 10)
					nextInt = RandomUtils.nextInt(1, 11);
					stringBuilder = new StringBuilder();
					for(int i = 0; i<nextInt; i++){
						stringBuilder.append(RandomStringUtils.randomAlphanumeric(1, 11));
						stringBuilder.append(",");
					}
					value = stringBuilder.toString();
					break;
				case NUM_ABSOLUTE_MINUS_ONE:
					value="-1";
					break;
				case NUM_ABSOLUTE_ZERO:
					value="0";
					break;
				case NUM_EMPTY:
					value="";
					break;
				case NUM_INVALID:
					//Generate random alphabetic string with length from 1 to MAX_LENGTH
					value = RandomStringUtils.randomAlphabetic(1, MAX_LENGTH);
					break;
				case NUM_VALID:
					//Generate random integer from Integer.MIN_VALUE to Integer.MAX_VALUE
					value = String.valueOf(String.valueOf(new Random().nextInt()));
					break;
				case NUM_VERY_BIG:
					//Generate a number equal to Integer.MAX_VALUE + r or Integer.MIN_VALUE - r, where "r" is a random number between 1 and Integer.MAX_VALUE
					BigInteger veryBigValue = null;
					nextString = String.valueOf(RandomUtils.nextInt(1, Integer.MAX_VALUE));
					boolean negative = RandomUtils.nextBoolean();
					if(negative){
						veryBigValue = new BigInteger(String.valueOf(Integer.MIN_VALUE)).subtract((new BigInteger(nextString)));
					}
					else{
						veryBigValue = new BigInteger(String.valueOf(Integer.MAX_VALUE)).add((new BigInteger(nextString)));
					}
					value = veryBigValue.toString();
					break;
				case STR_EMPTY:
					value="";
					break;
				case STR_INVALID:
					//Generate random string with length from 1 to MAX_LENGTH with non-printable characters (ASCII code from 0 to 31)
					value = RandomStringUtils.random(RandomUtils.nextInt(1, MAX_LENGTH), 0, 31, false, false);
					break;
				case STR_NULL:
					value="NULL";
					break;
				case STR_VALID:
					//Generate random alphanumeric string as a random UUID
					value = UUID.randomUUID().toString();
					break;
				case STR_VERY_LONG:
//					//Generate random alphanumeric string with length from 2^16 to r, where "r" is a random number between 1 and 2^16-1
//					nextInt = 65536 + RandomUtils.nextInt(1, 65536);
//					value = RandomStringUtils.randomAlphanumeric(65536, nextInt);
					
					//Generate random alphanumeric string with length=MAX_LENGTH
					value = RandomStringUtils.randomAlphanumeric(MAX_LENGTH, MAX_LENGTH+1);
					break;
				case ENUM_EMPTY:
					value="";
					break;
				case ENUM_INVALID:
					//Generate a random string of random length from 1 to MAX_LENGTH, different than any of the valid values
					do{
						nextString = RandomStringUtils.random(RandomUtils.nextInt(1, MAX_LENGTH), true, true);
					} while(validValues.contains(nextString));
					value = nextString;
					break;
				case ENUM_VALID:
					//Pick one of the valid values
					nextInt = RandomUtils.nextInt(0, validValues.size());
					value = validValues.get(nextInt);
					break;
				default:
					break;
			
			}
		}
		return value;
		
	}
}
