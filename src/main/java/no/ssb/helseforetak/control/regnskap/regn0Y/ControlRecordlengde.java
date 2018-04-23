package no.ssb.helseforetak.control.regnskap.regn0Y;

/**
 * $Log: ControlRecordlengde.java,v $
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
 * Revision 1.3  2006/01/02 09:58:17  lwe
 * endret etter mail-forespørsel fra Siri Bogen: feilmeldinger i K0, K11, K12
 *
 * Revision 1.2  2005/12/14 13:04:23  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlRecordlengde extends no.ssb.kostra.control.Control 
{
  private final String ERROR_TEXT = "Kontroll 0, Recordlengde:";
  private Vector<Integer> lineNumbers = new Vector<Integer>();
  private final int RECORD_LENGTH = 48;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = line.length() != RECORD_LENGTH;
  
    if (lineHasError)
    {
      lineNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    StringBuilder errorReport = new StringBuilder (ERROR_TEXT + lf + lf);
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport.append("\tFeil: feil antall posisjoner i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + "." + lf +
      "\tHver record skal være på 48 posisjoner. " + lf + 
      "\tNB! Records med feil lengde tas ikke med i videre kontroller." + lf +
      "\tDette kan medføre at ytterligere feil oppstår." + lf);
      
      if (numOfRecords <= 10)
      {
        errorReport.append("\t(Gjelder record nr.");
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport.append(" " + lineNumbers.elementAt(i));
        }
        errorReport.append(").");
      }
    }
    errorReport.append(lf + lf);
    return errorReport.toString();
  }

  public boolean foundError()
  {
    return lineNumbers.size() > 0;
  }  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}