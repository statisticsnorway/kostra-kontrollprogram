package no.ssb.kostra.control.sensitiv.soskval;

/*
 * $Log: ControlFodselsnummer.java,v $
 * Revision 1.1  2009/09/30 08:31:45  pll
 * Import.
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DatoFnr;

public final class ControlFodselsnummer extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K5: Fødselsnummer";
  private Vector<Integer> invalidFodselsnummer = new Vector<Integer>();
  private final int FODSELSNUMMER_OK = 1;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    String fodselsnummer = RecordFields.getFodselsnummer(line);

    try
    {
      int fnr_check = DatoFnr.validNorwId(fodselsnummer);
      lineHasError = fnr_check != FODSELSNUMMER_OK;
    }
    catch (Exception e)
    {
      lineHasError = true;
    }
    
    if (lineHasError)
    {
      invalidFodselsnummer.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = invalidFodselsnummer.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: ugyldig fødselsnummer i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "."; 
      errorReport += lf + "\tDet er ikke oppgitt fødselsnummer/d-nummer på "
      + "deltakeren" + lf + "\teller fødselsnummeret/d-nummeret inneholder" +
      " feil." + lf + "\tMed mindre det er snakk om en utenlandsk " + 
      "statsborger som ikke er" +lf + "\ttildelt norsk personnummer eller d-nummer, " + 
      "skal feltet inneholde" + lf + "\tdeltakeren fødselsnummer/d-nummer (11 siffer).";
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + invalidFodselsnummer.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return invalidFodselsnummer.size() > 0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}