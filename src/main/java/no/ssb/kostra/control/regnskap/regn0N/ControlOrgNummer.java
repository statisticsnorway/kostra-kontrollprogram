package no.ssb.kostra.control.regnskap.regn0N;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlOrgNummer extends no.ssb.kostra.control.Control
{
  private Vector<Integer> recordNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String orgNum = RecordFields.getOrgNummer (line);

    boolean lineHasError = ! orgNum.equalsIgnoreCase("         ");

    if (lineHasError)
    {
      recordNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 5, Organisasjonsnummer:" + lf + lf;
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0)
    {
      errorReport += "\t Feil: Filen inneholder organisasjonsnummer. " + lf +
          "\t\t" + numOfRecords + " av " + totalLineNumber + " records har utfylt organisasjonsnummer." + lf;
      if (numOfRecords <= 10)
      {
        errorReport += "\t\t(Gjelder følgende records:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + recordNumbers.elementAt(i);
        }
        errorReport += ")";
      }
      errorReport += lf + "\tKorreksjon: Rett opp i fila slik at skjema 0N ikke har organisasjonsnummer i felt 5." + lf + lf;
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