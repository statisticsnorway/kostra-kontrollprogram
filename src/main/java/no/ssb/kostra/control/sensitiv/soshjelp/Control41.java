package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
 */

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DatoFnr;

import java.util.Vector;

public final class Control41 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT =
      "K41: Dato for utbetalingsvedtak";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field = RecordFields.getFieldValue(line, 21);
    boolean lineHasError;

    if (field.equalsIgnoreCase("1")) {
      field = RecordFields.getFieldValue(line, 23);
      boolean isValidDate = DatoFnr.validDateDDMMYY(field) == 1;
      if (isValidDate) {
        int year = Integer.parseInt(field.substring(4));
        isValidDate = year <= Constants.getRapporteringsAarTwoDigits();
      }
      lineHasError = !isValidDate;
      
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
        " record" + (numOfRecords == 1 ? "" : "s") + "): " +
        "Feltet for" + lf +
        "\t\"Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter sosialtjenesteloven, så skal utbetalingsvedtakets dato (DDMMÅÅ) oppgis.\"" + lf +
        "\tFeltet er obligatorisk å fylle ut.";
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