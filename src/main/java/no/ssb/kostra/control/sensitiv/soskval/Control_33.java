package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_33 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private String MIN_SUM = "8.000";
  private final String ERROR_TEXT = 
      "K33: Kvalifiseringsstønad på kr "+MIN_SUM+",- eller lavere.";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError;
    
    try {
      int sum = Integer.parseInt(RecordFields.getFieldValue(line, 22));
      lineHasError = sum <= Integer.parseInt(MIN_SUM.replace(".","")) && sum > 0;
    } catch (NumberFormatException e) {
      lineHasError = false;
    }
    
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
          " record" + (numOfRecords == 1 ? "" : "s") + "): " +
          "Kvalifiseringsstønaden som deltakeren har fått i løpet av " + lf +
          "\trapporteringsåret er lavere enn Statistisk sentralbyrås " +
          "kontrollgrense på kr. "+MIN_SUM+",-."; 
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