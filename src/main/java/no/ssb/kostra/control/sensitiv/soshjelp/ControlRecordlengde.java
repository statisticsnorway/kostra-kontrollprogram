package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlRecordlengde.java,v $
 * Revision 1.5  2009/09/16 11:55:45  pll
 * Ny recordlengde.
 *
 * Revision 1.4  2008/09/04 13:14:12  pll
 * 2008-rapportering.
 *
 * Revision 1.3  2007/10/25 09:11:53  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/09/24 11:39:34  pll
 * Endret recordlengde fra 76 til 79 for 2007-rapporteringen.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.3  2006/09/26 14:13:15  lwe
 * et nytt felt med lengde 1 er lagt til, recordlengde blir 76
 *
 * Revision 1.2  2006/09/22 09:13:50  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:29  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.3  2006/01/05 08:31:52  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.2  2006/01/05 08:16:35  lwe
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
  private final int RECORD_LENGTH = 314;

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
