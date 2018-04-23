package no.ssb.helseforetak.control.regnskap.regn0Y;

/**
 * $Log: ControlOrgNummer.java,v $
 * Revision 1.3  2009/04/06 11:49:40  pll
 * String -> StringBuilder
 *
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:23  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:07  pll
 * Import av 2005-filer
 *
 * Revision 1.2  2005/12/14 13:04:23  lwe
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
    StringBuilder errorReport = new StringBuilder (ERROR_TEXT + lf + lf);
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0)
    {
      errorReport.append("\tFeil: Det er benyttet feil organisasjonsnummer i " + 
          numOfRecords + " av " + totalLineNumber + " records." + lf +
          "\tSjekk at organisasjonsnummeret er korrekt." + lf);
          
      if (numOfRecords <= 10)
      {
        errorReport.append("\t\t(Gjelder record nr.:");
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport.append(" " + recordNumbers.elementAt(i));
        }
        errorReport.append(")" + lf);
      }
      errorReport.append(lf + lf);
    }
    return errorReport.toString();
  }

  public boolean foundError()
  {
    return recordNumbers.size() > 0;
  }  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}
