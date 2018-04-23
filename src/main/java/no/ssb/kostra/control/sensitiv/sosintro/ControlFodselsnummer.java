package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: ControlFodselsnummer.java,v $
 * Revision 1.3  2008/10/09 13:40:40  pll
 * Tekstendring.
 *
 * Revision 1.2  2007/10/25 11:37:12  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:51  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:31  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.4  2006/01/05 08:31:54  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.3  2006/01/05 08:16:37  lwe
 * added logmessage
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
      if (lineHasError)
      {
        invalidFodselsnummer.add (new Integer (lineNumber));
      }
    }
    catch (Exception e)
    {
      lineHasError = true;
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
      errorReport += lf + "\tFeil: Det er ikke oppgitt fødselsnummer/d-nummer på mottakeren " + lf +
          "\teller fødselsnummeret/d-nummeret inneholder feil. "+ lf +
          "\tMed mindre det er snakk om en utenlandsk statsborger " + lf + 
          "\tsom ikke er tildelt norsk personnummer eller d-nummer, " + lf +
          "\tskal feltet inneholde mottakerens fødselsnummer/d-nummer (11 siffer).";
      
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
