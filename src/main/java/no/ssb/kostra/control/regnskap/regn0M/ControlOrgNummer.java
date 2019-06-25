package no.ssb.kostra.control.regnskap.regn0M;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlOrgNummer extends no.ssb.kostra.control.Control
{
  private Vector<Integer> recordNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String orgNum = no.ssb.kostra.control.regnskap.regn0M.RecordFields.getOrgNummer (line);

    boolean lineHasError = ! orgNum.equalsIgnoreCase("         ");

    if (lineHasError)
    {
      recordNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 5.2, organisasjonsnummer:" + lf + lf;
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0)
    {
      errorReport += "\t Feil: Filen inneholder organisasjonsnummer. " + lf +
          "\t" + numOfRecords + " av " + totalLineNumber + " records har utfylt organisasjonsnummer." + lf;
      if (numOfRecords <= 10)
      {
        errorReport += "\t(Gjelder følgende records:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + recordNumbers.elementAt(i);
        }
        errorReport += ")";
      }
      errorReport += lf + "\tKorreksjon: Rett opp i fila slik at skjema 0M ikke har organisasjonsnummer i felt 5." + lf + lf;
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