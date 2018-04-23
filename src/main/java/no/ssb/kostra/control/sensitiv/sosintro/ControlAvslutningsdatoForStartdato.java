package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: ControlAvslutningsdatoForStartdato.java,v $
 * Revision 1.2  2007/10/25 11:37:12  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.1  2006/10/03 13:22:25  lwe
 * Ny kontroll - ikke testet
 *
 */

import java.util.Calendar;
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DatoFnr;
import no.ssb.kostra.utils.CompatJdk13;

public final class ControlAvslutningsdatoForStartdato extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport {

  private final String ERROR_TEXT = "K16: Avslutningsdato før startdato";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    boolean lineHasError = false;
    
    // kontroll 
    String field_10 = RecordFields.getFieldValue(line, 10);
    String field_14 = RecordFields.getFieldValue(line, 14);
    
    if (CompatJdk13.isNumerical(field_10) && CompatJdk13.isNumerical(field_14)) {    
      int checkValue = 0;
      checkValue = DatoFnr.validDateDDMMYY(field_10);
      checkValue += DatoFnr.validDateDDMMYY(field_14);
      if (checkValue == 2) {
        int year_10 = DatoFnr.findYearYYYY(Integer.parseInt(field_10.substring(4)));
        Calendar date_10 = Calendar.getInstance();
        date_10.set(year_10,
                   Integer.parseInt(field_10.substring(2,4))-1,
                   Integer.parseInt(field_10.substring(0,2)));
        int year_14 = DatoFnr.findYearYYYY(Integer.parseInt(field_14.substring(4)));
        Calendar date_14 = Calendar.getInstance();
        date_14.set(year_14,
                    Integer.parseInt(field_14.substring(2,4))-1,
                    Integer.parseInt(field_14.substring(0,2)));
        lineHasError = date_14.before(date_10);            
      }      
    }
    
    if (lineHasError) {
      linesWithError.add(new Integer(lineNumber));
    }
    return lineHasError;
  }
  
  public String getErrorReport (int totalLineNumber) {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0) {      
      errorReport += lf + "\tFeil: i " + numOfRecords + " record" + 
      (numOfRecords == 1 ? "" : "s") + " kommer sluttdatoen for deltakelsen i "
      + lf + "\tintroduksjonsprogrammet før i tid enn datoen som er oppgitt som startdato "
      + lf + "\tfor deltagelse i introduksjonsprogrammet. ";
      if (numOfRecords <= 10) {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++) {
          errorReport += " " + linesWithError.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError() {
    return linesWithError.size() > 0;
  }  

  public String getErrorText() {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }


}