package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.CompatJdk13;

public class ControlNumericalFields extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K2: Ulovlig tegn i tallfeltet";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String concatenatedNumericalFields = 
        RecordFields.getAllNumericalFieldsConcatenated(line);
    
    boolean lineHasError = ! CompatJdk13.isNumericalWithSpace(concatenatedNumericalFields);
    
    if (lineHasError)
      lineNumbers.add(new Integer (lineNumber));
    
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    errorReport += "\tDet forekommer andre tegn enn tall " +
    "blankt" + lf + "\ti et eller flere felter hvor det kun skal være oppgitt tall." + lf;
    int numOfErrors = lineNumbers.size();
    if (numOfErrors <= 10)
    {
      errorReport += "\t\t(Gjelder record: ";
      for (int i=0; i<numOfErrors; i++)
      {
        errorReport += " " + lineNumbers.elementAt(i);
      }
      errorReport += ")" + lf;
    }
    errorReport += lf;
    return errorReport;
  }
  public boolean foundError()
  {
    return lineNumbers.size()>0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}
