package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: Control33.java,v $
 * Revision 1.3  2008/12/16 12:27:57  pll
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
 * Revision 1.9  2006/01/05 08:16:32  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control33 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
	  private final String ERROR_TEXT = "K33: Status ved året slutt";
	  private Vector<Integer> lineNumbers = new Vector<Integer>();

	  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
	  {
	    String status = RecordFields.getFieldValue(line, 30);

	    boolean lineHasError = ! (status.equalsIgnoreCase("1") ||
	                              status.equalsIgnoreCase("2") ||
	                              status.equalsIgnoreCase("3"));
	                                  
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
	      " er det ikke fylt ut hva som er sakens status ved utgangen av året. Feltet er obligatorisk."; 
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