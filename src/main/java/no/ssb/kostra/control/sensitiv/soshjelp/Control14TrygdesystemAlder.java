package no.ssb.kostra.control.sensitiv.soshjelp;

 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control14TrygdesystemAlder extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K14: Tilknytning til trygdesystemet og alder";
  private final int AGE_LIMIT = 67;
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
      /*
    String fodselsnr = RecordFields.getFodselsnummer(line); 
    int alder = Toolkit.getAlderFromFodselsnummer(fodselsnr);
    String field_11 = RecordFields.getFieldValue(line, 11);

    boolean lineHasError = (alder>=AGE_LIMIT) && 
        ! (field_11.equalsIgnoreCase("07") || field_11.equalsIgnoreCase("09"));
     
    if (lineHasError)
    {
      linesWithError.add(new Integer(lineNumber));
    }

    return lineHasError;
       */
      return false;
  }
       

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + " " +
          "er mottakeren " + AGE_LIMIT + " år eller eldre," +
          lf + "\tmen det er ikke krysset av for mottak av alderspensjon.";
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