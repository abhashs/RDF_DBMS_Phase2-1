package global;

import java.io.*;

public class PID {
	/** public int slotNo
	   */
	  public int slotNo;
	  
	  /** public PageId pageNo
	   */
	  public PageId pageNo = new PageId();
	  
	  /**
	   * default constructor of class
	   */
	  public PID () { }
	  
	  /**
	   *  constructor of class
	   */
	  public PID (LID lid)
	    {
	      pageNo = lid.pageNo;
	      slotNo = lid.slotNo;
	    }
	  
	  /**
	   * make a copy of the given rid
	   */
	  public void copyPid (PID pid)
	    {
	      pageNo = pid.pageNo;
	      slotNo = pid.slotNo;
	    }
	  
	  /** Write the rid into a byte array at offset
	   * @param ary the specified byte array
	   * @param offset the offset of byte array to write 
	   * @exception java.io.IOException I/O errors
	   */ 
	  public void writeToByteArray(byte [] ary, int offset)
	    throws java.io.IOException
	    {
	      Convert.setIntValue ( slotNo, offset, ary);
	      Convert.setIntValue ( pageNo.pid, offset+4, ary);
	    }
	  
	  
	  /** Compares two RID object, i.e, this to the rid
	   * @param rid RID object to be compared to
	   * @return true is they are equal
	   *         false if not.
	   */
	  public boolean equals(PID pid) {
	    
	    if ((this.pageNo.pid==pid.pageNo.pid)
		&&(this.slotNo==pid.slotNo))
	      return true;
	    else
	      return false;
	  }
	  
	  public LID returnLID() {
		  return new LID(this.pageNo, this.slotNo);
	  }
}

