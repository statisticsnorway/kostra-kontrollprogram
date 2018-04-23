package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlKoordinator.java,v $
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

public final class ControlKoordinator 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K34: Fått oppnevnt koordinator";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
      int field_16_1;
      boolean field_16_1_is_filled;
      boolean lineHasError = false;
      
      try {
          field_16_1 = Integer.parseInt(RecordFields.getFieldValue(line, 161));
          field_16_1_is_filled = field_16_1 == 1;
      } catch (NumberFormatException e) {
          field_16_1_is_filled = false;
      }
      
      if (field_16_1_is_filled) {
          
          int field_16_2;         
          try {
              field_16_2 = Integer.parseInt(RecordFields.getFieldValue(line, 162));
              lineHasError = !(field_16_2 == 1 || field_16_2 == 2);
          } catch (NumberFormatException e) {
              lineHasError = true;
          }
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
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s.") + lf +
          "\tDet er ikke krysset av for om mottakeren har fått oppnevnt " + lf +
          "\tkoordinator for den individuelle planen. Feltet er obligatorisk " + lf +
          "\tå fylle ut for de som har fått utarbeidet individuell plan.";           
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