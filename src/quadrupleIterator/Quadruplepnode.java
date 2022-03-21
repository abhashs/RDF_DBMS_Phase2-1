package quadrupleIterator; 

import global.*;
import bufmgr.*;
import diskmgr.*;
import heap.*;
import quadrupleheap.Quadruple;

/**
 * A structure describing a tuple.
 * include a run number and the tuple
 */
public class Quadruplepnode {
  /** which run does this tuple belong */
  public int     run_num;

  /** the tuple reference */
  public Quadruple   quadruple;

  /**
   * class constructor, sets <code>run_num</code> to 0 and <code>tuple</code>
   * to null.
   */
  public Quadruplepnode() 
  {
    run_num = 0;  // this may need to be changed
    quadruple = null; 
  }
  
  /**
   * class constructor, sets <code>run_num</code> and <code>tuple</code>.
   * @param runNum the run number
   * @param t      the tuple
   */
  public Quadruplepnode(int runNum, Quadruple t) 
  {
    run_num = runNum;
    quadruple = t;
  }
  
}

