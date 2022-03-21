package diskmgr;

/** JAVA */
/**
 * Scan.java-  class Scan
 *
 */

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import btree.KeyDataEntry;
import btree.QBTFileScan;
import btree.QBTreeFile;
import btree.QLeafData;
import btree.StringKey;
import global.*;
import labelheap.Label;
import labelheap.LabelHeapfile;
import quadrupleIterator.QuadrupleSort;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapfile;
import quadrupleheap.TScan;
import bufmgr.*;
import diskmgr.*;


/**	
 * A Scan object is created ONLY through the function openScan
 * of a HeapFile. It supports the getNext interface which will
 * simply retrieve the next record in the heapfile.
 *
 * An object of type scan will always have pinned one directory page
 * of the heapfile.
 */
public class Stream implements GlobalConst{
	
	private TScan tempScan;
	private QuadrupleHeapfile tempQHeap;
 
    /**
     * Note that one record in our way-cool HeapFile implementation is
     * specified by six (6) parameters, some of which can be determined
     * from others:
     */
	private QuadrupleSort quadrupleSort;
	
	public boolean checkIfFiltersSatisfy(boolean[] nullFilters, Quadruple quadruple, rdfDB rdfdatabase, 
			String subjectFilter, String predicateFilter, String objectFilter, float confidenceFilter) throws Exception {
		for (int i = 0; i < nullFilters.length; i++) {
				if (nullFilters[i]) {
					switch (i) {
						case 0: {
							if (rdfdatabase.getEntityHeap().getLabel(
									quadruple.getSubjectID().returnLID()).getLabel().compareTo(subjectFilter) != 0) {
								return false;
							}
						}
						case 1: {
							if (rdfdatabase.getPredicateHeap().getLabel(
									quadruple.getPredicateID().returnLID()).getLabel().compareTo(predicateFilter) != 0) {
								return false;
							}
						}
						case 2: {
							if (rdfdatabase.getEntityHeap().getLabel(
									quadruple.getObjectID().returnLID()).getLabel().compareTo(objectFilter) != 0) {
								return false;
							}
						}
						case 3: {
							if (quadruple.getConfidence() != confidenceFilter) {
								return false;
							}
						}
							
					}
				}
			}
		return true;
	}
	
	public void scanQuadHeapWithFilters(rdfDB rdfdatabase, boolean[] nullFilters, 
			String subjectFilter, String predicateFilter, String objectFilter, float confidenceFilter) throws Exception {
		tempQHeap = new QuadrupleHeapfile("/thfile"); 
		TScan qscan = new TScan(tempQHeap);
		
		QID qid = new QID();
		Quadruple quadruple = qscan.getNext(qid);
		while (quadruple != null) {
			boolean satisfiesFilters = checkIfFiltersSatisfy(nullFilters, quadruple, rdfdatabase, subjectFilter, predicateFilter, objectFilter, confidenceFilter);
			
			if (satisfiesFilters) {
				tempQHeap.insertQuadruple(quadruple.getQuadrupleByteArray());
			}
			quadruple = qscan.getNext(qid);
		}
		qscan.closescan();
	}
	
	public boolean subjectFilterCheck(KeyDataEntry nextEntry, QuadrupleHeapfile quadHeapfile, 
			LabelHeapfile entityHeapfile, String filter) throws Exception {
		QID qid = ((QLeafData) nextEntry.data).getData();
		Quadruple quadruple = quadHeapfile.getQuadruple(qid);
		
		String quadSubject = entityHeapfile.getLabel(quadruple.getSubjectID().returnLID()).getLabel();
		
		if (quadSubject != filter) {
			return false;
		}	
		return true;
	}
	
