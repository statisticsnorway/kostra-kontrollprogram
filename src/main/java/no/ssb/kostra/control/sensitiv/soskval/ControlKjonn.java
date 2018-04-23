package no.ssb.kostra.control.sensitiv.soskval;

/*
 * $Log: ControlKjonn.java,v $
 * Revision 1.1  2009/09/30 08:40:49  pll
 * Import.
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlKjonn extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K8: Kjønn";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String kjonn = RecordFields.getKjonn(line);
  
    boolean lineHasError = ! (kjonn.equalsIgnoreCase("1") || 
                              kjonn.equalsIgnoreCase("2"));

    if (lineHasError)
    {
      linesWithError.add(new Integer(lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: Deltakerens kjønn er ikke fylt ut, " +
      "eller feil kode" + lf + "\ter benyttet i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + ". Feltet er obligatorisk."; 
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
    return Constants.CRITICAL_ERROR;
  }
}