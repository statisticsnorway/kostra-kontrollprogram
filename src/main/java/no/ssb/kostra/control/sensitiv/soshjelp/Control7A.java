package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
 */
 
import java.util.Vector;

import no.ssb.kostra.control.Constants;

public final class Control7A extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K7A: Navn";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
	String fnr = RecordFields.getFieldValue(line, 6);
	String navn = RecordFields.getFieldValue(line, 7);
	
	boolean lineHasError = ((fnr.equalsIgnoreCase("00000000000") || fnr.equalsIgnoreCase("           "))) &&
			                (navn.equalsIgnoreCase("0000000000000000000000000") || navn.equalsIgnoreCase("                         "));
	
	if(lineHasError)
	{
	  linesWithError.add(new Integer(lineNumber));
	}
    return lineHasError;
  }
  
  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + lf +
          "\tMottakers fødselsnummer mangler og navn må fylles ut." + lf +
          "\tFeltet er obligatorisk å fylle ut."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + linesWithError.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return linesWithError.size() > 0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}