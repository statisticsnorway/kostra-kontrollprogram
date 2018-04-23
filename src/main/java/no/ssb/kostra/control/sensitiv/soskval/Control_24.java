package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_24 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K24: Antall møter (personlige samtaler og gruppesamtaler) med " +
      "NAV-veileder i løpet av "+ Constants.kostraYear;
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field = RecordFields.getFieldValue(line, 18);
    boolean lineHasError;
    
    try {
      int ant = Integer.parseInt(field);
      lineHasError = ant < 1;
    } catch (NumberFormatException e) {
      lineHasError = true;      
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
      errorReport += lf + "\tFeil (i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "): Feltet for" + lf +
          "\t\"Hvor mange ganger i løpet av programperioden i "+Constants.kostraYear+ " har " +
          "deltakeren hatt møter" + lf + "\t(personlige samtaler, telefonsamtaler " +
          "og gruppesamtaler) med veileder fra NAV?\"" + lf +
          "\ter ikke fylt ut. Feltet er obligatorisk å fylle ut."; 
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