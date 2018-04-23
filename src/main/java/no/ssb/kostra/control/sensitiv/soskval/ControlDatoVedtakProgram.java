package no.ssb.kostra.control.sensitiv.soskval;

/*
 * $Log: ControlDatoVedtakProgram.java,v $
 * Revision 1.5  2009/10/13 12:40:59  pll
 * Tekstendring.
 *
 * Revision 1.4  2009/10/11 19:16:27  pll
 * Bugfix.
 *
 * Revision 1.3  2009/10/11 18:54:18  pll
 * Endret etter endring i kravspec.
 *
 * Revision 1.2  2009/10/01 08:01:10  pll
 * Versjon: 2009-rapporteringen
 *
 * Revision 1.1  2009/09/30 13:21:11  pll
 * Import.
 *
 * Revision 1.1  2009/09/30 12:09:28  pll
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.*;

public final class ControlDatoVedtakProgram extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K15: Dato for fattet vedtak om program (søknad innvilget)";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String dato = RecordFields.getFieldValue(line, 11);
    
    boolean isValidDate = DatoFnr.validDateDDMMYY(dato) == 1;
    boolean isAcceptableYear;
    try {
      int year = Integer.parseInt(dato.substring(4));
      isAcceptableYear = year <= Constants.getRapporteringsAarTwoDigits();
    } catch (NumberFormatException e) {
      isAcceptableYear = false;
    }


    String dato_felt10 = RecordFields.getFieldValue(line, 10);
    boolean isValidDate_felt10 = DatoFnr.validDateDDMMYY(dato_felt10) == 1;
    //boolean isAfterField10 = true;
    if (isValidDate_felt10) 
      dato_felt10 = dato_felt10.substring(0,4)+"20"+dato_felt10.substring(4);
      dato = dato.substring(0,4)+"20"+dato.substring(4);
      /*try {
        isAfterField10 = Toolkit.isFirstDateBeforeLastDate_DDMMYYYY(dato_felt10, dato);
      } catch (Exception e) {}*/
    
    boolean lineHasError = !isValidDate || !isAcceptableYear;// || !isAfterField10;

            
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
          (numOfRecords == 1 ? "" : "s")+"): Feltet \"Hvilken dato det ble " +
          "fattet vedtak om program (søknad innvilget)?\"" + lf + "\tmangler utfylling eller har " +
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