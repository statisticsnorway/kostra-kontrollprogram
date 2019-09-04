package no.ssb.kostra.control.regnskap.regn0P;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlOrgNummer extends no.ssb.kostra.control.Control
{
  private Vector<Integer> recordNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String orgNum = no.ssb.kostra.control.regnskap.regn0P.RecordFields.getOrgNummer (line);

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
      errorReport += lf + "\tKorreksjon: Skjema 0P skal ikke ha organisasjonsnummer i felt 5. Legg inn blanke/mellomrom i felt 5." + lf + lf;
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