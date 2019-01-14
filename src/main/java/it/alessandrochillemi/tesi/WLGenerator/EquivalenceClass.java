package it.alessandrochillemi.tesi.WLGenerator;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

//Possibili classi di equivalenza per ogni parametro
public enum EquivalenceClass {
	STR_NULL,STR_EMPTY,STR_VERY_LONG,STR_INVALID,STR_VALID,
	COL_NULL,COL_EMPTY,COL_VERY_LONG,COL_INVALID,COL_VALID,
	DATE_NULL,DATE_EMPTY,DATE_VERY_LONG,DATE_INVALID,DATE_VALID,
	NUM_NULL,NUM_EMPTY,NUM_ABSOLUTE_MINUS_ONE,NUM_ABSOLUTE_ZERO,NUM_VERY_BIG,NUM_VALID,
	LIST_NULL,LIST_EMPTY,LIST_VALID,
	BOOLEAN_NULL,BOOLEAN_EMPTY,BOOLEAN_VALID,
	CONSTANT;

	private static String[] stringEquivalenceClasses = new String[] {"STR_NULL","STR_EMPTY","STR_VERY_LONG","STR_INVALID","STR_VALID"};
	private static String[] colorEquivalenceClasses = new String[] {"COL_NULL","COL_EMPTY","COL_VERY_LONG","COL_INVALID","COL_VALID"};
	private static String[] dateEquivalenceClasses = new String[] {"DATE_NULL","DATE_EMPTY","DATE_VERY_LONG","DATE_INVALID","DATE_VALID"};
	private static String[] numberEquivalenceClasses = new String[] {"NUM_NULL","NUM_EMPTY","NUM_ABSOLUTE_MINUS_ONE","NUM_ABSOLUTE_ZERO","NUM_VERY_BIG","NUM_VALID"};
	private static String[] listEquivalenceClasses = new String[] {"LIST_NULL","LIST_EMPTY","LIST_VALID"};
	private static String[] booleanEquivalenceClasses = new String[] {"BOOLEAN_NULL","BOOLEAN_EMPTY","BOOLEAN_VALID"};
	private static String[] constantEquivalenceClasses = new String[] {"CONSTANT"};

	//Genera la lista di tutte le possibili combinazioni di classi di equivalenza tra i gruppi selezionati;
	//i gruppi di classi di equivalenza sono selezionati automaticamente in base al tipo specificato in ingresso
	//per ogni parametro; viene usata la libreria Google Guava in combinazione con Java 8, prendendo spunto dalla risposta
	//seguente indirizzo: https://stackoverflow.com/a/37490796/5863657
	public static List<List<String>> cartesianProduct(TypeParam typeParam1, TypeParam typeParam2, TypeParam typeParam3, TypeParam typeParam4, TypeParam typeParam5, TypeParam typeParam6){
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
			case NUMBER:
				elements.add(numberEquivalenceClasses);
				break;
			case LIST:
				elements.add(listEquivalenceClasses);
				break;
			case BOOLEAN:
				elements.add(booleanEquivalenceClasses);
				break;
			case CONSTANT:
				elements.add(constantEquivalenceClasses);
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
				case NUMBER:
					elements.add(numberEquivalenceClasses);
					break;
				case LIST:
					elements.add(listEquivalenceClasses);
					break;
				case BOOLEAN:
					elements.add(booleanEquivalenceClasses);
					break;
				case CONSTANT:
					elements.add(constantEquivalenceClasses);
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
					case NUMBER:
						elements.add(numberEquivalenceClasses);
						break;
					case LIST:
						elements.add(listEquivalenceClasses);
						break;
					case BOOLEAN:
						elements.add(booleanEquivalenceClasses);
						break;
					case CONSTANT:
						elements.add(constantEquivalenceClasses);
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
						case NUMBER:
							elements.add(numberEquivalenceClasses);
							break;
						case LIST:
							elements.add(listEquivalenceClasses);
							break;
						case BOOLEAN:
							elements.add(booleanEquivalenceClasses);
							break;
						case CONSTANT:
							elements.add(constantEquivalenceClasses);
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
							case NUMBER:
								elements.add(numberEquivalenceClasses);
								break;
							case LIST:
								elements.add(listEquivalenceClasses);
								break;
							case BOOLEAN:
								elements.add(booleanEquivalenceClasses);
								break;
							case CONSTANT:
								elements.add(constantEquivalenceClasses);
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
								case NUMBER:
									elements.add(numberEquivalenceClasses);
									break;
								case LIST:
									elements.add(listEquivalenceClasses);
									break;
								case BOOLEAN:
									elements.add(booleanEquivalenceClasses);
									break;
								case CONSTANT:
									elements.add(constantEquivalenceClasses);
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
}
