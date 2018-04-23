package no.ssb.kostra.control.sensitiv.soshjelp;

 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlTrygdesystemArbeidssituasjon extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
          "K24: Tilknytning til trygdesystemet og arbeidssituasjon. Uføreytelser/alderspensjon og ikke arbeidssøker";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field_12 = RecordFields.getFieldValue(line, 12);
    String field_13 = RecordFields.getFieldValue(line, 13);

    boolean lineHasError = 
          (field_12.equalsIgnoreCase("04") || field_12.equalsIgnoreCase("07"))  
                   && ! field_13.equalsIgnoreCase("04");
                              
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
          "mottar mottaker trygd, " + lf +
          "\tmen det er ikke oppgitt Ikke arbeidssøker på arbeidssituasjon.";
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