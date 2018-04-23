package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: Control_21.java,v $
 * Revision 1.1  2009/10/13 12:17:14  pll
 * Import.
 *
 *
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.sensitiv.InvalidFnrException;
import no.ssb.kostra.utils.*;

public final class Control_21 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private int LIMIT = 18; 
  private final String ERROR_TEXT = 
      "K21: Alder under "+LIMIT+" år";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError;
    
    String fnr = RecordFields.getFieldValue(line, 6);
    try {
      int alder = Toolkit.getAlderFromFnr(fnr);
      lineHasError = alder < LIMIT;
    } catch (InvalidFnrException e) {
      lineHasError = false;
    }
    
    if (lineHasError)
      linesWithError.add(new Integer(lineNumber));

    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + 
          " er mottaker under " + LIMIT + " år."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + linesWithError.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return linesWithError.size() > 0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}