	public boolean predicateFilterCheck(KeyDataEntry nextEntry, QuadrupleHeapfile quadHeapfile, 
			LabelHeapfile predicateHeapfile, String filter) throws Exception {
		QID qid = ((QLeafData) nextEntry.data).getData();
		Quadruple quadruple = quadHeapfile.getQuadruple(qid);
		
		String quadPred = predicateHeapfile.getLabel(quadruple.getPredicateID().returnLID()).getLabel();
		
		if (quadPred != filter) {
			return false;
		}	
		return true;
	}
	
	
	public boolean objectFilterCheck(KeyDataEntry nextEntry, QuadrupleHeapfile quadHeapfile, 
			LabelHeapfile entityHeapfile, String filter) throws Exception {
		QID qid = ((QLeafData) nextEntry.data).getData();
		Quadruple quadruple = quadHeapfile.getQuadruple(qid);
		
		String quadObject = entityHeapfile.getLabel(quadruple.getObjectID().returnLID()).getLabel();
		
		if (quadObject != filter) {
			return false;
		}	
		return true;
	}
	
	public boolean confidenceFilterCheck(KeyDataEntry nextEntry, QuadrupleHeapfile quadHeapfile, float filter) throws Exception {
		QID qid = ((QLeafData) nextEntry.data).getData();
		Quadruple quadruple = quadHeapfile.getQuadruple(qid);
		
		Float quadConfidence = quadruple.getConfidence();
		
		if (quadConfidence != filter) {
			return false;
		}
		return true;
	}
	
	public void scanIndexTree(int firstFilter, int secondFilter, boolean[] nullFilters,
			rdfDB rdfdatabase, String subjectFilter, String predicateFilter, String objectFilter, String confidenceFilter) throws Exception {
		
		QBTreeFile indexBTree = rdfdatabase.getIndexBTreeFile();
		QuadrupleHeapfile quadHeapfile = rdfdatabase.getQuadrupleHeap();
		LabelHeapfile entityHeapfile = rdfdatabase.getEntityHeap();
		LabelHeapfile predicateHeapfile = rdfdatabase.getPredicateHeap();
		
		Map<Integer, String> filterMap = new HashMap<Integer, String>();
		filterMap.put(0, subjectFilter);
		filterMap.put(1, predicateFilter);
		filterMap.put(2, objectFilter);
		filterMap.put(3, confidenceFilter);
		
		String conjoinedFilters = filterMap.get(firstFilter) + "," + filterMap.get(secondFilter);
		
		StringKey key = new StringKey(conjoinedFilters);
		QBTFileScan indexFileScan = indexBTree.new_scan(key, key);
		KeyDataEntry nextEntry = indexFileScan.get_next();
		
		while (nextEntry != null && ((StringKey) (nextEntry.key)).getKey().equals(conjoinedFilters)) {
			boolean satisfiesFilters = true;
			
			if (firstFilter == 0) {
				satisfiesFilters = subjectFilterCheck(nextEntry, quadHeapfile, entityHeapfile, subjectFilter);
			}
			if (firstFilter == 1) {
				satisfiesFilters = predicateFilterCheck(nextEntry, quadHeapfile, predicateHeapfile, predicateFilter);
			}
			if (firstFilter == 2) {
				satisfiesFilters = objectFilterCheck(nextEntry, quadHeapfile, entityHeapfile, objectFilter);
			}
			if (firstFilter == 3) {
				satisfiesFilters = subjectFilterCheck(nextEntry, quadHeapfile, entityHeapfile, subjectFilter);
			}
			
			if (satisfiesFilters) {
				QID qid = ((QLeafData) nextEntry.data).getData();
				Quadruple quad = quadHeapfile.getQuadruple(qid);
				tempQHeap.insertQuadruple(quad.getQuadrupleByteArray());
			}
			
//			if (nullFilters[2]) {
//				satisfiesFilters = objectFilterCheck(nextEntry, quadHeapfile, entityHeapfile, objectFilter);
//			}
//			if (nullFilters[3] && satisfiesFilters) {
//					satisfiesFilters = confidenceFilterCheck(nextEntry, quadHeapfile, Float.parseFloat(confidenceFilter));
//			}
		}
		
		indexFileScan.DestroyBTreeFileScan();
		indexBTree.close();
	}

    
     
