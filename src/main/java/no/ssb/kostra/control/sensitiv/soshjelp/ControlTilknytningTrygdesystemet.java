package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlTilknytningTrygdesystemet.java,v $
 * Revision 1.5  2008/09/08 12:21:34  pll
 * Bugfix.
 *
 * Revision 1.4  2008/09/04 13:14:11  pll
 * 2008-rapportering.
 *
 * Revision 1.3  2007/10/25 09:12:28  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/09/24 11:41:35  pll
 * Versjon: 2007-rapporteringen.
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
 * Revision 1.4  2006/01/09 11:17:33  lwe
 * lagt til 9 som gyldig kode (umarkert i kravspesifikasjon)
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

public final class ControlTilknytningTrygdesystemet extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K13: Tilknytning til trygdesystemet";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_10 = RecordFields.getFieldValue(line, 10);
    String field_11 = RecordFields.getFieldValue(line, 11);

    if (field_10.equalsIgnoreCase("3"))
    {    
      lineHasError = ! (field_11.equalsIgnoreCase("01") ||
                        field_11.equalsIgnoreCase("02") ||
                        field_11.equalsIgnoreCase("03") ||
                        field_11.equalsIgnoreCase("04") ||
                        field_11.equalsIgnoreCase("05") ||
                        field_11.equalsIgnoreCase("06") ||
                        field_11.equalsIgnoreCase("07") ||
                        field_11.equalsIgnoreCase("08") ||
                        field_11.equalsIgnoreCase("09") ||
                        field_11.equalsIgnoreCase("10"));
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
          "er det benyttet feil kode i registreringen av hvilken trygd/pensjon " +
          lf + "\tsom utgjør størst økonomisk verdi ved siste kontakt med " +
          "sosial-/NAV-kontoret."; 
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