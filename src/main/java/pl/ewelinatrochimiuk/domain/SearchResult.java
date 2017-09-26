package pl.ewelinatrochimiuk.domain;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

	private List<Protein> proteins = new ArrayList<Protein>();
	
	public void addProtein(Protein protein){
		
		proteins.add(protein);
		
	}
	
	public void deleteAllProteins(){
		
		proteins.clear();
		
	}
	
	public List<Protein> getProteins(){
		
		return proteins;
		
	}
	
}
