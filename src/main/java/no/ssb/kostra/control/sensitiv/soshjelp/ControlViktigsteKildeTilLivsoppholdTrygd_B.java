package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlViktigsteKildeTilLivsoppholdTrygd_B.java,v $
 * Revision 1.2  2007/10/25 09:12:28  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
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

public final class ControlViktigsteKildeTilLivsoppholdTrygd_B extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K12B: Viktigste kilde til livsopphold i relasjon til tilknytning til trygdesystemet";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_10 = RecordFields.getFieldValue(line, 10);
    String field_11 = RecordFields.getFieldValue(line, 11);

    if (field_10.equalsIgnoreCase("5"))
    {
      try
      {
        int field_11_value = Integer.parseInt(field_11);
        lineHasError = (field_11_value >= 1 && field_11_value <= 8);
      }
      catch (NumberFormatException e)
      {
      }
    } 

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
          " record" + (numOfRecords == 1 ? "" : "s") + " " +
          "er det oppgitt at mottakerens viktigste kilde til livsopphold" + lf +
          "\tved siste kontakt med sosialkontoret er sosialhjelp, men det er likevel " +
          "oppgitt" + lf + "\tat mottakeren får en form for trygd under feltet for " +
          "tilknytning til trygdesystemet.";
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