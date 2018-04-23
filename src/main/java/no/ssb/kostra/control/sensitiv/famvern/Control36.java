package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: Control36.java,v $
 * Revision 1.6  2009/02/26 09:20:37  pll
 * Dati kan være like.
 *
 * Revision 1.5  2009/02/15 13:48:04  pll
 * Ny implementasjon.
 *
 * Revision 1.4  2008/12/19 10:06:49  pll
 * Bugfix.
 *
 * Revision 1.3  2008/12/16 13:12:02  pll
 * no message
 *
 * Revision 1.2  2007/10/25 11:37:03  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:48  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:27  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.7  2006/01/05 08:16:32  lwe
 * added logmessage
 * 
 */
 
import java.util.*;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.*;


public final class Control36 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K36: Avslutningsdato før første samtale";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String datoForste = RecordFields.getFieldValue(line, 17);
    String datoSiste = RecordFields.getFieldValue(line, 32);

    String status = RecordFields.getFieldValue(line, 30);
    
    boolean lineHasError = false;
    
    if (status.equalsIgnoreCase("1") || status.equalsIgnoreCase("2")) {    
        try {
          lineHasError = ! Toolkit.isFirstDateBeforeLastDateOrEqual_DDMMYYYY(datoForste, datoSiste);
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
      " kommer dato for avslutting av saken før dato for første behandlingssamtale."; 
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
