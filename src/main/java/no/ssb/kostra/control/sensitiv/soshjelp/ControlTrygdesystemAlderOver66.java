package no.ssb.kostra.control.sensitiv.soshjelp;

 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.Toolkit;
import no.ssb.kostra.control.sensitiv.InvalidFnrException;

public final class ControlTrygdesystemAlderOver66 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
          "K21: Tilknytning til trygdesystemet og alder. 67 år eller eldre";
  private final int AGE_LIMIT = 67;
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String fodselsnr = RecordFields.getFodselsnummer(line); 
    int alder;
    
    try {
      alder = Toolkit.getAlderFromFnr(fodselsnr);
    } catch (InvalidFnrException e) {
      // Ugyldig fnr sjekkes av annen kontroll.
      return false;
    }
    
    String field_11 = RecordFields.getFieldValue(line, 11);
    String field_12 = RecordFields.getFieldValue(line, 12);
    boolean lineHasError = false;

    if (alder>=AGE_LIMIT && field_11.equalsIgnoreCase("3"))
        lineHasError = ! (field_12.equalsIgnoreCase("07") || field_12.equalsIgnoreCase("09"));
     
    if (lineHasError)
      linesWithError.add(new Integer(lineNumber));

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
          "er mottakeren 67 år eller eldre, " + 
          "men det er ikke krysset av" + lf + "\t for mottak av \"Alderspensjon\" " +
          "eller \"Supplerende stønad (kort botid)\".";
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