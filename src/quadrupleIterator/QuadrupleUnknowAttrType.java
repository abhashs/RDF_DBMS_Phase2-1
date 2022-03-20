package quadrupleIterator;

import java.lang.*;
import chainexception.*;

public class QuadrupleUnknowAttrType extends ChainException {
  public QuadrupleUnknowAttrType(String s){super(null,s);}
  public QuadrupleUnknowAttrType(Exception prev, String s){super(prev,s);}
}
