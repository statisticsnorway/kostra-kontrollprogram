package no.ssb.kostra.control.sensitiv.soskval;

/**
 * $Log: ControlAargang.java,v $
 * Revision 1.1  2009/09/30 08:31:45  pll
 * Import.
 *
 * 
 */

import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlAargang extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K4: Oppgaveår";
  private Vector<Integer> recordsWithError = new Vector<Integer>();
  private String aargang = Constants.kostraYear.substring(2); 

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String oppgaveaar = RecordFields.getOppgaveaar(line);

    boolean lineHasError = ! oppgaveaar.equalsIgnoreCase(aargang);

    if (lineHasError)
    {
      recordsWithError.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    int numOfRecords = recordsWithError.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: Årgangen som angis på " + numOfRecords + " av totalt " + totalLineNumber + 
          " records stemmer ikke overens" + lf + "\tmed det som er gjeldende oppgaveår," +
          " sjekk at riktig fil benyttes." + lf;
      
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + recordsWithError.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return recordsWithError.size() > 0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}