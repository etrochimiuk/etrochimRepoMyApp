package pl.ewelinatrochimuk.myapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import dao.FastaFile;
import dao.FastaFileRepository;
//import dao.FastaFileRepository;
import mscanlib.common.MScanException;
import mscanlib.ms.mass.ElementMap;
import mscanlib.ms.mass.MassTools;
import mscanlib.ms.mass.PTM;
import mscanlib.ms.mass.PTMTools;
import mscanlib.ms.msms.MsMsPeptideHit;
import mscanlib.ms.msms.MsMsProteinHit;
import mscanlib.ms.msms.MsMsQuery;
import mscanlib.ms.msms.dbengines.DbEngineScoring;
import mscanlib.ms.msms.io.MsMsFile;
import mscanlib.ms.msms.io.MsMsFileHeader;
import mscanlib.ms.msms.io.MsMsScanConfig;
import mscanlib.ms.msms.io.mzidentml.MzIdentFileReader;
import mscanlib.system.MScanSystemTools;
import pl.ewelinatrochimiuk.domain.Peptide;
import pl.ewelinatrochimiuk.domain.Protein;
import pl.ewelinatrochimiuk.domain.SearchParameters;
import pl.ewelinatrochimiuk.domain.SearchResult;
import pl.ewelinatrochimiuk.domain.Spectrum;

@Service
public class SearchService {
	
	private String mInFilename=null;	//nazwa pliku wejsciowego
	private String mOutFilename=null;	//nazwa pliku wyjsciowego
	private String mParams=null;		//parametry wywolania
	private String mDbPath=null;		//sciezka do pliku FASTA z sekwencjami bialek
	private String mMsGfCommand=null;	//
	
