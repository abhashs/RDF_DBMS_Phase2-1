package btree;
import global.*;

/**  IndexData: It extends the DataClass.
 *   It defines the data "rid" for leaf node in B++ tree.
 */
public class QLeafData extends DataClass {
  private QID myRid;

  public String toString() {
     String s;
     s="[ "+ (new Integer(myRid.pageNo.pid)).toString() +" "
              + (new Integer(myRid.slotNo)).toString() + " ]";
     return s;
  }

  /** Class constructor
   *  @param    rid  the data rid
   */
  QLeafData(QID rid) {myRid= new QID(rid.pageNo, rid.slotNo);};  

  /** get a copy of the rid
  *  @return the reference of the copy 
  */
  public QID getData() {return new QID(myRid.pageNo, myRid.slotNo);};

  /** set the rid
   */ 
  public void setData(QID rid) { myRid= new QID(rid.pageNo, rid.slotNo);};
}   
