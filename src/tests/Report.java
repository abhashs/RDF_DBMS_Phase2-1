package tests;

import java.io.File;

import diskmgr.Pcounter;
import global.*;
import labelheap.LabelHeapfile;
import quadrupleheap.QuadrupleHeapfile;

public class Report {
	
	
	public static void main(String[] args) {
		try {
			String rdfdbname = args[0];
//			String indexOption = args[1];
			
			File databaseFile = new File(rdfdbname);
			
			if (databaseFile.exists()) {
				SystemDefs sysdefs = new SystemDefs(rdfdbname, 0, 1000, "Clock", 1);
				
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
		}catch (Exception e) {
			
		}
	}
}