	@Autowired
	private FastaFileRepository fastaFileRepository;
	
	
	@Autowired
	private Environment environment;
	
	
	public byte[] readImageOldWay(File file) throws IOException
	{
	  Logger.getLogger(Main.class.getName()).log(Level.INFO, "[Open File] " + file.getAbsolutePath());
	  InputStream is = new FileInputStream(file);
	  // Get the size of the file
	  long length = file.length();
	  // You cannot create an array using a long type.
	  // It needs to be an int type.
	  // Before converting to an int type, check
	  // to ensure that file is not larger than Integer.MAX_VALUE.
	  if (length > Integer.MAX_VALUE)
	  {
	    // File is too large
	  }
	  // Create the byte array to hold the data
	  byte[] bytes = new byte[(int) length];
	  // Read in the bytes
	  int offset = 0;
	  int numRead = 0;
	  while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
	  {
	    offset += numRead;
	  }
	  // Ensure all the bytes have been read in
	  if (offset < bytes.length)
	  {
	    throw new IOException("Could not completely read file " + file.getName());
	  }
	  // Close the input stream and return bytes
	  is.close();
	  return bytes;
	}
	
	
	private FastaFile searchFastaFile(String taxonomy, String DBname){
		
		return fastaFileRepository.findByTaxonomyAndDBname(taxonomy, DBname);
		
	}
	
	
	
	
	public void writeFile(File file, byte[] data) throws IOException
	 {
	   OutputStream fo = new FileOutputStream(file);
	   // Write the data
	   fo.write(data);
	   // flush the file (down the toilet)
	   fo.flush();
	   // Close the door to keep the smell in.
	   fo.close();
	 }
	
	
	public void readDB(String taxonomy, String DBname){
		
		Long ile = fastaFileRepository.count();
		//FastaFile newFastaFile = fastaFileRepository.findOne((long) 1);
		FastaFile newFastaFile = searchFastaFile(taxonomy, DBname);
		
		File outfile = new File(environment.getProperty("Fasta"));
		try {
			writeFile(outfile,newFastaFile.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("ILE RZECZY: " + ile);
		
	}
	
	public void testDB() throws IOException{
		
		
		FastaFile newFastaFile = new FastaFile("SwissProt","Human",1,"Ludzik");
		File file = new File("C:\\Users\\Ewelinka\\Desktop\\Inzynierka\\apka\\uniprot_sprot_Homo_Sapiens_rev.fasta");
	
		try
		{
		  // Lets open an image file
			newFastaFile.setFile(readImageOldWay(file));
		}
		catch (IOException ex)
		{
		  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		String name = fastaFileRepository.save(newFastaFile).getDBname();
		
		System.out.println("ID NOWEGO PLIKU TO: " + name); 
	}
	
	
	
	
	
	public boolean executeSearch(){
		
		String msgfPath = environment.getProperty("MSGF");
		this.mMsGfCommand="java -Xmx" + "4000M" + " -jar " + msgfPath;
		//this.mDbPath= "C:\\Users\\Ewelinka\\Desktop\\Inzynierka\\apka\\uniprot_sprot_Homo_Sapiens_rev.fasta";
		this.mDbPath = environment.getProperty("Fasta");
		this.mInFilename = "C:\\Users\\Ewelinka\\Desktop\\Inzynierka\\apka\\dane.mgf";
		this.mOutFilename = MScanSystemTools.replaceExtension(this.mInFilename,"mzid");
		
		try {
			Process	process = Runtime.getRuntime().exec(this.mMsGfCommand + " -s " + this.mInFilename + " -o " + this.mOutFilename + " -d " + this.mDbPath + " " + this.mParams);
		
			StreamGobbler stderrGobbler=new StreamGobbler(process.getErrorStream(),"\tstderr",true);
			StreamGobbler stdoutGobbler=new StreamGobbler(process.getInputStream(),"\tstdout",true);

			stderrGobbler.start();
			stdoutGobbler.start();
			
			stderrGobbler.join();
			stdoutGobbler.join();
			
		}
		catch (SecurityException se)
		{
			se.printStackTrace();
			return false;
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
			return false;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
		
		if(this.readResult()){
			
			System.out.println("wYWOLUJE FUNKCJE PREPARERESULT");
			prepareSearchResult();
			return true;
			
		}else {
			System.out.println("NIE WYWOWALEM FUNKCJI PREPARERESULT"); 
			return false;
		}
		
	}
	
	
	private String getMsgfPtmString(PTM ptm,boolean fixed)
	{
		StringBuffer msgfStr=new StringBuffer("");
 
		//Sklad chemiczny modyfikacji
		String	elements[]={"C","H","N","O","S","P","Br","Cl","Fe"};
		for (String elem:elements)
		{			
			Double elemCount=ptm.getComposition().getCompositionMap().get(ElementMap.getElement(elem));
			if (elemCount!=null)
			{
				msgfStr.append(elem);
				msgfStr.append(elemCount.intValue());
			}
		}	
		msgfStr.append(",");
		
		//Aminokwasy, na ktorych wystepuje modyfikacja
		if (ptm.getSitesCount()==1 && (ptm.getSite(0).equals("C-term") || ptm.getSite(0).equals("N-term")))
			msgfStr.append("*");
		else
		{
			for (String site:ptm.getSites())
				msgfStr.append(site);
		}
		msgfStr.append(",");
		
		//Czy modyfikacja jest opcjonalna 
		if (fixed)
			msgfStr.append("fix,");
		else
			msgfStr.append("opt,");

		//Miejsce wystepowanie modyfikacji
		if (ptm.getSitesCount()==1 && ptm.getSite(0).equals("C-term"))
			msgfStr.append("C-term,");
		else if (ptm.getSitesCount()==1 && ptm.getSite(0).equals("N-term"))
			msgfStr.append("N-term,");
		else
			msgfStr.append("any,");
		
		//Nazwa
		msgfStr.append(ptm.getName());
		
		return(msgfStr.toString());
	}

	
	public void prepareParameters(SearchParameters params){
		
		//StringBuffer parameters=new StringBuffer();
		String parameters="";
		
		try {
			Files.deleteIfExists(Paths.get("C:\\Users\\Ewelinka\\Desktop\\output.txt"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(!(params.getParentMassTolerance().equals(""))){
			
			parameters+=" -t " +  params.getParentMassTolerance() + params.getParentMassToleranceUnit();
		}
		
		if(!(params.getInstrument().equals(""))){		
		
			parameters+=" -inst " + params.getInstrument();
		}
		
		if(!(params.getEnzyme().equals(""))){
		
			parameters+=" -e " + params.getEnzyme() + " -ntt 1";
		
		}
		
		if(!(params.getFragmentationMethod().equals(""))){
			
			parameters+=" -m " + params.getFragmentationMethod();
		
		}
		
		String[] fixedPtmNames=params.getFixedModifications();
		
		if(!(fixedPtmNames.length==0)){
			
			for (String name : fixedPtmNames)
			{
			
				PTM ptm=PTMTools.getPTM(name);		
		
				try {
					Files.write(Paths.get("C:\\Users\\Ewelinka\\Desktop\\output.txt"), (getMsgfPtmString(ptm,true) + System.lineSeparator()).getBytes(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} 
		}
		
		String[] variousPtmNames=params.getVariousModification();
		
		if(!(variousPtmNames.length==0)){
			
		
			for (String name : variousPtmNames)
			{
			
				PTM ptm=PTMTools.getPTM(name);	
		
				try {
					Files.write(Paths.get("C:\\Users\\Ewelinka\\Desktop\\output.txt"), (getMsgfPtmString(ptm,false) + System.lineSeparator()).getBytes(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} 
		} 
		
		parameters += " -mod C:\\Users\\Ewelinka\\Desktop\\output.txt";
		
		parameters += " -ti 0,1 -tda 0 -n 10 -minLength 8 -maxLength 30"; //TODO: ZAAWANSOWANE USTAWIENIA!
		
		mParams = parameters;
		
		this.readDB(params.getTaxonomy(), params.getDBname());
		
		System.out.println("PARAMETRY: " + mParams);
		
	}
	
	
	LinkedHashMap<String,MsMsProteinHit> proteinsMap=new LinkedHashMap<String,MsMsProteinHit>();
	LinkedHashMap<String,MsMsPeptideHit> peptidesMap=new LinkedHashMap<String,MsMsPeptideHit>();
	
	
	private boolean readResult(){
		/*
		 * Odczyt pliku
		 */
		
		MsMsScanConfig scanConfig=new MsMsScanConfig();
		scanConfig.mDbEngine=DbEngineScoring.DB_ENGINE_MSGF;
		scanConfig.mDbEngineFileFormat=MsMsFile.FORMAT_MSGF_MZIDENTML;
		
		
		MzIdentFileReader reader;
		try {
			reader = new MzIdentFileReader(mOutFilename,scanConfig);
			reader.readFile();
			reader.closeFile();
			reader.createHashes(proteinsMap,peptidesMap);
			
			
		} catch (MScanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	
	public SearchResult prepareSearchResult()
	{
			
			/*
			 * Pobranie i wyswietlenie naglowka - parametry wyszukiwania 
			 */
			//MsMsFileHeader header=reader.getHeader();
			/*System.out.println("\nTitle: " + header.getSearchTitle());
			System.out.println("User: " + header.getUser());
			System.out.println("User mail: " + header.getUserMail());
			System.out.println("Data file: " + header.getMsDataFile());
			System.out.println("Enzyme: " + header.getEnzyme());
			System.out.println("Missed cleavages: " + header.getMissedCleavages());
			System.out.println("Instrument: " + header.getInstrumentName());
			System.out.println("Parent MMD: " + header.getParentMMDString());
			System.out.println("Fragment MMD: " + header.getFragmentMMDString());
			System.out.println("Fixed PTMs: " + header.getFixedPTMsString());
			System.out.println("Variable PTMs: " + header.getVariablePTMsString());  */
			
			
			
			/*
			 * Wyswietlenie wynikow
			 */
			
			SearchResult searchResult = new SearchResult();
			
			System.out.println("ROZMIAR: " + proteinsMap.values().size());
			
			for (MsMsProteinHit protein : proteinsMap.values())
			{
				Protein proteinNEW = new Protein();
				
				proteinNEW.setID(protein.getId().toString());
				proteinNEW.setName(protein.getName().toString());
				proteinNEW.setScore(Double.toString(protein.getScore()));
				proteinNEW.setPeptidesCount(Integer.toString(protein.getPeptidesCount()));
				
				//informacje o bialku: ID, nazwa, score, liczba peptydow 
				System.out.println("Protein: " + protein.getId() + "\t" + protein.getName() + "\t" + protein.getScore() + "\t" + protein.getPeptidesCount());

				//Wyswietlenie peptydow bialka
				for (MsMsPeptideHit peptide : protein.getPeptides().values())
				{
					Peptide peptideNEW = new Peptide();
					
					peptideNEW.setSequence(peptide.getSequence());
					peptideNEW.setMass(Double.toString(peptide.getCalcMass()));
					peptideNEW.setQueriesCount(Integer.toString(peptide.getQueriesCount()));
					//informacje o peptydzie: sekwencja i masa teoretyczna (wynikajaca z sekwencji)
					System.out.println("\tPeptide: " + peptide.getSequence() + "\t" + peptide.getCalcMass() + "\t" + peptide.getQueriesCount());
					
					//wyswietlenie widm przypisanych do peptydu
					for (MsMsQuery query: peptide.getQueriesList())
					{
						Spectrum spectrum = new Spectrum();
						
						spectrum.setNr(Integer.toString(query.getNr()));
						spectrum.setMz(Double.toString(query.getMz()));
						spectrum.setMass(Double.toString(query.getMass()));
						spectrum.setCharge(Byte.toString(query.getCharge()));
						spectrum.setMassDifference(Double.toString(MassTools.getDeltaPPM(query.getMass(), peptide.getCalcMass())));
						spectrum.setScore(Double.toString(query.getScore()));
						
						peptideNEW.addSpectrum(spectrum);
						//informacje o widmie: numer, m/z zmierzone, stopien naladowania, masa zmierzona, roznica mas w PPM, score
						System.out.println("\t\tQuery:" + query.getNr() + "\t" + query.getMz() + "\t+" + query.getCharge() + "\t" + query.getMass() + "\t" + MassTools.getDeltaPPM(query.getMass(), peptide.getCalcMass()) + "\t" + query.getScore());	
					}			
					
					proteinNEW.addPeptide(peptideNEW);
					
				}
				
				searchResult.addProtein(proteinNEW);
				
				
			} 
			
			return searchResult;
			
		
	} 
	
		
}
