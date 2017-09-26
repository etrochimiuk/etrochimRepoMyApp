package pl.ewelinatrochimuk.myapp;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import junit.framework.Assert;
import mscanlib.common.MScanException;
import mscanlib.ms.mass.MassTools;
import mscanlib.ms.mass.PTMMap;
import pl.ewelinatrochimiuk.domain.SearchParameters;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan("dao")
@EnableJpaRepositories("dao")
public class MyappApplicationTests {

	@Autowired
	SearchService service;
	
	@Test
	 public void testPrepareParameters(){
		SearchParameters params = new SearchParameters();
		
		try {
			MassTools.initMaps();
			System.out.println("Maps initalized");	
		} catch (MScanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			service.testDB();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("COS POSZLO NIE TAK!");
		} 
		//service.readDB();
		
		
		String names[]=PTMMap.getPTMNames(true,true);
		params.setEnzyme("1");
		params.setFragmentationMethod("0");
		params.setDBname("SwissProt");
		params.setTaxonomy("Ludzik");
		//params.setInstrument("1");
		//params.setParentMassTolerance("20");
		params.setParentMassToleranceUnit("ppm");
		System.out.println("MODYFIKACJA: " + names[0]);
		String [] mods = {names[0],names[1]};
		String [] mods2 = {names[5],names[6]};
		params.setFixedModifications(mods);
		params.setVariousModification(mods2);
		service.prepareParameters(params);
		
		Assert.assertTrue(service.executeSearch()); 
		
	}

}
