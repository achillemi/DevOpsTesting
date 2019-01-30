package it.alessandrochillemi.tesi.FrameUtils;

public interface Oracle{
	
	//Determina se una risposta è un fallimento o meno in base alla sua validità (presenza o meno di parametri con classi di equivalenza non valide) e al response code
	public boolean isFailure(boolean valid, int responseCode);
	
	//Determina se una risposta è un fallimento critico o meno in base alla sua validità (presenza o meno di parametri con classi di equivalenza non valide) e al response code
	public boolean isCriticalFailure(boolean valid, int responseCode);

}
