package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlIndividuellPlan.java,v $
 * Revision 1.2  2009/09/23 07:36:25  pll
 * Versjon: 2009-rapporteringen.
 *
 * Revision 1.1  2009/09/18 08:29:21  pll
 * Import.
 *
 * Revision 1.2  2007/10/25 09:10:37  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/09/25 08:24:33  pll
 * Versjon: 2007-rapporteringen.
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlBoligsituasjon 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K35: Boligsituasjon";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_20 = RecordFields.getFieldValue(line, 20);
    try
    {
      int field_20_value = Integer.parseInt(field_20);
      lineHasError = !(field_20_value == 1 || field_20_value == 2 || field_20_value == 3 || field_20_value == 4 || field_20_value == 5);
    }
    catch (NumberFormatException e)
    {
      lineHasError = true;  
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
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + " " +
          "er det ikke krysset av for mottakerens boligsituasjon";           
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