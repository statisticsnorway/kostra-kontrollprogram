package no.ssb.kostra.control.sensitiv.famvern;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control17 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K17: Primærklientens bosituasjon ved opprettelsen";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
     boolean lineHasError;     
     String field_10 = RecordFields.getFieldValue(line, 10);
     
     try {
         int kode = Integer.parseInt(field_10);
         lineHasError = kode<1 || kode>5;
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
      " er det ikke fylt ut om primærklienten bor sammen med andre ved " + lf + 
      "\tsakens opprettelse eller feil kode er benyttet. Feltet er obligatorisk."; 
      int iterations; 
      boolean tooMany = false; 
      if (numOfRecords <= 10) {
        iterations = numOfRecords; 
      } else {
        iterations = 10; 
        tooMany = true; 
      }
      errorReport += lf + "\t\t(Gjelder "+(tooMany ? "blant annet " : "")+"record nr.";
      for (int i=0; i<iterations; i++)
      {
        errorReport += " " + lineNumbers.elementAt(i);
      }
      errorReport += ").";
      if(tooMany) {
        errorReport += lf + "Det er flere records som har denne feilen, men de blir ikke listet. "; 
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
