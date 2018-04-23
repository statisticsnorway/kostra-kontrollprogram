package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlGruppenavn extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K7: Gruppenavn";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
	  String field_4 = RecordFields.getFieldValue(line, 4);
	  boolean lineHasError;
	  try
	  {
		  lineHasError = (field_4.trim().length() == 0 || Integer.parseInt(field_4) == 0 );
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
      " Dette er ikke oppgitt på gruppen. Tekstfeltet skal ha maksimalt 30 posisjoner."; 
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
