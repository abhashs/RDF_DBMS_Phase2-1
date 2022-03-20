package quadrupleIterator;
import chainexception.*;

import java.lang.*;

public class QuadrupleJoinsException extends ChainException {
  public QuadrupleJoinsException(String s){super(null,s);}
  public QuadrupleJoinsException(Exception prev, String s){ super(prev,s);}
}
