package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 * $Log: ControlGruppeAvslutningFoerGruppeStart.java,v $
 * Revision 1.3  2009/02/26 19:05:57  pll
 * Bugfix.
 *
 * Revision 1.2  2009/02/26 09:21:04  pll
 * Dati kan være like.
 *
 * Revision 1.1  2009/01/06 14:27:58  pll
 * Import.
 *
 * Revision 1.1  2009/01/06 14:17:12  pll
 * Import.
 *
 * Revision 1.1  2009/01/06 11:39:41  pll
 * Import.
 *
 * Revision 1.3  2008/12/13 08:43:52  pll
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
import no.ssb.kostra.utils.*;

public final class ControlGruppeAvslutningFoerGruppeStart extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K35: Dato for gruppens avslutning kommer før gruppens oppstart";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    String field_28 = RecordFields.getFieldValue(line, 28); 
    
    if (field_28.equalsIgnoreCase("1") || field_28.equalsIgnoreCase("2")) {
      //Gruppen er avsluttet.
      
      String datoGruppestart = RecordFields.getFieldValue(line, 5);
      String datoGruppeslutt = RecordFields.getFieldValue(line, 30);
      
      try {
        lineHasError = ! Toolkit.isFirstDateBeforeLastDateOrEqual_DDMMYYYY(datoGruppestart, datoGruppeslutt);
      } catch (Exception e) {}    
    }    
        
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
      " kommer dato for avslutning av gruppen før dato for gruppebehandlingens start."; 
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
