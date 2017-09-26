package pl.ewelinatrochimiuk.domain;

import java.util.ArrayList;
import java.util.List;

public class Peptide {

	private List<Spectrum> spectrum = new ArrayList<Spectrum>();
	private String sequence;
	private String mass;
	private String queriesCount;
	
	public void addSpectrum(Spectrum s){
		
		spectrum.add(s);
		
		
	}
	
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getMass() {
		return mass;
	}
	public void setMass(String mass) {
		this.mass = mass;
	}
	public String getQueriesCount() {
		return queriesCount;
	}
	public void setQueriesCount(String queriesCount) {
		this.queriesCount = queriesCount;
	}
	
	
	
}
