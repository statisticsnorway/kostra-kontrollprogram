package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_25 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K25: Vært i tiltak som har utløst stønad til livsopphold etter " +
      "forskrift om arbeidsmarkedstiltak (individstønad) i løpet av "+ Constants.kostraYear;
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field = RecordFields.getFieldValue(line, 19);
    boolean lineHasError = !(field.equalsIgnoreCase("1") ||
                             field.equalsIgnoreCase("2"));
    
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
      errorReport += lf + "\tFeil (i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "): Feltet for" + lf +
          "\t\"Har deltakeren – som en del av kvalifiseringsprogrammet " +
          "– vært i tiltak" + lf + "\tsom har utløst stønad til livsopphold " +
          "etter forskrift om arbeidsmarkedstiltak" + lf + "\t(individstønad) i " +
          "løpet av " + Constants.kostraYear + "?\", er ikke fylt ut eller feil kode er benyttet. " +
          lf + "\tFeltet er obligatorisk å fylle ut"; 
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