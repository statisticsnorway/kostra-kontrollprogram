package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 *
 */

import no.ssb.kostra.control.Constants;
import java.util.Vector;

public final class ControlDUFnummer extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K38: DUF-nummer";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  private final  int[] WEIGHTS = new int[] {2,3,6,4,5,7};

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String fNr = RecordFields.getFodselsnummer(line);
    String dufNr = RecordFields.getDUFnummer(line);
    dufNr = dufNr.replace(' ','0');
    fNr = fNr.replace(' ','0');

    if (fNr.equalsIgnoreCase("00000000000")) {
      if (dufNr.equalsIgnoreCase("000000000000")) {
        return true;
      }
    }

    //1. Lager kontrollstreng.
    String contrString = dufNr.substring(4, 10) + dufNr.substring(0,4);

    //2. Regner ut vektet tverrsum.
    int weightIndex = 0;
    int sum = 0;
    for (int i=contrString.length(); i>0; i--)
    {
      try
      {
        int number = Integer.parseInt(contrString.substring(i-1, i));
        sum += number * WEIGHTS[weightIndex];
        weightIndex = (++weightIndex == WEIGHTS.length ? 0 : weightIndex);
      }
      catch (NumberFormatException e)
      {
        linesWithError.add(new Integer(lineNumber));
        return true;
      }
    }

    //3. Beregner kontrolltall.
    int remainder = sum % 11;

    String kontrollTall = (remainder < 10 ? "0" : "") + remainder;

    lineHasError = ! dufNr.substring(10).equalsIgnoreCase(kontrollTall);

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
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + 
          " er det ikke oppgitt DUF-nummer på mottakeren," + lf +
          "\teller DUF-nummeret inneholder en feil."; 
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