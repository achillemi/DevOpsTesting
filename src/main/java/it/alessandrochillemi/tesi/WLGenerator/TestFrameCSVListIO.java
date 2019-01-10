package it.alessandrochillemi.tesi.WLGenerator;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

//Questa classe serve a fare operazioni di I/O su una lista CSV di TestFrame, sfruttando la libreria SuperCSV
public class TestFrameCSVListIO {
	
	//Imposto le preferenze per la lettura/scrittura dei CSV in modo che gli spazi che non sono inclusi
	//tra virgolette, all'inizio e alla fine di una cella, vengano ignorati
	private static final CsvPreference STANDARD_SURROUNDING_SPACES_NEED_QUOTES = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).surroundingSpacesNeedQuotes(true).build();

	//Specifico come deve essere processato ogni campo del CSV, ovvero il tipo dei vari campi; l'ordine dei processor
	//deve rispettare quello del CSV; un processor "null" indica che non è necessario alcun processing
	private static final CellProcessor[] PROCESSORS = new CellProcessor[] {
			new ParseInt(),												//ID
			new ParseEnum(HTTPMethod.class, true),						//method
			null,														//endpoint
			new ParseInt(),												//paramsNumber
			new Optional(),												//keyParam1
			new Optional(new ParseEnum(EquivalenceClass.class, true)),	//classParam1
			new Optional(),												//keyParam2
			new Optional(new ParseEnum(EquivalenceClass.class, true)),	//classParam2
			new Optional(),												//keyParam3
			new Optional(new ParseEnum(EquivalenceClass.class, true)),	//classParam3
			new Optional(),												//keyParam4
			new Optional(new ParseEnum(EquivalenceClass.class, true)),	//classParam4
			new Optional(),												//keyParam5
			new Optional(new ParseEnum(EquivalenceClass.class, true)),	//classParam5
			new Optional(),												//keyParam6
			new Optional(new ParseEnum(EquivalenceClass.class, true)),	//classParam6
			new ParseDouble(),											//probSelection
			new ParseDouble(),											//probFailure		
	};
	
	private 	final static String[] header = new String[] {"ID","method","endpoint","paramsNumber","keyParam1","classParam1","keyParam2","classParam2","keyParam3","classParam3","keyParam4","classParam4","keyParam5","classParam5","keyParam6","classParam6","probSelection","probFailure"};
	
	public static ArrayList<FrameBean> readListFromCSV(String csvPath){
		ICsvBeanReader beanReader = null;
		FrameBean testFrameBean = null;
		ArrayList<FrameBean> ret = new ArrayList<FrameBean>();
		try {
			beanReader = new CsvBeanReader(new FileReader(csvPath), STANDARD_SURROUNDING_SPACES_NEED_QUOTES);
			
			beanReader.getHeader(true);
			
			while((testFrameBean = beanReader.read(FrameBean.class, header,PROCESSORS)) != null){
				ret.add(testFrameBean);
			}
			
			if(beanReader != null){
				beanReader.close();
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static FrameBean readRowFromCSV(String csvPath, int rowNumber){
		ICsvBeanReader beanReader = null;
		FrameBean ret = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(csvPath), STANDARD_SURROUNDING_SPACES_NEED_QUOTES);
			
			beanReader.getHeader(true);
			
			for(int i = 0; i<rowNumber; i++){
				ret = beanReader.read(FrameBean.class, header,PROCESSORS);
			}
			
			if(beanReader != null){
				beanReader.close();
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static void writeRowToCSV(FrameBean testFrameBean, String csvPath, boolean append, boolean writeHeader){
		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(new FileWriter(csvPath, append), STANDARD_SURROUNDING_SPACES_NEED_QUOTES);
			
			if(writeHeader){
				beanWriter.writeHeader(header);
			}
			
			beanWriter.write(testFrameBean, header, PROCESSORS);
			
			if(beanWriter != null){
				beanWriter.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	public static int getCSVLength(String csvPath){
		int count = 0;
		for(FrameBean testFrameBean : readListFromCSV(csvPath)){
			count++;
		}
		return count;
	}

}
