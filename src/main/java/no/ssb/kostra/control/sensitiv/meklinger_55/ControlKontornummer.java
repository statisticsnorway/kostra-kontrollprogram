package no.ssb.kostra.control.sensitiv.meklinger_55;

/*
*/
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlKontornummer extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K4: Kontornummer";
  private Vector<Integer> lineNumbers = new Vector<Integer>();
  private Vector<Integer> invalidKontorNumber = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
	boolean lineHasError = true;

	String recordKontorNumber = RecordFields.getFieldValue(line, 2);
	recordKontorNumber = recordKontorNumber.replace (' ', '0');
	{
	if (recordKontorNumber.equalsIgnoreCase("016") || recordKontorNumber.equalsIgnoreCase("017") ||
		recordKontorNumber.equalsIgnoreCase("021") || recordKontorNumber.equalsIgnoreCase("023") ||
		recordKontorNumber.equalsIgnoreCase("024") || recordKontorNumber.equalsIgnoreCase("026") ||
		recordKontorNumber.equalsIgnoreCase("037") || recordKontorNumber.equalsIgnoreCase("038") ||
		recordKontorNumber.equalsIgnoreCase("039") || recordKontorNumber.equalsIgnoreCase("041") ||
		recordKontorNumber.equalsIgnoreCase("043") || recordKontorNumber.equalsIgnoreCase("044") ||
		recordKontorNumber.equalsIgnoreCase("051") || recordKontorNumber.equalsIgnoreCase("052") ||
		recordKontorNumber.equalsIgnoreCase("054") || recordKontorNumber.equalsIgnoreCase("061") ||
		recordKontorNumber.equalsIgnoreCase("062") || recordKontorNumber.equalsIgnoreCase("064") ||
		recordKontorNumber.equalsIgnoreCase("071") || recordKontorNumber.equalsIgnoreCase("073") ||
		recordKontorNumber.equalsIgnoreCase("081") || recordKontorNumber.equalsIgnoreCase("082") ||
		recordKontorNumber.equalsIgnoreCase("091") || recordKontorNumber.equalsIgnoreCase("101") ||
		recordKontorNumber.equalsIgnoreCase("111") || recordKontorNumber.equalsIgnoreCase("112") ||
		recordKontorNumber.equalsIgnoreCase("121") || recordKontorNumber.equalsIgnoreCase("122") ||
		recordKontorNumber.equalsIgnoreCase("123") || recordKontorNumber.equalsIgnoreCase("124") ||
		recordKontorNumber.equalsIgnoreCase("125") || recordKontorNumber.equalsIgnoreCase("126") ||
		recordKontorNumber.equalsIgnoreCase("141") || recordKontorNumber.equalsIgnoreCase("142") ||
		recordKontorNumber.equalsIgnoreCase("151") || recordKontorNumber.equalsIgnoreCase("152") ||
		recordKontorNumber.equalsIgnoreCase("153") || recordKontorNumber.equalsIgnoreCase("162") ||
		recordKontorNumber.equalsIgnoreCase("171") || recordKontorNumber.equalsIgnoreCase("172") ||
		recordKontorNumber.equalsIgnoreCase("181") || recordKontorNumber.equalsIgnoreCase("182") ||
		recordKontorNumber.equalsIgnoreCase("183") || recordKontorNumber.equalsIgnoreCase("184") ||
		recordKontorNumber.equalsIgnoreCase("185") || recordKontorNumber.equalsIgnoreCase("191") ||
		recordKontorNumber.equalsIgnoreCase("192") || recordKontorNumber.equalsIgnoreCase("193") ||
		recordKontorNumber.equalsIgnoreCase("201") || recordKontorNumber.equalsIgnoreCase("202") ||
		recordKontorNumber.equalsIgnoreCase("203") || recordKontorNumber.equalsIgnoreCase("204")
		)
	    lineHasError = false;
	}
	
	if (lineHasError)
	{
	  invalidKontorNumber.add (new Integer (lineNumber));
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
      " Det er ikke oppgitt kontornummer, eller feil kode er benyttet.";
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
