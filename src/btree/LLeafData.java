package btree;
import global.*;

/**  IndexData: It extends the DataClass.
 *   It defines the data "rid" for leaf node in B++ tree.
 */
public class LLeafData extends DataClass {
  private LID myRid;

  public String toString() {
     String s;
     s="[ "+ (new Integer(myRid.pageNo.pid)).toString() +" "
              + (new Integer(myRid.slotNo)).toString() + " ]";
     return s;
  }

  /** Class constructor
   *  @param    rid  the data rid
   */
  LLeafData(LID rid) {myRid= new LID(rid.pageNo, rid.slotNo);};  

  /** get a copy of the rid
  *  @return the reference of the copy 
  */
  public LID getData() {return new LID(myRid.pageNo, myRid.slotNo);};

  /** set the rid
   */ 
  public void setData(LID rid) { myRid= new LID(rid.pageNo, rid.slotNo);};
}   
