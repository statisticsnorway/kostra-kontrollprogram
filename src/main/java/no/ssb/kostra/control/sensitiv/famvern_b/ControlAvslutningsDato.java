package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 * $Log: ControlAvslutningsDato.java,v $
 * Revision 1.1  2009/01/06 14:17:12  pll
 * Import.
 *
 * Revision 1.3  2008/12/16 12:51:25  pll
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
 * Revision 1.9  2006/03/31 13:20:38  lwe
 * Calendar-months are counted from 0, not 1. So January=0. Subtract 1 from the dates in the controlled fields
 *
 * Revision 1.8  2006/03/27 12:41:11  lwe
 * bruker metode for å hente firesifret år ut fra tosifret år med metode i utils.DatoFnr-klassen
 *
 * Revision 1.7  2006/01/05 08:16:32  lwe
 * added logmessage
 * 
 */
 
import java.util.*;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.*;

public final class ControlAvslutningsDato extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K20: Gruppebehandlingen er avsluttet, men avslutningsdato mangler";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false; 
    String field_13 = RecordFields.getFieldValue(line, 13);
    
    if (field_13.equalsIgnoreCase("2")) {
      
      String field_14 = RecordFields.getFieldValue(line, 14);
      int checkValue = 0;
      checkValue = DatoFnr.validDateDDMMYYYY(field_14);
      lineHasError = (checkValue == 0); 
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
      " det er krysset av for at gruppebehandlingen er avsluttet i rapporteringsåret," + lf + 
      "\tmen ikke fylt ut dato for avslutning av saken."; 
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
