package no.ssb.kostra.control.sensitiv.soskval;

/*
*/
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.*;

public final class ControlDatoStartProgram extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K16: Dato for når deltakeren begynte i program (iverksettelse).";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String dato = RecordFields.getFieldValue(line, 12);
    
    boolean isValidDate = DatoFnr.validDateDDMMYY(dato) == 1;
    boolean isAcceptableYear;
    try {
      int year = Integer.parseInt(dato.substring(4));
      isAcceptableYear = year <= Constants.getRapporteringsAarTwoDigits();
    } catch (NumberFormatException e) {
      isAcceptableYear = false;
    }
   
    String dato_felt11 = RecordFields.getFieldValue(line, 11);
    boolean isValidDate_felt11 = DatoFnr.validDateDDMMYY(dato_felt11) == 1;
    //boolean isAfterField11 = true;
    if (isValidDate_felt11) 
      dato_felt11 = dato_felt11.substring(0,4)+"20"+dato_felt11.substring(4);
      dato = dato.substring(0,4)+"20"+dato.substring(4);
      /*try {
        isAfterField11 = Toolkit.isFirstDateBeforeLastDate_DDMMYYYY(dato_felt11, dato);
      } catch (Exception e) {}*/
    
    boolean lineHasError = !isValidDate || !isAcceptableYear;// || !isAfterField11;


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
      errorReport += lf + "\tFeil (i " +numOfRecords + " record" + 
          (numOfRecords == 1 ? "" : "s")+"): Feltet \"Hvilken dato begynte " +
          "deltakeren i program (iverksettelse)?\"" + lf + "\tmangler utfylling eller har " +
          "ugyldig dato" + lf + "\tFeltet er obligatorisk å fylle ut.";
         
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