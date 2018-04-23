package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: ControlRecordlengde.java,v $
 * Revision 1.6  2010/02/05 07:00:26  pll
 * Recordlengde = 337.
 *
 * Revision 1.5  2010/02/01 14:04:12  pll
 * Endret recordlengde.
 *
 * Revision 1.4  2009/06/18 10:18:11  pll
 * Endret til kritisk kontrolltype.
 *
 * Revision 1.3  2008/12/11 09:50:20  pll
 * Oppdatert recordlengde.
 *
 * Revision 1.2  2007/10/25 11:37:04  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:49  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:28  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.6  2006/01/05 08:16:33  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlRecordlengde extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K1: Recordlengde";
  private Vector<Integer> lineNumbers = new Vector<Integer>();
  private final int RECORD_LENGTH = 151;

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
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: feil antall posisjoner i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + "."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + lineNumbers.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return lineNumbers.size() > 0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}
