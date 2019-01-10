package it.alessandrochillemi.tesi.WLGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

//Classe che serve a generare la lista con tutti i Frame
public class FrameListGenerator {
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	private static final String ENVIRONMENT_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
	private enum TypeParam {
		STRING,COLOR,DATE,NUMBER,LIST,BOOLEAN
	}
	
	private static String[] stringEquivalenceClasses = new String[] {"STR_NULL","STR_EMPTY","STR_VERY_LONG","STR_INVALID","STR_VALID"};
	private static String[] colorEquivalenceClasses = new String[] {"COL_NULL","COL_EMPTY","COL_VERY_LONG","COL_INVALID","COL_VALID"};
	private static String[] dateEquivalenceClasses = new String[] {"DATE_NULL","DATE_EMPTY","DATE_VERY_LONG","DATE_INVALID","DATE_VALID"};
	private static String[] numberEquivalenceClasses = new String[] {"NUM_NULL","NUM_EMPTY","NUM_ABSOLUTE_MINUS_ONE","NUM_ABSOLUTE_ZERO","NUM_VERY_BIG","NUM_VALID"};
	private static String[] listEquivalenceClasses = new String[] {"LIST_NULL","LIST_EMPTY","LIST_VALID"};
	private static String[] booleanEquivalenceClasses = new String[] {"BOOLEAN_NULL","BOOLEAN_EMPTY","BOOLEAN_VALID"};

	public static void main(String[] args) {
		
		//Leggo le variabili d'ambiente
		Properties environment = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(ENVIRONMENT_PATH);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			environment.load(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Leggo le variabili d'ambiente
		String frameMapPath = environment.getProperty("frame_map_path");
		
		HTTPMethod method = HTTPMethod.DELETE;
		String endpoint = "/categories/{id}";
		Integer paramNumber = 1;
		List<List<String>> classesCombinations = cartesianProduct(TypeParam.NUMBER,null,null,null,null,null);
		ArrayList<String> keysParam = new ArrayList<String>();
		keysParam.add("{id}");
//		keysParam.add("name");
//		keysParam.add("color");
//		keysParam.add("text_color");
		Double probSelection = 1.0/17348;
		Double probFailure = 0.0 + new Random().nextDouble() * (1.0 - 0.0);
		
		List<FrameBean> frameBeansList = generateFrameBeans(method,endpoint,paramNumber,keysParam,classesCombinations,probSelection,probFailure);
		
		System.out.println("ok");
		
		//PROSSIMA API: GET /posts.json
		
		appendToFramesMap(frameBeansList,frameMapPath);
		
		TreeMap<Integer, FrameBean> oldMap = null;
		if(Files.exists(Paths.get(frameMapPath))) { 
			FileInputStream fis;
			try {
				fis = new FileInputStream(frameMapPath);
				ObjectInputStream ois = new ObjectInputStream(fis);
				oldMap = (TreeMap<Integer, FrameBean>) ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(Entry<Integer, FrameBean> entry : oldMap.entrySet()){
			System.out.print(entry.getKey() + " ");
			entry.getValue().print();
			System.out.print("\n");
		}
		
		System.out.println("ok");
		
	}
	
	private static List<FrameBean> generateFrameBeans(HTTPMethod method, String endpoint, Integer paramNumber, List<String> keysParam, List<List<String>> classesCombinations, Double probSelection, Double probFailure){
		List<FrameBean> frameBeansList = new ArrayList<FrameBean>();
		
		for(int i = 0; i<classesCombinations.size(); i++){
			FrameBean frameBean = new FrameBean();
			frameBean.setMethod(method);
			frameBean.setEndpoint(endpoint);
			frameBean.setParamNumber(paramNumber);
			frameBean.setProbSelection(probSelection);
			frameBean.setProbFailure(probFailure);
			for(int j = 0; j<classesCombinations.get(i).size(); j++){
				Param param = new Param(keysParam.get(j),EquivalenceClass.valueOf(classesCombinations.get(i).get(j)));
				frameBean.addParam(param);
			}
			frameBeansList.add(frameBean);
		}

		return frameBeansList;
		
	}
	
	//Genera la lista di tutte le possibili combinazioni tra gruppi di stringhe (ovvero, in questo caso,
	//le classi di equivalenza per ogni tipo); seleziona automaticamente i gruppi in base al tipo specificato in ingresso
	//per ogni parametro; viene usata la libreria Google Guava in combinazione con Java 8, prendendo spunto dalla risposta
	//seguente indirizzo: https://stackoverflow.com/a/37490796/5863657
	private static List<List<String>> cartesianProduct(TypeParam typeParam1, TypeParam typeParam2, TypeParam typeParam3, TypeParam typeParam4, TypeParam typeParam5, TypeParam typeParam6){
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
	
	@SuppressWarnings("unchecked")
	private static void appendToFramesMap(List<FrameBean> list, String path){
		
		TreeMap<Integer, FrameBean> oldMap = null;
		if(Files.exists(Paths.get(path))) { 
			FileInputStream fis;
			try {
				fis = new FileInputStream(path);
				ObjectInputStream ois = new ObjectInputStream(fis);
				oldMap = (TreeMap<Integer, FrameBean>) ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(oldMap == null){
			oldMap = new TreeMap<Integer,FrameBean>();
		}
		
		for(FrameBean frameBean : list){
			oldMap.put(oldMap.isEmpty() ? 1 : oldMap.lastKey()+1, frameBean);
		}
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(oldMap);
	        oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
