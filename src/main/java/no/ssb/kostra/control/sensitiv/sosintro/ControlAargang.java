package no.ssb.kostra.control.sensitiv.sosintro;

/**
 * $Log: ControlAargang.java,v $
 * Revision 1.4  2007/11/07 10:50:01  pll
 * Endret implementasjon av getErrorType().
 *
 * Revision 1.3  2007/11/06 07:37:05  pll
 * Bugfix.
 *
 * Revision 1.2  2007/10/25 11:37:04  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:51  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:30  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.4  2006/01/05 08:31:53  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.3  2005/10/28 17:22:39  lwe
 * endringer etter testing hos fag
 *
 * Revision 1.2  2005/10/26 17:18:50  lwe
 * endret årgang fra 04 til 05 (woops) +lagt til CVS-logging i javadoc i begynnelsen av filen
 * 
 */
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlAargang extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K4: Oppgaveår";
  private Vector<Integer> recordsWithError = new Vector<Integer>();
  private String aargang = Constants.kostraYear.substring(2); 

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String oppgaveaar = RecordFields.getOppgaveaar(line);

    boolean lineHasError = ! oppgaveaar.equalsIgnoreCase(aargang);

    if (lineHasError)
    {
      recordsWithError.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    int numOfRecords = recordsWithError.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: Årgangen som angis på " + numOfRecords + " av totalt " + totalLineNumber + 
          " records stemmer ikke" + lf + "\toverens med det som er gjeldende oppgaveår; sjekk at riktig fil benyttes." + lf; 
    }
    errorReport += lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return recordsWithError.size() > 0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}
