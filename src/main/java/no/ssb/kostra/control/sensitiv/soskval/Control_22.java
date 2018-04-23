package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_22 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K22: Statlig/kommunalt initiert oppfølging/tiltak en uke før registrert søknad om kvalifiseringsprogram.";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field;

    for (int i=1; i<=5; i++) {
      
      field = RecordFields.getFieldValue(line, (160+i));
      if (!(field.equalsIgnoreCase(" ") || 
            field.equalsIgnoreCase("0") || 
            field.equalsIgnoreCase(Integer.toString(i)))) {
        
        linesWithError.add(new Integer(lineNumber));
        return true;
        
      }
    }

    return false;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil (i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "): Feltet " + lf +
          "\t\"Var deltakeren i noen form for statlig eller kommunalt " +
          "initiert oppfølging/tiltak" + lf + "\ten uke før registrert søknad " +
          "om kvalifiseringsprogram?\"" + lf + "\tinneholder ugyldige verdier." + lf +
          "\tFeltet er obligatorisk å fylle ut"; 
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
    return Constants.CRITICAL_ERROR;
  }
}