    /** The constructor pins the first directory page in the file
     * and initializes its private data members from the private
     * data member from hf
     *
     * @exception InvalidTupleSizeException Invalid tuple size
     * @exception IOException I/O errors
     *
     * @param hf A HeapFile object
     */
	//used to be int not QuadrupleOrder
  public Stream(rdfDB rdfdatabase, QuadrupleOrder orderType, String subjectFilter, String predicateFilter, 
		  String objectFilter, float confidenceFilter) throws Exception
  {
	  
	  boolean[] nullFilters = {
			  	subjectFilter == "+",
				predicateFilter == "+",
				objectFilter == "+",
				confidenceFilter == (float) -1.0
	  };
	  
	  boolean[] indexOneKeys = { true, true, false, false };
	  boolean[] indexTwoKeys = { true, false, true, false };
	  boolean[] indexThreeKeys = {false, false, true, true };
	  boolean[] indexFourKeys = {true, false, false, true };
	  boolean[] indexFiveKeys = {false, true, true, false };

	   
	  switch(rdfdatabase.indexOption) {
	  	case 1: {
	  		
	  		int count = 0;
	  		boolean mismatch = false;
 	  		
	  		while(!mismatch && count < indexOneKeys.length) {
	  			
	  			if (indexOneKeys[count] && !nullFilters[count]) {
	  				mismatch = true;
	  			}
	  			else {
	  				count++;
	  			}
  			}
	  		
	  		if (mismatch) {
	  			//case 2 get quad heap and filter
	  			scanQuadHeapWithFilters(rdfdatabase, nullFilters, subjectFilter, predicateFilter, objectFilter, confidenceFilter);
	  		}
	  		else {
	  			//we know we can use subject predicate
	  			//check if object is a filter, or confidence is a filter
	  			
	  			scanIndexTree(2, 3, nullFilters, rdfdatabase, subjectFilter, predicateFilter, objectFilter, Float.toString(confidenceFilter) );

	  		}
	  		
	  		break;
	  	}	
	  	
	  	case 2: {
	  		int count = 0;
	  		boolean mismatch = false;
 	  		
	  		while(!mismatch && count < indexTwoKeys.length) {
	  			
	  			if (indexTwoKeys[count] && !nullFilters[count]) {
	  				mismatch = true;
	  			}
	  			else {
	  				count++;
	  			}
  			}
	  		
	  		if (mismatch) {
	  			//case 2 get quad heap and filter
	  			scanQuadHeapWithFilters(rdfdatabase, nullFilters, subjectFilter, predicateFilter, objectFilter, confidenceFilter);
	  		}
	  		else {
	  			//we know we can use subject, object
	  			//check if predicate is a filter, or confidence is a filter
	  			scanIndexTree(1, 3, nullFilters, rdfdatabase, subjectFilter, predicateFilter, objectFilter, Float.toString(confidenceFilter) );
	  		}
	  		
	  		break;
	  	}
	  	
	  	case 3: {
	  		int count = 0;
	  		boolean mismatch = false;
 	  		
	  		while(!mismatch && count < indexThreeKeys.length) {
	  			
	  			if (indexThreeKeys[count] && !nullFilters[count]) {
	  				mismatch = true;
	  			}
	  			else {
	  				count++;
	  			}
  			}
	  		
	  		if (mismatch) {
	  			scanQuadHeapWithFilters(rdfdatabase, nullFilters, subjectFilter, predicateFilter, objectFilter, confidenceFilter);
	  			//case 2 get quad heap and filter
	  		}
	  		else {
	  			//we know we can use object, confidence
	  			//check if subject is a filter, or predicate is a filter
	  			scanIndexTree(0, 1, nullFilters, rdfdatabase, subjectFilter, predicateFilter, objectFilter, Float.toString(confidenceFilter) );
	  		}
	  		
	  		break;
	  	}
	  	
	  	case 4: {
	  		int count = 0;
	  		boolean mismatch = false;
 	  		
	  		while(!mismatch && count < indexFourKeys.length) {
	  			
	  			if (indexFourKeys[count] && !nullFilters[count]) {
	  				mismatch = true;
	  			}
	  			else {
	  				count++;
	  			}
  			}
	  		
	  		if (mismatch) {
	  			//case 2 get quad heap and filter
	  			scanQuadHeapWithFilters(rdfdatabase, nullFilters, subjectFilter, predicateFilter, objectFilter, confidenceFilter);
	  		}
	  		else {
	  			//we know we can use subject, confidence
	  			//check if predicate is a filter, or object is a filter
	  			scanIndexTree(1, 2, nullFilters, rdfdatabase, subjectFilter, predicateFilter, objectFilter, Float.toString(confidenceFilter) );
	  		}
	  		
	  		break;
	  	}
	  	
	  	case 5: {
	  		int count = 0;
	  		boolean mismatch = false;
 	  		
	  		while(!mismatch && count < indexFiveKeys.length) {
	  			
	  			if (indexFiveKeys[count] && !nullFilters[count]) {
	  				mismatch = true;
	  			}
	  			else {
	  				count++;
	  			}
  			}
	  		
	  		if (mismatch) {
	  			scanQuadHeapWithFilters(rdfdatabase, nullFilters, subjectFilter, predicateFilter, objectFilter, confidenceFilter);
	  			//case 2 get quad heap and filter
	  		}
	  		else {
	  			//we know we can use predicate, object
	  			//check if subject is a filter, or confidence is a filter
	  			scanIndexTree(0, 3, nullFilters, rdfdatabase, subjectFilter, predicateFilter, objectFilter, Float.toString(confidenceFilter) );
	  		}
	  		
	  		break;
	  	}
	  }
	  	
  	//Sort the results
	tempScan = new TScan(tempQHeap);
	try 
	{
		quadrupleSort = new QuadrupleSort(tempScan, orderType , 20);
	}
	catch (Exception e) 
	{
		e.printStackTrace();
	}

  
  
  }
	  
	  
	  

	  
	  // based on index option, get quadruples in certain index format btree (i.e (Subject,Predicate))
	  // this index btree points from keys of Subject,Predicate => quadruple object
	  // this quadruple object contains all 4 fields, which must match filters
	  
