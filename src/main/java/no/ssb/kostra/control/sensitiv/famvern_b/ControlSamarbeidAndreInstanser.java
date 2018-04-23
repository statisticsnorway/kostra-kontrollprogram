package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 * $Log: ControlSamarbeidAndreInstanser.java,v $
 * Revision 1.1  2009/01/06 13:58:22  pll
 * Import.
 *
 * Revision 1.1  2009/01/06 09:48:39  pll
 * Import.
 *
 * Revision 1.3  2008/12/13 09:15:52  pll
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

public final class ControlSamarbeidAndreInstanser extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K31: Samarbeid mellom gruppen og andre instanser";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    StringBuilder field_27 = new StringBuilder();
    
    for (int i=1; i<15; i++) 
        field_27.append(RecordFields.getFieldValue(line,(i>9?2700+i:270+i)));
    
    boolean lineHasError = field_27.indexOf("1") == -1; 
    
    if (lineHasError)
      lineNumbers.add (new Integer (lineNumber));
    
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
      " er det ikke krysset av for om det har vært samarbeid med andre instanser" + lf + 
      "\tsiden saken ble opprettet. Feltet er obligatorisk å fylle ut."; 
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
