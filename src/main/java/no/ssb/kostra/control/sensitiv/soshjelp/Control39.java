package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
*/

import no.ssb.kostra.control.Constants;

import java.util.Vector;

public final class Control39
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K39: Første vilkår i året, vilkår";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_21 = RecordFields.getFieldValue(line, 21);
    try
    {
      int field_21_value = Integer.parseInt(field_21);
      lineHasError = !(field_21_value == 1 || field_21_value == 2);
    }
    catch (NumberFormatException e)
    {
      lineHasError = true;  
    }
    

    if (lineHasError)
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
          " record" + (numOfRecords == 1 ? "" : "s") + " " +
          "Det er ikke krysset av for om det stilles vilkår til mottakeren etter sosialtjenesteloven." + lf +
          "\tRegistreres for første vilkår i kalenderåret. Feltet er obligatorisk." + lf;
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
    return Constants.CRITICAL_ERROR;
  }
}