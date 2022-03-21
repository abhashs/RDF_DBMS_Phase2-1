package tests;

import java.io.File;

import diskmgr.Pcounter;
import diskmgr.Stream;
import global.QID;
import global.QuadrupleOrder;
import global.SystemDefs;
import labelheap.LabelHeapfile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapfile;

public class Query {
	
	public static void quadruplePrint(Quadruple quadruple) {
		
		LabelHeapfile entityHeap = SystemDefs.JavabaseDB.getEntityHeap();
		LabelHeapfile predicateHeap = SystemDefs.JavabaseDB.getPredicateHeap();
		
		try {
			String subject = entityHeap.getLabel( quadruple.getSubjectID().returnLID()).getLabel();
			String predicate = predicateHeap.getLabel( quadruple.getPredicateID().returnLID()).getLabel();
			String object = entityHeap.getLabel( quadruple.getObjectID().returnLID()).getLabel();
			
			System.out.println(subject + " " + predicate + " " + object + quadruple.getConfidence());
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	public static void main(String[] args){
		String rdfdbname = args[0];
		int indexOption = Integer.parseInt(args[1]);
		int order = Integer.parseInt(args[2]);
		String subjectFilter = args[3];
		String predicateFilter = args[4];
		String objectFilter = args[5];
		
		float confidenceFilter = -1;
		
		if (args[6].compareTo("+") != 0) {
			System.out.println(args[6]);
			confidenceFilter = Float.parseFloat(args[6]);
		}
		
		int numBuf = Integer.parseInt(args[7]);
		
		File databaseFile = new File(rdfdbname);
		if (databaseFile.exists()){
			try {
				//Maybe 1000
				SystemDefs sysdefs = new SystemDefs(rdfdbname, 0, numBuf, "Clock", indexOption);
				
				SystemDefs.JavabaseDB.init();
				
				QuadrupleHeapfile qHeap = new QuadrupleHeapfile(rdfdbname + "/qhfile");
				
				Stream stream = SystemDefs.JavabaseDB.openStream(
						new QuadrupleOrder(order), subjectFilter, predicateFilter, objectFilter, confidenceFilter);

				QID qid = new QID();
				Quadruple quadruple = stream.getNext(qid);
				
				while(quadruple != null) {
					Query.quadruplePrint(quadruple);
				}
				
				if (stream != null) {stream.closeStream();}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
//		System.out.println("EntityCount: " + SystemDefs.JavabaseDB.getEntityCnt());
//		System.out.println("SubjectCount: " + SystemDefs.JavabaseDB.getSubjectCnt());
//		System.out.println("PredicateCount: " + SystemDefs.JavabaseDB.getPredicateCnt());
//		System.out.println("ObjectCount: " + SystemDefs.JavabaseDB.getObjectCnt());
//		System.out.println("Quadruple: " + SystemDefs.JavabaseDB.getQuadrupleCnt());
//		
//		System.out.println(Pcounter.rcounter);
//		System.out.println(Pcounter.wcounter);
	}
	
}
