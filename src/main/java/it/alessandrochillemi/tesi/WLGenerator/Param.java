package it.alessandrochillemi.tesi.WLGenerator;

import java.io.Serializable;

public class Param implements Serializable{
	private String keyParam;												//Nome del parametro
	private EquivalenceClass classParam;									//Classe di equivalenza del parametro
	
	private static final long serialVersionUID = 1L;
	
	public Param(){
		
	}
	
	public Param(String keyParam, EquivalenceClass classParam){
		this.keyParam = keyParam;
		this.classParam = classParam;
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
	
}
