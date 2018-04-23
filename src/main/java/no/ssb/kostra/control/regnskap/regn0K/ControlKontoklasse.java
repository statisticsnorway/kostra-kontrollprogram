package no.ssb.kostra.control.regnskap.regn0K;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlKontoklasse extends no.ssb.kostra.control.Control
{
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String kontoklasse = RecordFields.getKontoklasse (line);

    boolean lineHasError = ! (kontoklasse.equalsIgnoreCase("3") || 
                              kontoklasse.equalsIgnoreCase("4"));
    if (lineHasError)
    {
      lineNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 6, kontoklasse:" + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: ukjent kontoklasse i " + numOfRecords + " record" + (numOfRecords == 1 ? "" : "s") + "."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + lineNumbers.elementAt(i) + (i>0 ? "" : ", ");
        }
        errorReport += ").";
      }
    }
    errorReport += lf + "\tKorreksjon: Rett kontoklasse. Kontoklasse 3 og 4 er gyldige for bevilgningsregnskapet. " + lf + lf;
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
