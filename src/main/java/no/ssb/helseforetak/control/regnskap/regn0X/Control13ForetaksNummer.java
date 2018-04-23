package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: Control13ForetaksNummer.java,v $
 * Revision 1.3  2009/10/03 12:26:05  pll
 * Versjon: 2009-rapporteringen
 *
 * Revision 1.2  2007/10/25 11:33:57  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.2  2006/11/27 10:16:55  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:07  pll
 * Import av 2005-filer
 *
 * Revision 1.2  2005/12/14 13:04:21  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class Control13ForetaksNummer extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 13, Kontroll av at posisjon " +
                                    "23 - 31 ikke er fylt ut med 000000000:";
  private Vector<Integer> recordNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String foretaksNum = RecordFields.getForetaksNummer(line);
    boolean lineHasError = foretaksNum.equalsIgnoreCase("000000000");
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
      errorReport += "\tFeil: Posisjonene 23-31 skal ikke rapporteres " +
      "som 000000000." + lf + "\tFor nærmere informasjon " +
      "se Håndbok for rapportering av regnskapsdata for " +
      "helseforetak og regionale helseforetak "+Constants.kostraYear+"." + lf;
          
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
    return Constants.NORMAL_ERROR;
  }
}
