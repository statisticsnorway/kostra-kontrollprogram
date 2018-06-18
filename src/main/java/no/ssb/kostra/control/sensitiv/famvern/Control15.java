package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: Control15.java,v $
 * Revision 1.3  2008/12/13 06:45:51  pll
 * no message
 *
 * Revision 1.2  2007/10/25 11:37:03  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:47  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:25  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.6  2006/01/05 08:16:30  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control15 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K15: Primærklientens samlivsstatus.";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field_9 = RecordFields.getFieldValue(line, 91);

    boolean lineHasError; 

    try {
        int kode = Integer.parseInt(field_9);
        lineHasError = kode<1 || kode>4;
    } catch (NumberFormatException e) {
        lineHasError = true;
    }
  
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
      errorReport += "\tFeil: i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + 
      " er primærklientens samlivsstatus ved sakens opprettelse ikke fylt ut " + lf +
      "\teller feil kode er benyttet. Feltet er obligatorisk."; 
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
    return Constants.NORMAL_ERROR;
  }
}
