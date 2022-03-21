package quadrupleIterator;
import global.*;
import heap.*;
import diskmgr.*;
import bufmgr.*;
import index.*;
import iterator.PredEvalException;
import iterator.UnknownKeyTypeException;
import quadrupleheap.Quadruple;

import java.io.*;

/**
 *All the relational operators and access methods are iterators.
 */
public abstract class QuadrupleIterator implements Flags {
  
  /**
   * a flag to indicate whether this iterator has been closed.
   * it is set to true the first time the <code>close()</code> 
   * function is called.
   * multiple calls to the <code>close()</code> function will
   * not be a problem.
   */
  public boolean closeFlag = false; // added by bingjie 5/4/98

  /**
   *abstract method, every subclass must implement it.
   *@return the result tuple
   *@exception IOException I/O errors
   *@exception QuadrupleJoinsException some join exception
   *@exception IndexException exception from super class    
   *@exception InvalidTupleSizeException invalid tuple size
   *@exception InvalidTypeException tuple type not valid
   *@exception PageNotReadException exception from lower layer
   *@exception TupleUtilsException exception from using tuple utilities
   *@exception PredEvalException exception from PredEval class
   *@exception SortException sort exception
   *@exception LowMemException memory error
   *@exception UnknowAttrType attribute type unknown
   *@exception UnknownKeyTypeException key type unknown
   *@exception Exception other exceptions
   */
  public abstract Quadruple get_next() 
    throws IOException,
	   QuadrupleJoinsException ,
	   IndexException,
	   InvalidTupleSizeException,
	   InvalidTypeException, 
	   PageNotReadException,
	   QuadrupleUtilsException, 
	   PredEvalException,
	   SortException,
	   LowMemException,
	   QuadrupleUnknowAttrType,
	   UnknownKeyTypeException,
	   Exception;

  /**
   *@exception IOException I/O errors
   *@exception QuadrupleJoinsException some join exception
   *@exception IndexException exception from Index class
   *@exception SortException exception Sort class
   */
  public abstract void close() 
    throws IOException, 
	   QuadrupleJoinsException, 
	   SortException,
	   IndexException;
  
  /**
   * tries to get n_pages of buffer space
   *@param n_pages the number of pages
   *@param PageIds the corresponding PageId for each page
   *@param bufs the buffer space
   *@exception QuadrupleIteratorBMException exceptions from bufmgr layer
   */
  public void  get_buffer_pages(int n_pages, PageId[] PageIds, byte[][] bufs)
    throws QuadrupleIteratorBMException
    {
      Page pgptr = new Page();        
      PageId pgid = null;
      
      for(int i=0; i < n_pages; i++) {
	pgptr.setpage(bufs[i]);

	pgid = newPage(pgptr,1);
	PageIds[i] = new PageId(pgid.pid);
	
	bufs[i] = pgptr.getpage();
	
      }
    }

  /**
   *free all the buffer pages we requested earlier.
   * should be called in the destructor
   *@param n_pages the number of pages
   *@param PageIds  the corresponding PageId for each page
   *@exception QuadrupleIteratorBMException exception from bufmgr class 
   */
  public void free_buffer_pages(int n_pages, PageId[] PageIds) 
    throws QuadrupleIteratorBMException
    {
      for (int i=0; i<n_pages; i++) {
	freePage(PageIds[i]);
      }
    }

  private void freePage(PageId pageno)
    throws QuadrupleIteratorBMException {
    
    try {
      SystemDefs.JavabaseBM.freePage(pageno);
    }
    catch (Exception e) {
      throw new QuadrupleIteratorBMException(e,"Iterator.java: freePage() failed");
    }
    
  } // end of freePage

  private PageId newPage(Page page, int num)
    throws QuadrupleIteratorBMException {
    
    PageId tmpId = new PageId();
    
    try {
      tmpId = SystemDefs.JavabaseBM.newPage(page,num);
    }
    catch (Exception e) {
      throw new QuadrupleIteratorBMException(e,"Iterator.java: newPage() failed");
    }

    return tmpId;

  } // end of newPage
}
