package dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FastaFileRepository extends CrudRepository<FastaFile, Long>{

	FastaFile findByTaxonomyAndDBname(String taxonomy, String DBname);
	
	
}
