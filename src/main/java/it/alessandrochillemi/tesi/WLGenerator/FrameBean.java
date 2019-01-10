package it.alessandrochillemi.tesi.WLGenerator;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

//Classe "bean" che modella i campi di un TestFrame che devono essere letti o scritti
public class FrameBean implements Serializable{
	
	private Integer ID;												//ID del TestFrame
	private HTTPMethod method;										//Metodo della richiesta HTTP per usare l'API
	private String endpoint;											//Endpoint dell'API
	private Integer paramsNumber;									//Numero di parametri
	private String keyParam1;										//Key (ovvero il nome) del parametro 1
	private EquivalenceClass classParam1;							//Classe di equivalenza del parametro 1
	private String keyParam2;										//Key (ovvero il nome) del parametro 2
	private EquivalenceClass classParam2;							//Classe di equivalenza del parametro 2
	private String keyParam3;										//Key (ovvero il nome) del parametro 3
	private EquivalenceClass classParam3;							//Classe di equivalenza del parametro 3
	private String keyParam4;										//Key (ovvero il nome) del parametro 4
	private EquivalenceClass classParam4;							//Classe di equivalenza del parametro 4
	private String keyParam5;										//Key (ovvero il nome) del parametro 5
	private EquivalenceClass classParam5;							//Classe di equivalenza del parametro 5
	private String keyParam6;										//Key (ovvero il nome) del parametro 6
	private EquivalenceClass classParam6;							//Classe di equivalenza del parametro 6
	private Double probSelection;									//Probabilità di selezione del TestFrame
	private Double probFailure;										//Probabilità di fallimento del TestFrame
	
	private static final AtomicInteger count = new AtomicInteger(0);	//Contatore degli ID condiviso da tutti i TestFrame
	
	private static final long serialVersionUID = 5259280897255194440L;
	
	public FrameBean(){
		this.ID = count.incrementAndGet();
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public HTTPMethod getMethod() {
		return method;
	}

	public void setMethod(HTTPMethod method) {
		this.method = method;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public Integer getParamsNumber() {
		return paramsNumber;
	}

	public void setParamsNumber(Integer paramsNumber) {
		this.paramsNumber = paramsNumber;
	}

	public String getKeyParam1() {
		return keyParam1;
	}

	public void setKeyParam1(String keyParam1) {
		this.keyParam1 = keyParam1;
	}

	public EquivalenceClass getClassParam1() {
		return classParam1;
	}

	public void setClassParam1(EquivalenceClass classParam1) {
		this.classParam1 = classParam1;
	}

	public String getKeyParam2() {
		return keyParam2;
	}

	public void setKeyParam2(String keyParam2) {
		this.keyParam2 = keyParam2;
	}

	public EquivalenceClass getClassParam2() {
		return classParam2;
	}

	public void setClassParam2(EquivalenceClass classParam2) {
		this.classParam2 = classParam2;
	}

	public String getKeyParam3() {
		return keyParam3;
	}

	public void setKeyParam3(String keyParam3) {
		this.keyParam3 = keyParam3;
	}

	public EquivalenceClass getClassParam3() {
		return classParam3;
	}

	public void setClassParam3(EquivalenceClass classParam3) {
		this.classParam3 = classParam3;
	}

	public String getKeyParam4() {
		return keyParam4;
	}

	public void setKeyParam4(String keyParam4) {
		this.keyParam4 = keyParam4;
	}

	public EquivalenceClass getClassParam4() {
		return classParam4;
	}

	public void setClassParam4(EquivalenceClass classParam4) {
		this.classParam4 = classParam4;
	}

	public String getKeyParam5() {
		return keyParam5;
	}

	public void setKeyParam5(String keyParam5) {
		this.keyParam5 = keyParam5;
	}

	public EquivalenceClass getClassParam5() {
		return classParam5;
	}

	public void setClassParam5(EquivalenceClass classParam5) {
		this.classParam5 = classParam5;
	}

	public String getKeyParam6() {
		return keyParam6;
	}

	public void setKeyParam6(String keyParam6) {
		this.keyParam6 = keyParam6;
	}

	public EquivalenceClass getClassParam6() {
		return classParam6;
	}

	public void setClassParam6(EquivalenceClass classParam6) {
		this.classParam6 = classParam6;
	}

	public Double getProbSelection() {
		return probSelection;
	}

	public void setProbSelection(Double probSelection) {
		this.probSelection = probSelection;
	}

	public Double getProbFailure() {
		return probFailure;
	}

	public void setProbFailure(Double probFailure) {
		this.probFailure = probFailure;
	}
	

}
