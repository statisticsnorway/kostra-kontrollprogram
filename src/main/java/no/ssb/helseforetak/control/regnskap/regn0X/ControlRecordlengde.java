package no.ssb.helseforetak.control.regnskap.regn0X;


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlRecordlengde extends no.ssb.kostra.control.Control 
{
  private final String ERROR_TEXT = "Kontroll 0, Recordlengde:";
  private Vector<Integer> lineNumbers = new Vector<Integer>();
  private final int RECORD_LENGTH = 48;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = line.length() != RECORD_LENGTH;
  
    if (lineHasError)
    {
      lineNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: feil antall posisjoner i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + "." + lf +
      "\tHver record skal være på 48 posisjoner. " + lf + 
      "\tNB! Records med feil lengde tas ikke med i videre kontroller." + lf +
      "\tDette kan medføre at ytterligere feil oppstår." + lf;
      
      if (numOfRecords <= 10)
      {
        errorReport += "\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + lineNumbers.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return lineNumbers.size() > 0;
  }  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}