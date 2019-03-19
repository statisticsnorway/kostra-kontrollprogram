package no.ssb.kostra.control.sensitiv.soskval;

/*
 * $Log: ControlDatoRegNAV.java,v $
 * Revision 1.2  2009/10/01 07:59:56  pll
 * Versjon: 2009-rapporteringen
 *
 * Revision 1.1  2009/09/30 12:09:28  pll
 * Import.
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DatoFnr;

public final class ControlDatoRegNAV extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K14: Dato for registrert søknad ved NAV-kontoret";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String dato = RecordFields.getFieldValue(line, 10);
    
    boolean isValidDate = DatoFnr.validDateDDMMYY(dato) == 1;
    boolean isAcceptableYear;
    try {
      int year = Integer.parseInt(dato.substring(4));
      isAcceptableYear = year <= Constants.getRapporteringsAarTwoDigits() + 1;
    } catch (NumberFormatException e) {
      isAcceptableYear = false;
    }
    
//    boolean lineHasError = !isValidDate;
    boolean lineHasError = !isValidDate || !isAcceptableYear;


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
          (numOfRecords == 1 ? "" : "s")+"): Feltet \"Hvilken dato ble søknaden " +
          "registrert ved NAV-kontoret?\"" + lf + "\tmangler utfylling eller har " +
          "ugyldig dato. Feltet er obligatorisk å fylle ut.";
         
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