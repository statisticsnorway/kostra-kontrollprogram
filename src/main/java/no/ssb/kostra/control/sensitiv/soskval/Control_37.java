package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DatoFnr;

public final class Control_37 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K37: Dato for avsluttet program (gjelder fullførte, avsluttede " +
      "etter avtale og varig avbrutte program, ikke for permisjoner) (DDMMÅÅ).";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field = RecordFields.getFieldValue(line, 24);
    boolean lineHasError;
    
    if (field.equalsIgnoreCase("3") || field.equalsIgnoreCase("4") || field.equalsIgnoreCase("5")) {
      
      field = RecordFields.getFieldValue(line, 25);
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
        "Feltet for \"Hvilken dato avsluttet deltakeren programmet?\"" + lf +
        "\tmå fylles ut dersom det er krysset av for svaralternativ " + lf +
        "\t3= \"Deltakeren har fullført program eller avsluttet program " +
        "etter avtale (gjelder ikke flytting)\"" + lf + "\t4 = \"Deltakerens program er varig " +
        "avbrutt på grunn av uteblivelse (gjelder ikke flytting)\"" + lf + "\t\teller" + lf +
        "\t5= \"Deltakerens program ble avbrutt på grunn av flytting til annen kommune\"" + lf +
        lf + "\tunder feltet for \"Hva er status " +
        "for deltakelsen i kvalifiseringsprogrammet per 31.12."  + Constants.kostraYear + "?\"";
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