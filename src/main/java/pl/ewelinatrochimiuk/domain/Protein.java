package pl.ewelinatrochimiuk.domain;

import java.util.ArrayList;
import java.util.List;

public class Protein {
	
	private List<Peptide> peptides = new ArrayList<Peptide>();
	private String score;
	private String ID;
	private String name;
	private String peptidesCount;
	
	public void addPeptide(Peptide peptide){
		
		peptides.add(peptide);
		
	}
	
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPeptidesCount() {
		return peptidesCount;
	}
	public void setPeptidesCount(String peptidesCount) {
		this.peptidesCount = peptidesCount;
	}
	
	
	
	

}
