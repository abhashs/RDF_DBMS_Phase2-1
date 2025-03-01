/* File Quadruple.java */

package quadrupleheap;

import java.io.*;
import java.lang.*;
import global.*;


public class Quadruple implements GlobalConst{

  static LID getLIDfromByteArray(byte[] array, int position) throws IOException{
    PageId pageNo = new PageId(Convert.getIntValue(position, array));
    int slotNo = Convert.getIntValue(position + 4, array);
    LID newLID = new LID(pageNo, slotNo);
    return newLID;
  }

 /** 
  * Maximum size of any tuple
  */
  public static final int max_size = MINIBASE_PAGESIZE;

 /** 
   * a byte array to hold data
   */
  private byte [] data;

  /**
   * start position of this tuple in data[]
   */
  private int quadruple_offset;

  /**
   * length of this tuple
   */
  private int quadruple_length;

  /** 
   * private field
   * Number of fields in this tuple
   */
  private short fldCnt;

  /** 
   * private field
   * Array of offsets of the fields
   */
 
  private short [] fldOffset; 

   /**
    * Class constructor
    * Creat a new tuple with length = max_size,tuple offset = 0.
    */

  private AttrType valueType = new AttrType(AttrType.attrReal);
  private EID Subject; //8 bytes
  private PID Predicate; //8 bytes
  private EID Object; //8 bytes
  private float Value; //4 bytes

  public Quadruple()
  {
       // Creat a new tuple
       //! Maybe change to max_size
       data = new byte[QUADRUPLE_SIZE];
      //  data = new byte[max_size];
       quadruple_offset = 0;
       quadruple_length = QUADRUPLE_SIZE;
       Subject = new EID();
       Predicate = new PID();
       Object = new EID();
       Value = (float) 0.0; //!
  }
   
   /** Constructor
    * @param atuple a byte array which contains the tuple
    * @param offset the offset of the tuple in the byte array
    * @param length the length of the tuple
    */

  public Quadruple(byte[] aquadruple, int offset) throws IOException {
      data = aquadruple;
      quadruple_offset = offset;
      quadruple_length = QUADRUPLE_SIZE;

      setSubjectID(Quadruple.getLIDfromByteArray(aquadruple, offset + 0).returnEID());
      setPredicateID(Quadruple.getLIDfromByteArray(aquadruple, offset + 8).returnPID());
      setObjectID(Quadruple.getLIDfromByteArray(aquadruple, offset + 16).returnEID());
      setConfidence(Convert.getFloValue(offset + 24, aquadruple));
  }

   public Quadruple(byte [] aquadruple, int offset, int size) throws IOException
   {
      data = aquadruple;
      quadruple_offset = offset;
      quadruple_length = size;

      if (size >= 28){
        setSubjectID(Quadruple.getLIDfromByteArray(aquadruple, offset + 0).returnEID());
        setPredicateID(Quadruple.getLIDfromByteArray(aquadruple, offset + 8).returnPID());
        setObjectID(Quadruple.getLIDfromByteArray(aquadruple, offset + 16).returnEID());
        setConfidence(Convert.getFloValue(offset + 24, aquadruple));
      }

   }
  
   /** Constructor(used as tuple copy)
    * @param fromTuple   a byte array which contains the tuple
    * 
    */
   public Quadruple(Quadruple fromQuadruple) throws IOException
   {
      data = fromQuadruple.getQuadrupleByteArray();
      quadruple_length = fromQuadruple.getLength();
      quadruple_offset = 0;
      
      setSubjectID(fromQuadruple.getSubjectID());
      setPredicateID(fromQuadruple.getPredicateID());
      setObjectID(fromQuadruple.getObjectID());
      setConfidence(fromQuadruple.getConfidence());
   }


  //! Maybe change these to use byte arrays? If this doesn't work
  public EID getSubjectID(){
    return Subject;
  }

  public PID getPredicateID(){
    return Predicate;
  }

  public EID getObjectID(){
    return Object;
  }

  public float getConfidence(){
    return Value;
  }

  public void setSubjectID(EID subjectQID) throws IOException{
    Subject = subjectQID;
    Subject.writeToByteArray(data, quadruple_offset + 0);
  }

