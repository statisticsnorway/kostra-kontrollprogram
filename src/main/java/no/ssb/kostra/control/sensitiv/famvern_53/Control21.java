package no.ssb.kostra.control.sensitiv.famvern_53;

/*
*/
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control21 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K21: Tilsyn, timer";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError; 
    String fieldValue = RecordFields.getFieldValue(line, 102);
    
    try {
        int value = Integer.parseInt(fieldValue);
        lineHasError = value<1;
    } catch (NumberFormatException e) {
        lineHasError = true;
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
      " det er ikke fylt ut hvor mange timer kotoret har gjennomført når det gjelder " + lf +
      "\t'Tilsyn'. , til tross for at det er oppgitt antall tiltak.";
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