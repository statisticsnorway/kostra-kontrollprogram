package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: Control25IndividuellPlanKoordinator.java,v $
 * Revision 1.2  2007/10/25 09:10:37  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/09/25 08:24:32  pll
 * Versjon: 2007-rapporteringen.
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control25IndividuellPlanKoordinator 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K25: Koordinator for individuell plan";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_162 = RecordFields.getFieldValue(line, 162);
    String field_163 = RecordFields.getFieldValue(line, 163);

    if (field_162.equalsIgnoreCase("1"))
    {
      try
      {
        int field_163_value = Integer.parseInt(field_163);
        lineHasError = !(field_163_value == 1 || field_163_value == 2);
      }
      catch (NumberFormatException e)
      {
        lineHasError = true;
      }
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
          "er det ikke krysset av for om mottakeren har fått oppnevnt " +
          "koordinator for" + lf + "\tden individuelle planen. Feltet er " +
          "obligatorisk hvis mottaker har fått utarbeidet individuell plan. ";           
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