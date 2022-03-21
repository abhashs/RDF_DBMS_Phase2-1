package global;

/** 
 * Enumeration class for TupleOrder
 * 
 */

public class QuadrupleOrder {

  public static final int SubjectPredicateObjectConfidence  = 1;
  public static final int PredicateSubjectObjectConfidence = 2;
  public static final int SubjectConfidence     = 3;
  public static final int PredicateConfidence = 4;
  public static final int ObjectConfidence     = 5;
  public static final int Confidence = 6;

  public int quadrupleOrder;
  
  public static final int[] orderOneIntArray = {1, 2, 3, 4};
  public static final int[] orderTwoArray = {2, 1, 3, 4};
  public static final int[] orderThreeIntArray = {1, 4};
  public static final int[] orderFourArray = {2, 4};
  public static final int[] orderFiveArray = {1, 4};
  public static final int[] orderSixArray = {4};
  

  /** 
   * TupleOrder Constructor
   * <br>
   * A tuple ordering can be defined as 
   * <ul>
   * <li>   TupleOrder tupleOrder = new TupleOrder(TupleOrder.Random);
   * </ul>
   * and subsequently used as
   * <ul>
   * <li>   if (tupleOrder.tupleOrder == TupleOrder.Random) ....
   * </ul>
   *
   * @param _tupleOrder The possible ordering of the tuples 
   */

  public QuadrupleOrder (int _quadrupleOrder) {
    quadrupleOrder = _quadrupleOrder;
  }

  public String toString() {
    
    switch (quadrupleOrder) {
    case SubjectPredicateObjectConfidence:
    	return "SubjectPredicateObjectConfidence";
    case PredicateSubjectObjectConfidence:
    	return "PredicateSubjectObjectConfidence";
    case SubjectConfidence:
    	return "SubjectConfidence";
    case PredicateConfidence:
    	return "PredicateConfidence";
    case ObjectConfidence:
    	return "ObjectConfidence";
    case Confidence:
    	return "Confidence";
    }
    
    return ("Unexpected QuadrupleOrder " + quadrupleOrder);
  }

}
