package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_21 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K21: Ytelser de to siste månedene før registrert søknad ved NAV-kontoret.";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field = RecordFields.getFieldValue(line, 151);
      
    boolean lineHasError = false;
    
    if (field.equalsIgnoreCase("1")) {
      
      String field_15_2 = RecordFields.getFieldValue(line, 152);
      
      lineHasError = 
          !(field_15_2.equalsIgnoreCase("2") || field_15_2.equalsIgnoreCase("3"));

      field = RecordFields.getFieldValue(line, 153);
      lineHasError |=  !(field.equalsIgnoreCase("0") || field.equalsIgnoreCase(" ") || field.equalsIgnoreCase("4"));

      field = RecordFields.getFieldValue(line, 154);
      lineHasError |=  !(field.equalsIgnoreCase("0") || field.equalsIgnoreCase(" ") || field.equalsIgnoreCase("5"));

      field = RecordFields.getFieldValue(line, 155);
      lineHasError |=  !(field.equalsIgnoreCase("0") || field.equalsIgnoreCase(" ") || field.equalsIgnoreCase("6"));
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
      errorReport += lf + "\tFeil (i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "): Feltet for " +
          "\"Hadde deltakeren i løpet av de siste to månedene før " + lf +
          "\tregistrert søknad ved NAV-kontoret en eller flere av " +
          "følgende ytelser?\"" + lf + "\tinneholder ugyldige verdier."; 
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