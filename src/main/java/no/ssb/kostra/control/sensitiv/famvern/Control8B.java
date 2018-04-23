package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: Control8.java,v $
 * Revision 1.3  2008/12/12 17:00:16  pll
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
 * Revision 1.6  2006/01/05 08:16:32  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control8B extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K8B: Hvis avkrysset for 'Andre' så spesifiser";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
	  String field_5 = RecordFields.getFieldValue(line, 5);
	  String field_51 = RecordFields.getFieldValue(line, 51);
	  
	  boolean lineHasError;
	  
	  try
	  {
		  int kode = Integer.parseInt(field_5);
		  lineHasError = (kode == 15 && (field_51.trim().length() == 0 || Integer.parseInt(field_51) == 0 ));
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
      ". Det er krysset av for 'Andre, spesifiser' på" + lf +
      "\t'Hvordan har klienten blitt kjent med familievernet'," + lf +
      "\tmen spesifiseringen av hvordan dette har skjedd mangler," + lf +
      "\teller feltet inneholder for mange (over 30) karakterer."; 
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
