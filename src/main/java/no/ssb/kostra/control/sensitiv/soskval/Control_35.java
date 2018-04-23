package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_35 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K35: Årsak til permisjon i løpet av " + Constants.kostraYear+". Svaralternativer";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field = RecordFields.getFieldValue(line, 231);
    boolean lineHasError = false;
    boolean isFilled = true;
    
    if (field.equalsIgnoreCase("1")) {

      isFilled = false;
      
      for (int i=2; i<=4; i++) {
        
        field = RecordFields.getFieldValue(line,230+i);
       
        if (field.equalsIgnoreCase(Integer.toString(i+1)))
          isFilled = true;        
        else if (!(field.equalsIgnoreCase(" ") || field.equalsIgnoreCase("0")))
          lineHasError = true;
      }
    }

    if (lineHasError || !isFilled)
      linesWithError.add(new Integer(lineNumber));
    
    return lineHasError || !isFilled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil (i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "): " +
          "Deltakeren har hatt permisjon fra program i løpet av " + Constants.getRapporteringsAar() + lf +
          "\tmen årsaken til permisjon er ikke utfylt eller feil kode er benyttet."; 
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