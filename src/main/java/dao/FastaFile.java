package dao;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "FastaFiles")
public class FastaFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	private String dBname;
	private String File_name;
	private int DB_id;
	private String taxonomy;

	
	@Lob
	private byte[] File;


	
	protected FastaFile(){
		
	}
	
	
	
	public FastaFile(String DB, String File_name, int DB_id, String taxonomy){
		this.setDBname(DB);
		this.setFile_name(File_name);
		this.setDB_id(DB_id);
		this.setTaxonomy(taxonomy);
	
	}
	

	public String getDBname() {
		return dBname;
	}

	public void setDBname(String dB) {
		dBname = dB;
	}

	public String getFile_name() {
		return File_name;
	}

	public void setFile_name(String file_name) {
		File_name = file_name;
	}

	public int getDB_id() {
		return DB_id;
	}

	public void setDB_id(int dB_id) {
		DB_id = dB_id;
	}

	public byte[] getFile() {
		return File;
	}

	public void setFile(byte[] file) {
		File = file;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}



}
