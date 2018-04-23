package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_31 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K31: Har kvalifiseringssum men mangler varighet";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError;
    boolean hasSum;
    
    try {
      int sum = Integer.parseInt(RecordFields.getFieldValue(line, 22));
      hasSum = sum > 0;
    } catch (NumberFormatException e) {
      hasSum = false;
    }

    if (hasSum) {

      String field;
      boolean isFilled = false;
      boolean isValid = true;

      for (int i=1; i<=12; i++) {

        field = RecordFields.getFieldValue(line, (i<10?210+i:2100+i));
        field = field.replace(' ','0');

        if (field.equalsIgnoreCase((i<10?"0":"")+Integer.toString(i))) {
          isFilled = true;
        } else if (!(field.equalsIgnoreCase("00"))) {
          isValid = false;
        }
      }
      
      lineHasError = !(isFilled && isValid);
      
    } else {
      lineHasError = false;
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
      errorReport += lf + "\tFeil (i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "): Deltakeren " +
          "har fått kvalifiseringsstønad i løpet av året," + lf + "\tmen mangler " +
          "utfylling for hvilke måneder stønaden gjelder. Feltet er obligatorisk å fylle ut."; 
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