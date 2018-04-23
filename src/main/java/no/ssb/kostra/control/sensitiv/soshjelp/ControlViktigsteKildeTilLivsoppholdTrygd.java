package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlViktigsteKildeTilLivsoppholdTrygd.java,v $
 * Revision 1.4  2009/09/21 08:51:49  pll
 * Versjon: 2009-rapporteringen.
 *
 * Revision 1.3  2008/09/04 13:14:11  pll
 * 2008-rapportering.
 *
 * Revision 1.2  2007/10/25 09:12:28  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.3  2006/12/28 09:24:18  pll
 * Rettet opp programmeringsfeil: tillatte koder 1-9 (ikke 1-8)
 *
 * Revision 1.2  2006/09/22 09:13:50  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:30  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.3  2006/01/05 08:31:53  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.2  2006/01/05 08:16:36  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlViktigsteKildeTilLivsoppholdTrygd extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
          "K20: Viktigste kilde til livsopphold i relasjon til tilknytning til trygdesystemet. Trygd";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_11 = RecordFields.getFieldValue(line, 11);
    String field_12 = RecordFields.getFieldValue(line, 12);

    if (field_11.equalsIgnoreCase("3"))
      lineHasError = ! (field_12.equalsIgnoreCase("01") ||
                        field_12.equalsIgnoreCase("02") ||
                        field_12.equalsIgnoreCase("04") ||
                        field_12.equalsIgnoreCase("05") ||
                        field_12.equalsIgnoreCase("06") ||
                        field_12.equalsIgnoreCase("07") ||
                        field_12.equalsIgnoreCase("09") ||
                        field_12.equalsIgnoreCase("10") ||
                        field_12.equalsIgnoreCase("11"));

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
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + " " +
          "er det oppgitt at mottakerens viktigste kilde til livsopphold " +
          "er \"Trygd/pensjon\"," + lf + "\tmen det er ikke oppgitt hvilken trygd som " +
          "utgjorde største verdi ved siste kontakt" + lf + "\tmed sosial-/NAVkontoret. " +
          "Feltet er obligatorisk å fylle ut."; 
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