  public void setPredicateID(PID predicateID) throws IOException{
    Predicate = predicateID;
    Predicate.writeToByteArray(data, quadruple_offset + 8);
  }

  public void setObjectID(EID objectQID) throws IOException{
    Object = objectQID;
    Object.writeToByteArray(data, quadruple_offset + 16);
  }

  public void setConfidence(float confidence) throws IOException{
    Value = confidence;
    Convert.setFloValue(confidence, quadruple_offset + 24, data);
  }
   
   /** Copy a tuple to the current tuple position
    *  you must make sure the tuple lengths must be equal
    * @param fromTuple the tuple being copied
    */
   public void quadrupleCopy(Quadruple fromQuadruple)
   {
       byte [] temparray = fromQuadruple.getQuadrupleByteArray();
       System.arraycopy(temparray, 0, data, quadruple_offset, quadruple_length);   
       Subject = fromQuadruple.getSubjectID();
       Predicate = fromQuadruple.getPredicateID();
       Object = fromQuadruple.getObjectID();
       Value = fromQuadruple.getConfidence();
//       fldCnt = fromTuple.noOfFlds(); 
//       fldOffset = fromTuple.copyFldOffset(); 
   }

   /** This is used when you don't want to use the constructor
    * @param atuple  a byte array which contains the tuple
    * @param offset the offset of the tuple in the byte array
    * @param length the length of the tuple
    */

   public void quadrupleInit(byte [] aquadruple, int offset) throws IOException
   {
      data = aquadruple;
      quadruple_offset = offset;
      quadruple_length = QUADRUPLE_SIZE;

      setSubjectID(Quadruple.getLIDfromByteArray(aquadruple, offset + 0).returnEID());
      setPredicateID(Quadruple.getLIDfromByteArray(aquadruple, offset + 8).returnPID());
      setObjectID(Quadruple.getLIDfromByteArray(aquadruple, offset + 16).returnEID());
      setConfidence(Convert.getFloValue(offset + 24, aquadruple));
   }

 /**
  * Set a tuple with the given tuple length and offset
  * @param	record	a byte array contains the tuple
  * @param	offset  the offset of the tuple ( =0 by default)
  * @param	length	the length of the tuple
  */
 public void quadrupleSet(byte[] fromQuadruple, int offset) throws IOException
  {
      // System.arraycopy(fromQuadruple, offset, data, 0, quadruple_length);
      System.arraycopy(fromQuadruple, offset, data, 0, QUADRUPLE_SIZE);
      quadruple_offset = 0;
      quadruple_length = QUADRUPLE_SIZE;

//      setSubjectID(Quadruple.getLIDfromByteArray(fromQuadruple, offset + 0).returnEID());
//      setPredicateID(Quadruple.getLIDfromByteArray(fromQuadruple, offset + 8).returnPID());
//      setObjectID(Quadruple.getLIDfromByteArray(fromQuadruple, offset + 16).returnEID());
//      setConfidence(Convert.getFloValue(offset + 24, fromQuadruple));
  }
 
// public void quadrupleSet(byte[] fromQuadruple, int offset, int size) throws IOException
// {
//     // System.arraycopy(fromQuadruple, offset, data, 0, quadruple_length);
//     System.arraycopy(fromQuadruple, offset, data, 0, QUADRUPLE_SIZE);
//     quadruple_offset = 0;
//     quadruple_length = size;
// }
  
 /** get the length of a tuple, call this method if you did not 
  *  call setHdr () before
  * @return 	length of this tuple in bytes
  */   
  public int getLength()
   {
      return quadruple_length;
   }

/** get the length of a tuple, call this method if you did 
  *  call setHdr () before
  * @return     size of this tuple in bytes
  */
  public short size()
   {
     return (short)quadruple_length;
      // return ((short) (fldOffset[fldCnt] - quadruple_offset));
   }
 
   /** get the offset of a tuple
    *  @return offset of the tuple in byte array
    */   
   public int getOffset()
   {
      return quadruple_offset;
   }   
   
   /** Copy the tuple byte array out
    *  @return  byte[], a byte array contains the tuple
    *		the length of byte[] = length of the tuple
    */
    
