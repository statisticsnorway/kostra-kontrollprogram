package no.ssb.kostra.control.regnskap.regn0C;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlKvartal extends no.ssb.kostra.control.Control
{
  private Vector<Integer> recordNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String kvartal = RecordFields.getKvartal (line);

    boolean lineHasError = ! kvartal.equalsIgnoreCase(" ");

    if (lineHasError)
    {
      recordNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 3, kvartal:" + lf + lf;
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0)
    {
      errorReport += "\t Feil: For årsregnskapet skal posisjon 7 være blank. " + lf + 
          "\t\t" + numOfRecords + " av " + totalLineNumber + " records har utfylt kvartal.";
      if (numOfRecords <= 10)
      {
        errorReport += "(Gjelder følgende records:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + recordNumbers.elementAt(i) + (i>0 ? "" : ", ");
        }
        errorReport += ")";
      }
      errorReport += lf + "\tKorreksjon: Rett opp i fila slik at posisjon 7 er blank." + lf + lf;
    }
    return errorReport;
  }

  public boolean foundError()
  {
    return recordNumbers.size() > 0;
  }  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}