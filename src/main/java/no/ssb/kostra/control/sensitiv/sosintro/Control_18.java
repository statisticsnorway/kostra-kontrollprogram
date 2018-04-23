package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: Control_18.java,v $
 * Revision 1.2  2009/10/13 12:01:35  pll
 * Tekstendring.
 *
 * Revision 1.1  2009/10/13 10:56:19  pll
 * Import.
 *
 * Revision 1.1  2009/10/13 10:46:44  pll
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_18 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K18: Deltaker er fortsatt i program ved årets slutt, men mangler kryss for desember";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_13 = RecordFields.getFieldValue(line, 13);
    if (field_13.equalsIgnoreCase("06")) {
      String field_15_12 = RecordFields.getFieldValue(line, 1512);
      lineHasError = !field_15_12.equalsIgnoreCase("12");
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
//      errorReport += lf + "\tFeil: i " + numOfRecords + " record" + (numOfRecords == 1 ? "" : "s") + " er  det markert at deltakeren fortsatt er i program, " + lf + "\tmen det mangler utfylling for desember måned. " + "Kontroller at" + lf + "\tstatus ved årets slutt og " + "stønadsmåneder er riktig utfylt.";
      errorReport += lf + "\tDeltakeren er fortsatt i program, men det mangler utfylling for " +
      "desember måned. Kontroller at status ved årets slutt og stønadsmåneder " +"" +
      "er riktig utfylt.";
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