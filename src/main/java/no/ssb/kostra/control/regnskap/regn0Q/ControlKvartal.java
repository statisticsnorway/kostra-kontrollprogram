package no.ssb.kostra.control.regnskap.regn0Q;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlKvartal extends no.ssb.kostra.control.Control
{
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String kvartal = RecordFields.getKvartal (line);

    boolean lineHasError = ! kvartal.equalsIgnoreCase(" ");
    
    if (lineHasError)
    {
      lineNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 10, kvartal:" + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: kvartal (posisjon 7) er ikke blank i " + 
          numOfRecords + " record" + (numOfRecords == 1 ? "" : "s") + "."; 
      if (numOfRecords <= 10)
      {
        errorReport += " (Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + lineNumbers.elementAt(i) + (i>0 ? "" : ", ");
        }
        errorReport += ").";
      }
    }
    errorReport += lf + "\tKorreksjon: Rett opp i fila slik at posisjon 7 er blank." + lf + lf;
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