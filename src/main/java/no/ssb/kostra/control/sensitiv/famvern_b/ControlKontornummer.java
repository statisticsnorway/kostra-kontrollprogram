package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 *
 */

import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.FamilievernKontorNrChecker;

public final class ControlKontornummer extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K4: Kontornummer";
  private Vector<Integer> lineNumbers = new Vector<Integer>();
//  private String kontorNumber;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String recordRegionNumber = RecordFields.getRegionNr(line);
	String recordKontorNumber = RecordFields.getKontornummer(line);
//    boolean lineHasError = ! recordKontorNumber.equalsIgnoreCase(kontorNumber);
    boolean lineHasError = !FamilievernKontorNrChecker.hasCorrectKontorNr(recordRegionNumber, recordKontorNumber);

    if (lineHasError)
    {
      lineNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: i " + numOfRecords +
      " record" + (numOfRecords == 1 ? "" : "s") +
      " Det er ikke oppgitt kontornummer, eller feil kode er benyttet." + lf +
      "\tFeltet er obligatorisk og må fylles ut.";
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + lineNumbers.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return lineNumbers.size() > 0;
  }

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

//  public void setKontorNumber (String kontorNumber)
//  {
//    this.kontorNumber = kontorNumber;
//  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}
