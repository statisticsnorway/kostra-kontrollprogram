package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 * $Log: ControlAvslDatoFoerForsteGruppeMote.java,v $
 * Revision 1.3  2009/02/26 09:20:50  pll
 * Dati kan være like.
 *
 * Revision 1.2  2009/02/12 08:56:18  pll
 * Kontrollen gir nå IKKE utslag p.g.a. feil datoformat o.l.
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

public final class ControlAvslDatoFoerForsteGruppeMote extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K21: Avslutningsdato før første samtale";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String datoForsteGruppemote = RecordFields.getFieldValue(line, 5);
    String datoSisteGruppemote = RecordFields.getFieldValue(line, 14);

    boolean lineHasError;
    
    try {
      lineHasError = ! Toolkit.isFirstDateBeforeLastDateOrEqual_DDMMYYYY(datoForsteGruppemote, datoSisteGruppemote);
    } catch (Exception e) {
      lineHasError = false;
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
      " dato for avslutning av gruppebehandlingen kommer før dato for gruppebehandlingens start."; 
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
