package pl.ewelinatrochimiuk.domain;

public class Spectrum {

	private String nr;
	private String mz;
	private String charge;
	private String mass;
	private String massDifference;
	private String score;

	
	
	
	public String getNr() {
		return nr;
	}
	public void setNr(String nr) {
		this.nr = nr;
	}
	public String getMz() {
		return mz;
	}
	public void setMz(String mz) {
		this.mz = mz;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getMass() {
		return mass;
	}
	public void setMass(String mass) {
		this.mass = mass;
	}
	public String getMassDifference() {
		return massDifference;
	}
	public void setMassDifference(String massDifference) {
		this.massDifference = massDifference;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
}
