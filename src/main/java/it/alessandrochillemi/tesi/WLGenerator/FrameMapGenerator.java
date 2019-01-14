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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

//Classe che serve a generare la lista con tutti i Frame
public class FrameMapGenerator {
	
	//Percorso nel quale si trova il file con le variabili di ambiente
	private static final String ENVIRONMENT_PATH = "/Users/alessandrochillemi/Desktop/Universita/Magistrale/Tesi/environment.properties";
	
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
		String endpoint = "/groups/{group_id}/members.json";
		Integer paramNumber = 2;
		List<List<String>> classesCombinations = EquivalenceClass.cartesianProduct(TypeParam.NUMBER,TypeParam.NUMBER,null,null,null,null);
		ArrayList<String> keysParam = new ArrayList<String>();
		keysParam.add("{group_id}");
		keysParam.add("user_id");
//		keysParam.add("filter");
//		keysParam.add("page");
//		keysParam.add("show_emails");
//		keysParam.add("approved");
		Double probSelection = 1.0/17648;
		Double probFailure = 0.0 + new Random().nextDouble() * (1.0 - 0.0);
		
//		List<FrameBean> frameBeansList = FrameBean.generateFrameBeans(method,endpoint,paramNumber,keysParam,classesCombinations,probSelection,probFailure);
		
		System.out.println("ok");
				
		//TODO: modificare GET /posts.json con path parameter?
		//TODO: nella generazione dei valori, ricorda di mettere un: if(key=="slug") then value="-"
		//TODO: il parametro "status" in PUT /t/{id}/status ammette un insieme finito di valori. Come gestirlo?
		//TODO: il parametro "order" in GET /latest.json ammette un insieme finito di valori. Come gestirlo?
		//TODO: il parametro "flag" in GET /top/{flag}.json ammette un insieme finito di valori. Come gestirlo?
		//TODO: il parametro "status_type" in POST /t/{id}/status_update ammette un insieme finito di valori (non specificato)? Come gestirlo?
		//TODO: il parametro "notification_level" in POST /t/{id}/notifications ammette un insieme finito di valori. Come gestirlo?
		//TODO: l'API POST /posts.json (per la creazione di un messaggio privato) richiede l'attributo archetype="private_message"
		//TODO: il parametro "type" in PUT /users/{username}/preferences/avatar/pick ammette un insieme finito di valori (non specificato)? Come gestirlo?
		//TODO: il parametro "period" e il parametro "order" in GET /directory_items.json?period={period}&order={order} ammettono un insieme finito di valori (non specificato)? Come gestirlo?
		//TODO: il parametro "{flag}" e il parametro "order" in GET /admin/users/list/{flag}.json ammettono un insieme finito di valori (non specificato)? Come gestirlo?
	
//		appendToFramesMap(frameBeansList,frameMapPath);
		
		printFramesMap(frameMapPath);
		
		System.out.println("ok");
		
	}
	
	//Genera la lista di tutte le possibili combinazioni di classi di equivalenza tra i gruppi selezionati;
	//i gruppi di classi di equivalenza sono selezionati automaticamente in base al tipo specificato in ingresso
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
	
	private static void deleteAPIFromFramesMap(String path, String endpoint){
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
		
		Iterator<Map.Entry<Integer, FrameBean>> iter = oldMap.entrySet().iterator();
		while (iter.hasNext()) {
		    Entry<Integer, FrameBean> entry = iter.next();
		    if(entry.getValue().getEndpoint().equals(endpoint)){
		        iter.remove();
		    }
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
	
	@SuppressWarnings("unchecked")
	private static void printFramesMap(String path){
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
		
		if(oldMap != null){
			for(Entry<Integer, FrameBean> entry : oldMap.entrySet()){
				System.out.print(entry.getKey() + " ");
				entry.getValue().print();
				System.out.print("\n");
			}
		}
	}
	
}
