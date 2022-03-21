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
import quadrupleheap.Quadruple;

public class BatchInsert implements GlobalConst  {
	
	
	public static void main(String args[]) {
		
		String dataFilename = args[0];
		int indexOption = Integer.parseInt(args[1]);
		String rdfDbName = args[2];
		
//		String dbpath = "/tmp/"+ rdfDbName +System.getProperty("user.name")+".minibase-db"; 
//	    String logpath = "/tmp/"+ rdfDbName +System.getProperty("user.name")+".minibase-log"; 
		
		// 1000 pages? or something else
		
		
		File databaseFile = new File(rdfDbName);
		File testFile = new File("src/tests/" + dataFilename);
		
		SystemDefs sysdef = new SystemDefs( rdfDbName, 1000, NUMBUF, "Clock", indexOption);
		
//		if (databaseFile.exists()) {
//			System.out.println("Loading Db");
//			SystemDefs sysdef = new SystemDefs( rdfDbName, 0, NUMBUF, "Clock", indexOption);
//		} else {
//			System.out.println("New Db");
//			SystemDefs sysdef = new SystemDefs( rdfDbName, 10000, NUMBUF, "Clock", indexOption);
//		}
		
		SystemDefs.JavabaseDB.init();
		
		try {
			List <String> testFileLines = Files.readAllLines(testFile.toPath());
			
			int count = 0;
			for (String line : testFileLines) {
				
//				String[] lineTokens = line.replaceAll("\\s+","").substring(1).split(":");
//				String[] idsAndConfidence = line.split("\t");
//				String[] idTokens = idsAndConfidence[0].replaceAll("\\s+","").substring(1).split(":");
				
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
				
				
//				//Check if ONLY subject object and predicate are present, if not throw this line out (not including confidence yet)
//				if (idTokens.length != 3) { 
//					continue;
//				}
//				System.out.println(line);
//				//Check if confidence is present, if not throw this line out
//				if (idsAndConfidence[2] == null) {
//					continue;
//				}
				
//				if (subjectString == "Gunnar_Danielsen") {
//					System.out.println("Stop here");
//				}
				
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
		
		System.out.println("EntityCount: " + SystemDefs.JavabaseDB.getEntityCnt());
		System.out.println("SubjectCount: " + SystemDefs.JavabaseDB.getSubjectCnt());
		System.out.println("PredicateCount: " + SystemDefs.JavabaseDB.getPredicateCnt());
		System.out.println("ObjectCount: " + SystemDefs.JavabaseDB.getObjectCnt());
		System.out.println("Quadruple: " + SystemDefs.JavabaseDB.getQuadrupleCnt());
		
		System.out.println(Pcounter.rcounter);
		System.out.println(Pcounter.wcounter);
		
		SystemDefs.JavabaseDB.createIndex();
//		SystemDefs.JavabaseDB.deinit();
//		SystemDefs.JavabaseDB.getEntityHeap()
//		try {
//			System.out.println(SystemDefs.JavabaseDB.getEntityHeap().getLabel(new LID(new PageId(14), 11)).getLabel());
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
		
		
	}
}
