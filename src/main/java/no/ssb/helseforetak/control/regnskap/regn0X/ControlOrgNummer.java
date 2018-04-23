package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: ControlOrgNummer.java,v $
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:06  pll
 * Import av 2005-filer
 *
 * Revision 1.2  2005/12/14 13:04:22  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlOrgNummer extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 4, Organisasjonsnummer:";
  private Vector<Integer> recordNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String orgNum = RecordFields.getOrgNummer (line);

    boolean lineHasError = 
        ! no.ssb.helseforetak.utils.OrgNummer.orgNrIsValid(orgNum);

    if (lineHasError)
    {
      recordNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + lf + lf;
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: Det er benyttet feil organisasjonsnummer i " + 
          numOfRecords + " av " + totalLineNumber + " records." + lf +
          "\tSjekk at organisasjonsnummeret er korrekt." + lf;
          
      if (numOfRecords <= 10)
      {
        errorReport += "\t\t(Gjelder record nr.:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + recordNumbers.elementAt(i);
        }
        errorReport += ")" + lf;
      }
      errorReport += lf + lf;
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
