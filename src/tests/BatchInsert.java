package tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import diskmgr.Pcounter;
import global.*;
import labelheap.Label;
import labelheap.LabelHeapfile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapfile;


public class BatchInsert implements GlobalConst  {
	
	public static void main(String args[]) {
		String dataFilename = args[0];
		int indexOption = Integer.parseInt(args[1]);
		String rdfDbName = args[2];
		
		File databaseFile = new File(rdfDbName);
		File testFile = new File("src/tests/" + dataFilename);
		
		if (databaseFile.exists()) {
			SystemDefs sysdef = new SystemDefs( rdfDbName, 0, NUMBUF, "Clock", indexOption);
		} else {
			SystemDefs sysdef = new SystemDefs( rdfDbName, 10000, NUMBUF, "Clock", indexOption);
		}
		SystemDefs.JavabaseDB.init();
		
		
		try {
			List <String> testFileLines = Files.readAllLines(testFile.toPath());
			
			int count = 0;
			for (String line : testFileLines) {
				
				if (line.length() == 0) {
					continue;
				}
				line = line.substring(1);
				
				int index = line.indexOf(":");
				
				if (index == -1) { continue; }
				String subjectString = line.substring(0, index).replaceAll("\\s+", "");
				line = line.substring(index + 1);
				
				index = line.indexOf(":");
				if (index == -1) { continue; }
				String predicateString = line.substring(0, index).replaceAll("\\s+", "");
				line = line.substring(index + 1);
				
				index = line.indexOf("\t");
				if (index == -1) { continue; }
				String objectString = line.substring(0, index).replaceAll("\\s+", "");
				line = line.substring(index + 1);
				
				float confidence = Float.parseFloat(line.replaceAll("\\s+",""));
				
				//Check if confidence is within valid range, if not throw this line out
				if (confidence < 0 || confidence > 1) {
					continue;
				}
				
				
				EID subjectEID = SystemDefs.JavabaseDB.insertEntity(subjectString); //Subject
				PID predicatePID = SystemDefs.JavabaseDB.insertPredicate(predicateString); //Predicate
				EID objectEID = SystemDefs.JavabaseDB.insertEntity(objectString); //Object
				
				
				Quadruple quadruple = new Quadruple();
				quadruple.setSubjectID(subjectEID);
				quadruple.setPredicateID(predicatePID);
				quadruple.setObjectID(objectEID);
				quadruple.setConfidence(confidence);
			
				SystemDefs.JavabaseDB.insertQuadruple(quadruple.getQuadrupleByteArray());
				
				count++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SystemDefs.JavabaseDB.createIndex();
		System.out.println("Index created on indexOption " + indexOption);
		
//		System.out.println(Pcounter.rcounter);
//		System.out.println(Pcounter.wcounter);
		
		try {
			System.out.println("\n==================== Report ====================");
			System.out.println("Quadruple Statistics");
			System.out.println("\t# of Subjects:\t\t\t" + SystemDefs.JavabaseDB.getSubjectCnt());
			System.out.println("\t# of Predicates:\t\t" + SystemDefs.JavabaseDB.getPredicateCnt());
			System.out.println("\t# of Objects:\t\t\t" + SystemDefs.JavabaseDB.getObjectCnt());
			System.out.println("\t# of Entities:\t\t\t" + SystemDefs.JavabaseDB.getEntityCnt());
			System.out.println("\t# of Quadruples:\t\t" + SystemDefs.JavabaseDB.getQuadrupleCnt());
			System.out.println("Page Statistics");
			System.out.println("\tPage Size (bytes):\t\t" + SystemDefs.JavabaseDB.db_page_size());
			System.out.println("\t# of Pages:\t\t\t" + SystemDefs.JavabaseDB.db_num_pages());
			System.out.println("\t# of Page reads:\t\t" + Pcounter.rcounter);
			System.out.println("\t# of Page writes:\t\t" + Pcounter.wcounter);
			System.out.println("Total Database Size in Bytes:\t\t" + databaseFile.length());
			System.out.println("=================================================\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
