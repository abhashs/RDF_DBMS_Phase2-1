package quadrupleIterator;


import labelheap.LabelHeapfile;
import quadrupleheap.Quadruple;
import global.*;
import java.io.*;
import java.lang.*;
import quadrupleIterator.QuadrupleUnknowAttrType;;

/**
 *some useful method when processing Tuple 
 */
public class QuadrupleUtils
{
	
//	private int compare;
	private static int cascadeCompare(Quadruple q1, Quadruple q2, int[] orderInts) {
		int count = 0;
		int result = 0;
		
		while(result == 0) {
			result = QuadrupleUtils.CompareQuadrupleWithQuadruple(q1, q2, orderInts[count]);
			count++;
		}
		return result;
	}
	
	
	public static int compareByOrder(Quadruple q1, Quadruple q2, QuadrupleOrder order) {
		int quadOrder = order.quadrupleOrder;
		
		if (quadOrder == QuadrupleOrder.SubjectPredicateObjectConfidence) {
			return cascadeCompare(q1, q2, QuadrupleOrder.orderOneIntArray);
		}
		else if (quadOrder == QuadrupleOrder.PredicateSubjectObjectConfidence) {
			return cascadeCompare(q1, q2, QuadrupleOrder.orderTwoArray);
		}
		else if (quadOrder == QuadrupleOrder.SubjectConfidence) {
			return cascadeCompare(q1, q2, QuadrupleOrder.orderThreeIntArray);
		}
		else if (quadOrder == QuadrupleOrder.PredicateConfidence) {
			return cascadeCompare(q1, q2, QuadrupleOrder.orderFourArray);
		}
		else if (quadOrder == QuadrupleOrder.ObjectConfidence) {
			return cascadeCompare(q1, q2, QuadrupleOrder.orderFiveArray);
		}
		else if (quadOrder == QuadrupleOrder.Confidence) {
			return cascadeCompare(q1, q2, QuadrupleOrder.orderSixArray);
		}
		else {
			System.err.println("Wrong order in compareByOrder");
			
			return 2;
		}
	}
  
  /**
   * This function compares a tuple with another tuple in respective field, and
   *  returns:
   *
   *    0        if the two are equal,
   *    1        if the tuple is greater,
   *   -1        if the tuple is smaller,
   *
   *@param    fldType   the type of the field being compared.
   *@param    t1        one tuple.
   *@param    t2        another tuple.
   *@param    t1_fld_no the field numbers in the tuples to be compared.
   *@param    t2_fld_no the field numbers in the tuples to be compared. 
   *@exception QuadrupleUnknowAttrType don't know the attribute type
   *@exception IOException some I/O fault
   *@exception QuadrupleUtilsException exception from this class
   *@return   0        if the two are equal,
   *          1        if the tuple is greater,
   *         -1        if the tuple is smaller,                              
   */
	public static int CompareQuadrupleWithQuadruple(Quadruple q1, Quadruple q2, int quadruple_fld_no) {
		try {
			if (quadruple_fld_no == 1 || quadruple_fld_no == 3) {
				
				LabelHeapfile entityHeapfile = new LabelHeapfile(SystemDefs.JavabaseDBName + "/ehfile");
				LID q1EntityLID = null;
				LID q2EntityLID = null;
				
				if (quadruple_fld_no == 1) {
					q1EntityLID = q1.getSubjectID().returnLID();
					q2EntityLID = q2.getSubjectID().returnLID();
				}
				else if (quadruple_fld_no == 3) {
					q1EntityLID = q1.getObjectID().returnLID();
					q2EntityLID = q2.getObjectID().returnLID();
				}
				
				int howMuchGreater = entityHeapfile.getLabel(q1EntityLID).getLabel().compareTo(
						entityHeapfile.getLabel(q2EntityLID).getLabel());
				
				if (howMuchGreater > 0) {
					return 1;
				}
				else if (howMuchGreater < 0) {
					return -1;
				}
				else {
					return 0;
				}		
			}
			else if (quadruple_fld_no == 2) {
				LabelHeapfile entityHeapfile = new LabelHeapfile(SystemDefs.JavabaseDBName + "/phfile");
				LID q1PredID = q1.getPredicateID().returnLID();
				LID q2PredID = q2.getPredicateID().returnLID();
				
				int howMuchGreater = entityHeapfile.getLabel(q1PredID).getLabel().compareTo(
						entityHeapfile.getLabel(q2PredID).getLabel());
				
				if (howMuchGreater > 0) {
					return 1;
				}
				else if (howMuchGreater < 0) {
					return -1;
				}
				else {
					return 0;
				}		
			}
			else if (quadruple_fld_no == 4) {
				if (q1.getConfidence() > q2.getConfidence()) {
					return 1;
				}
				else if (q1.getConfidence() < q2.getConfidence()) {
					return -1;
				}
				else {
					return 0;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	

 public static boolean Equal(Quadruple q1, Quadruple q2){
	  try {
		  for(int i = 1; i <= 3; i++) {
	    	  if(CompareQuadrupleWithQuadruple(q1, q2, i) != 0) {
	    		  return false;
	    	  }
	      }
	      return true; 
	  }
	  catch (Exception e) {
		  e.printStackTrace();
	  }
	  
	  return false;
}

  public static void SetValue(Quadruple q1, Quadruple q2) {
      q1.quadrupleCopy(q2);
  }
  
  
  // Used to be setup_op_tuple in iterator
  
  // Used to be setup_op_tuple in iterator
}




