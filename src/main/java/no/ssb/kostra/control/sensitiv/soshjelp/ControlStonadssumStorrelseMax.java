package no.ssb.kostra.control.sensitiv.soshjelp;


import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlStonadssumStorrelseMax extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String MAX_BELOP = "400.000";
  private final String ERROR_TEXT = 
          "K30: Stønadssum på kr " + MAX_BELOP + ",- eller mer";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    boolean field_15_1_isFilled = false;
    boolean field_15_2_isFilled = false;
    int field_15_1_value = 0;
    int field_15_2_value = 0;

    String field_15_1 = RecordFields.getFieldValue(line, 151);
    String field_15_2 = RecordFields.getFieldValue(line, 152);

    //Felt 15.1
    try
    {
      int tmp_15_1 = Integer.parseInt(field_15_1);
      field_15_1_value = tmp_15_1;
      field_15_1_isFilled = true;
    }
    catch (NumberFormatException e)
    {
      //Antar at 15.1 ikke er fylt ut.
    }      

    //Felt 15.2
    try
    {
      int tmp_15_2 = Integer.parseInt(field_15_2);
      field_15_2_value = tmp_15_2;
      field_15_2_isFilled = true;
    }
    catch (NumberFormatException e)
    {
      //Antar at 15.2 ikke er fylt ut.
    }      

    if (! (field_15_1_isFilled && field_15_2_isFilled))
    {
      // Kontroll 20 tar seg av denne tilstanden.
    }
    else
    {
      lineHasError = (field_15_1_value + field_15_2_value) > Integer.parseInt(MAX_BELOP.replace(".",""));
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
          " record" + (numOfRecords == 1 ? "" : "s") + 
          " overstiges Statistisk sentralbyrås kontrollgrense for " + lf +
          "\tsamlet stønadsbeløp på kr " + MAX_BELOP + ",-"; 
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