package no.ssb.kostra.control.regnskap.regn0M;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlBelopLik0 extends no.ssb.kostra.control.Control
{
  private Vector<Integer> recordNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {  
    int belop = RecordFields.getBelopIntValue(line);

    if (belop == 0)
    {
      recordNumbers.add (new Integer (lineNumber));
    }
    return false;
  }

  public String getErrorReport (int totalLineNumber)
  {
//    String errorReport = "Kontroll 20, kvartal:" + lf + lf;
	String errorReport = "Beløp er lik 0:" + lf + lf;
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0)
    {
//      errorReport += "\t Feil: For årsregnskapet skal posisjon 7 være blank. " + lf + 
//          "\t\t" + numOfRecords + " av " + totalLineNumber + " records har utfylt kvartal.";
        errorReport += "\t Advarsel: Beløp i rekorden er lik tallet null (0) " + lf + 
        "\t\t" + numOfRecords + " av " + totalLineNumber + " records har utfylt beløp lik 0.";
      if (numOfRecords <= 10)
      {
        errorReport += "(Gjelder følgende records:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + recordNumbers.elementAt(i);
        }
        errorReport += ")";
      }
      errorReport += lf + "\tKorreksjon: Fjern eller sjekk om beløp er riktig.";
      errorReport += lf + lf;
    }
    return errorReport;
  }

  public boolean foundError()
  {
    return recordNumbers.size() > 0;
  }  

  public int getErrorType() {
    return Constants.NO_ERROR;
  }
}