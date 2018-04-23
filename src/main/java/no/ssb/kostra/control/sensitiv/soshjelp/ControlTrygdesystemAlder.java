package no.ssb.kostra.control.sensitiv.soshjelp;

 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.sensitiv.InvalidFnrException;
import no.ssb.kostra.utils.Toolkit;

public final class ControlTrygdesystemAlder extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K15: Tilknytning til trygdesystemet og alder";
  private final int AGE_LIMIT = 66;
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String fodselsnr = RecordFields.getFodselsnummer(line); 
    int alder;
    try {
      alder = Toolkit.getAlderFromFnr(fodselsnr);
    } catch (InvalidFnrException e) {
      //Kan ikke bestemme alder fra fnr. Kontrollen kan ikke foretas.
      return false;
    }
    String field_11 = RecordFields.getFieldValue(line, 11);

    boolean lineHasError = (alder<=AGE_LIMIT) && field_11.equalsIgnoreCase("07");
     
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
          "er mottakeren " + AGE_LIMIT + " år eller yngre og mottar alderspensjon.";
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