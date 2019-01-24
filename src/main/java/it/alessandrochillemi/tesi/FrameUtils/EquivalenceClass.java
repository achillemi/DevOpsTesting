package it.alessandrochillemi.tesi.FrameUtils;

import java.util.ArrayList;

public interface EquivalenceClass {
	public String generateValue(ArrayList<String> validValues);
	public boolean isInvalid();
	public boolean isEmpty();
}