  //  public byte [] getTupleByteArray() 
  //  {
  //      byte [] tuplecopy = new byte [tuple_length];
  //      System.arraycopy(data, tuple_offset, tuplecopy, 0, tuple_length);
  //      return tuplecopy;
  //  }

   public byte[] getQuadrupleByteArray(){
    byte[] quadruplecopy = new byte[quadruple_length];
    System.arraycopy(data, quadruple_offset, quadruplecopy, 0, quadruple_length);
    return quadruplecopy;
   }
   
   /** return the data byte array 
    *  @return  data byte array 		
    */
    
   public byte [] returnTupleByteArray()
   {
       return data;
   }
   
   /**
    * Convert this field into integer 
    * 
    * @param	fldNo	the field number
    * @return		the converted integer if success
    *			
    * @exception   IOException I/O errors
    * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
    */

  public int getIntFld(int fldNo) 
  	throws IOException, FieldNumberOutOfBoundException
  {           
    int val;
    if ( (fldNo > 0) && (fldNo <= fldCnt))
     {
      val = Convert.getIntValue(fldOffset[fldNo -1], data);
      return val;
     }
    else 
     throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
  }
    
   /**
    * Convert this field in to float
    *
    * @param    fldNo   the field number
    * @return           the converted float number  if success
    *			
    * @exception   IOException I/O errors
    * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
    */

    public float getFloFld(int fldNo) 
    	throws IOException, FieldNumberOutOfBoundException
     {
	float val;
      if ( (fldNo > 0) && (fldNo <= fldCnt))
       {
        val = Convert.getFloValue(fldOffset[fldNo -1], data);
        return val;
       }
      else 
       throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
     }


   /**
    * Convert this field into String
    *
    * @param    fldNo   the field number
    * @return           the converted string if success
    *			
    * @exception   IOException I/O errors
    * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
    */

   public String getStrFld(int fldNo) 
   	throws IOException, FieldNumberOutOfBoundException 
   { 
         String val;
    if ( (fldNo > 0) && (fldNo <= fldCnt))      
     {
        val = Convert.getStrValue(fldOffset[fldNo -1], data, 
		fldOffset[fldNo] - fldOffset[fldNo -1]); //strlen+2
        return val;
     }
    else 
     throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
  }
 
   /**
    * Convert this field into a character
    *
    * @param    fldNo   the field number
    * @return           the character if success
    *			
    * @exception   IOException I/O errors
    * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
    */

   public char getCharFld(int fldNo) 
   	throws IOException, FieldNumberOutOfBoundException 
    {   
       char val;
      if ( (fldNo > 0) && (fldNo <= fldCnt))      
       {
        val = Convert.getCharValue(fldOffset[fldNo -1], data);
        return val;
       }
      else 
       throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
 
    }

  /**
   * Set this field to integer value
   *
   * @param	fldNo	the field number
   * @param	val	the integer value
   * @exception   IOException I/O errors
   * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
   */

  public Quadruple setIntFld(int fldNo, int val) 
  	throws IOException, FieldNumberOutOfBoundException
  { 
    if ( (fldNo > 0) && (fldNo <= fldCnt))
     {
	Convert.setIntValue (val, fldOffset[fldNo -1], data);
	return this;
     }
    else 
     throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND"); 
  }

  /**
   * Set this field to float value
   *
   * @param     fldNo   the field number
   * @param     val     the float value
   * @exception   IOException I/O errors
   * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
   */

  public Quadruple setFloFld(int fldNo, float val) 
  	throws IOException, FieldNumberOutOfBoundException
  { 
   if ( (fldNo > 0) && (fldNo <= fldCnt))
    {
     Convert.setFloValue (val, fldOffset[fldNo -1], data);
     return this;
    }
    else  
     throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND"); 
     
  }

  /**
   * Set this field to String value
   *
   * @param     fldNo   the field number
   * @param     val     the string value
   * @exception   IOException I/O errors
   * @exception   FieldNumberOutOfBoundException Tuple field number out of bound
   */

   public Quadruple setStrFld(int fldNo, String val) 
		throws IOException, FieldNumberOutOfBoundException  
   {
     if ( (fldNo > 0) && (fldNo <= fldCnt))        
      {
         Convert.setStrValue (val, fldOffset[fldNo -1], data);
         return this;
      }
     else 
       throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }


