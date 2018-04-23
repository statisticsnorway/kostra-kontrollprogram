package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: Control_17.java,v $
 * Revision 1.1  2009/10/13 10:46:44  pll
 * Import.
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.*;

public final class Control_17 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K17: Avslutningsdato før 0101"+Constants.kostraYear.substring(2);
  private Vector<Integer> linesWithError = new Vector<Integer>();
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String date = RecordFields.getFieldValue(line, 14);
    boolean isValidDate = DatoFnr.validDateDDMMYY(date) == 1;
    boolean isBeforeLimit;
    try {
        isBeforeLimit = 
            Toolkit.isFirstDateBeforeLastDate_DDMMYYYY(date.substring(0,4)+
            "20"+date.substring(4), "0101"+Constants.kostraYear + "-1");
//            "20"+date.substring(4), "0111" + (Constants.getRapporteringsAar()-1));
    } catch (Exception e) { isBeforeLimit = false; }
    
    if (isValidDate && isBeforeLimit) {
      
      String field_16 = RecordFields.getFieldValue(line, 1601);
      try {
        int belop = Integer.parseInt(field_16);
//        lineHasError = belop <= 0;
        lineHasError = belop >= 0;
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
          " record" + (numOfRecords == 1 ? "" : "s") + 
          " er sluttdatoen for deltagelsen i introduksjonsprogrammet " + lf +
          "\tfør 0101"+Constants.kostraYear.substring(2)+". Sjekk om " +
          "datoen er riktig, og at det i så fall er utbetalt stønad i "+ lf +
          "\tjanuar "+Constants.kostraYear+"."; 
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