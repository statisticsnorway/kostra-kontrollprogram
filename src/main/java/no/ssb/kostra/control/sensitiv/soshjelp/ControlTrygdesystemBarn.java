package no.ssb.kostra.control.sensitiv.soshjelp;

 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlTrygdesystemBarn extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
          "K23: Tilknytning til trygdesystemet og barn. Overgangsstønad";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field_12 = RecordFields.getFieldValue(line, 12);
    String field_101 = RecordFields.getFieldValue(line, 101);
    int field_102;
    
    try {
        field_102 = Integer.parseInt(RecordFields.getFieldValue(line, 102));
    } catch (NumberFormatException e) {
        field_102 = 0;
    }

    boolean lineHasError = field_12.equalsIgnoreCase("05") 
                        && ! field_101.equalsIgnoreCase("1")
                        && field_102 == 0;    
            
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
          "mottar mottaker overgangsstønad, " + lf +
          "\tmen det er ikke oppgitt barn under 18 år i husholdningen.";
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