   /**
    * setHdr will set the header of this tuple.   
    *
    * @param	numFlds	  number of fields
    * @param	types[]	  contains the types that will be in this tuple
    * @param	strSizes[]      contains the sizes of the string 
    *				
    * @exception IOException I/O errors
    * @exception InvalidTypeException Invalid tupe type
    * @exception InvalidTupleSizeException Tuple size too big
    *
    */

public void setHdr (short numFlds,  AttrType types[], short strSizes[])
 throws IOException, InvalidTypeException, InvalidTupleSizeException		
{
  if((numFlds +2)*2 > max_size)
    throw new InvalidTupleSizeException (null, "TUPLE: TUPLE_TOOBIG_ERROR");
  
  fldCnt = numFlds;
  Convert.setShortValue(numFlds, quadruple_offset, data);
  fldOffset = new short[numFlds+1];
  int pos = quadruple_offset+2;  // start position for fldOffset[]
  
  //sizeof short =2  +2: array siaze = numFlds +1 (0 - numFilds) and
  //another 1 for fldCnt
  fldOffset[0] = (short) ((numFlds +2) * 2 + quadruple_offset);   
   
  Convert.setShortValue(fldOffset[0], pos, data);
  pos +=2;
  short strCount =0;
  short incr;
  int i;

  for (i=1; i<numFlds; i++)
  {
    switch(types[i-1].attrType) {
    
   case AttrType.attrInteger:
     incr = 4;
     break;

   case AttrType.attrReal:
     incr =4;
     break;

   case AttrType.attrString:
     incr = (short) (strSizes[strCount] +2);  //strlen in bytes = strlen +2
     strCount++;
     break;       
 
   default:
    throw new InvalidTypeException (null, "TUPLE: TUPLE_TYPE_ERROR");
   }
  fldOffset[i]  = (short) (fldOffset[i-1] + incr);
  Convert.setShortValue(fldOffset[i], pos, data);
  pos +=2;
 
}
 switch(types[numFlds -1].attrType) {

   case AttrType.attrInteger:
     incr = 4;
     break;

   case AttrType.attrReal:
     incr =4;
     break;

   case AttrType.attrString:
     incr =(short) ( strSizes[strCount] +2);  //strlen in bytes = strlen +2
     break;

   default:
    throw new InvalidTypeException (null, "TUPLE: TUPLE_TYPE_ERROR");
   }

  fldOffset[numFlds] = (short) (fldOffset[i-1] + incr);
  Convert.setShortValue(fldOffset[numFlds], pos, data);
  
  quadruple_length = fldOffset[numFlds] - quadruple_offset;

  if(quadruple_length > max_size)
   throw new InvalidTupleSizeException (null, "TUPLE: TUPLE_TOOBIG_ERROR");
}
     
  
  /**
   * Returns number of fields in this tuple
   *
   * @return the number of fields in this tuple
   *
   */

  public short noOfFlds() 
   {
     return fldCnt;
   }

  /**
   * Makes a copy of the fldOffset array
   *
   * @return a copy of the fldOffset arrray
   *
   */

  public short[] copyFldOffset() 
   {
     short[] newFldOffset = new short[fldCnt + 1];
     for (int i=0; i<=fldCnt; i++) {
       newFldOffset[i] = fldOffset[i];
     }
     
     return newFldOffset;
   }

 /**
  * Print out the tuple
  * @param type  the types in the tuple
  * @Exception IOException I/O exception
  */
 public void print()
    throws IOException 
 {
  int i, val;
  float fval;
  String sval;


  System.out.print("[ " + Subject.slotNo + ", " + Subject.pageNo + ", ");
  System.out.print(Predicate.slotNo + ", " + Predicate.pageNo + ", ");
  System.out.print(Object.slotNo + ", " + Object.pageNo + ", ");
  System.out.println(Value + "]");
 }

  /**
   * private method
   * Padding must be used when storing different types.
   * 
   * @param	offset
   * @param type   the type of tuple
   * @return short typle
   */

  private short pad(short offset, AttrType type)
   {
      return 0;
   }
}

