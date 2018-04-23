package no.ssb.kostra.control.sensitiv.soshjelp;

 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control24B extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
          "K24B: Mottakeren mottar trygden arbeidsavklaringspenger, men det er oppgitt Arbeidsløs, ikke registrert på arbeidssituasjon";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
	String field_11 = RecordFields.getFieldValue(line, 11);
	String field_12 = RecordFields.getFieldValue(line, 12);
    String field_13 = RecordFields.getFieldValue(line, 13);

    boolean lineHasError = 
          field_11.equalsIgnoreCase("3") && field_12.equalsIgnoreCase("11") && field_13.equalsIgnoreCase("08");
                              
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
          "mottar mottaker trygden arbeidsavklaringspenger, " + lf +
          "\tmen det er oppgitt Arbeidsledig, men ikke registrert hos NAV på arbeidssituasjon.";
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