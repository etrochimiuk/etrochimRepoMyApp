package pl.ewelinatrochimiuk.domain;

public class SearchParameters {

	private String DBname="";
	private String name="";
	private String email="";
	private String parentMassTolerance=""; //done
	private String parentMassToleranceUnit=""; //done
	private String fragmentationMethod=""; //done
	private String instrument="";
	private String enzyme="";
	private String modification=""; 
	private String taxonomy="";
	private String semispecific="";
	private String[] fixedModifications={}; // done
	private String[] variousModification={}; // done
	
	
	
	public String getParentMassTolerance() {
		return parentMassTolerance;
	}
	public void setParentMassTolerance(String parentMassTolerance) {
		this.parentMassTolerance = parentMassTolerance;
	}
	public String getParentMassToleranceUnit() {
		return parentMassToleranceUnit;
	}
	public void setParentMassToleranceUnit(String parentMassToleranceUnit) {
		this.parentMassToleranceUnit = parentMassToleranceUnit;
	}
	public String getFragmentationMethod() {
		return fragmentationMethod;
	}
	public void setFragmentationMethod(String fragmentationMethod) {
		this.fragmentationMethod = fragmentationMethod;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getEnzyme() {
		return enzyme;
	}
	public void setEnzyme(String enzyme) {
		this.enzyme = enzyme;
	}
	public String getModification() {
		return modification;
	}
	public void setModification(String modification) {
		this.modification = modification;
	}
	public String getTaxonomy() {
		return taxonomy;
	}
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}
	public String getSemispecific() {
		return semispecific;
	}
	public void setSemispecific(String semispecific) {
		this.semispecific = semispecific;
	}
	
	public String[] getFixedModifications() {
		return fixedModifications;
	}
	public void setFixedModifications(String[] fixedModifications) {
		this.fixedModifications = fixedModifications;
	}
	public String[] getVariousModification() {
		return variousModification;
	}
	public void setVariousModification(String[] variousModification) {
		this.variousModification = variousModification;
	}
	public String getDBname() {
		return DBname;
	}
	public void setDBname(String dBname) {
		DBname = dBname;
	}
	
	
	
	
	
}