	  // Case 1:
	  // keys in btree == filters (and lines up)
	  // nullFilters = {"s", "s", "*", -1}
	  // keyInBtree = (Subject, Predicate)
	  // Scan btree USING the filters, anything that is returned would satisfy the filter already
	  //
	  // Case 2:
	  // Filters < keys fields in btree
	  // nullFilters = {"s", "*", "*", -1}
	  // keyInBtree = (subject, predicate)
	  // we can't filter then key, because our key is missing predicates.
	  // instead we get the whole quadruple heap, and filter from there
	  //
	  // Case 3:
	  // Filters > key fields in btree
	  // nullFilters = {"s", "s", "s", -1}
	  // keyInBtree = (Subject, Predicate)
	  // subjectFilter = "Austin"
	  // predicateFilter = "drives"
	  // objectFilter = "toyota"
	  // key = "Austin,drives"
	  
	  // Scan btree with key but not filter yet, on those quadruples, we can filter
	  
	  
	  
	  //result (Quadruples):
	  //"Austin drives toyota"
	  // "Austin drives honda"
	  // "Austin drives nissan"
	  
	  // case 1 and case 3 allow us to scan with the index file
	  
	  // Implementation
	  // KEYS = (SUBJECT, PREDICATE)
	  // filters = (subject, predicate, object)
	  // difference = -object
	  
	  
	  
	  
	  // if negative difference, get quads with key, apply different filter
	  // if postive different, apply different filters to entire quad heap, get 
	  
	  
//	  init(hf);
  


  
  /** Retrieve the next record in a sequential scan
   *
   * @exception InvalidTupleSizeException Invalid tuple size
   * @exception IOException I/O errors
   *
   * @param rid Record ID of the record
   * @return the Tuple of the retrieved record.
   */
  public Quadruple getNext(QID rid) throws Exception
  {
    return quadrupleSort.get_next();
  }

  
  public void closeStream() {
	  try {
		  if (tempScan != null) {
			  tempScan.closescan();
		  }
		  if (tempQHeap != null) {
			  tempQHeap.deleteFile();
		  }
		  if (quadrupleSort != null) {
			  quadrupleSort.close();
		  }

	  } catch (Exception e) {
		  e.printStackTrace();
	  }  
  }



}
