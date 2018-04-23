package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: Control18.java,v $
 * Revision 1.4  2008/12/13 07:29:51  pll
 * no message
 *
 * Revision 1.3  2007/11/12 11:55:01  pll
 * Benytter no.ssb.kostra.control.Constants.
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
 
import java.util.*;

import no.ssb.kostra.control.Constants;

public final class Control18B extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K18B: Hvis avkrysset for 'Annet' så spesifiser";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
	  boolean lineHasError;
	  String field_14 = RecordFields.getFieldValue(line, 14);
	  String field_141 = RecordFields.getFieldValue(line, 141);
	  
	  try
	  {
		 int kode = Integer.parseInt(field_14);
		 lineHasError = (kode == 7 && (field_141.trim().length() == 0
				         || Integer.parseInt(field_141) == 0 ));
	  }
	  catch (Exception e)
	  {
	    lineHasError = false;
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
      " Det er krysset for 'Andre, spesifiser' på spørsmålet om tilknytning til arbeidslivet, " + lf +
      "\tmen spesifiseringen mangler, eller inneholder for mange (over 30) karakterer."; 
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
