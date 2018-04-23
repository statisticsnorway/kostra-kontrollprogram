package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: Control24IndividuellPlanUtarbeidet.java,v $
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

public final class Control24IndividuellPlanUtarbeidet 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K24: Utarbeidelse av individuell plan";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_161 = RecordFields.getFieldValue(line, 161);
    String field_162 = RecordFields.getFieldValue(line, 162);

    if (field_161.equalsIgnoreCase("1"))
    {
      try
      {
        int field_162_value = Integer.parseInt(field_162);
        lineHasError = !(field_162_value == 1 || field_162_value == 2);
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
          "er det ikke krysset av for om mottakeren har fått utarbeidet individuell plan." + lf + 
          "\tFeltet er obligatorisk hvis mottaker har rett til individuell plan. ";           
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