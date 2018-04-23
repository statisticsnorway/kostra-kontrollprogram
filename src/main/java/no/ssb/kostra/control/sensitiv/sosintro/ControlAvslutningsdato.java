package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: ControlAvslutningsdato.java,v $
 * Revision 1.4  2009/10/13 10:14:23  pll
 * Versjon: 2009-rapporteringen.
 *
 * Revision 1.3  2007/11/07 10:52:04  pll
 * Det er lagt inn egen sjekk av årstallet i avslutningsdato.
 * Årstallet skal samsvare med rapp.år.
 *
 * Revision 1.2  2007/10/25 11:37:11  pll
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
 * Revision 1.3  2006/01/05 08:16:36  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DatoFnr;
//import no.ssb.kostra.utils.Toolkit;

public final class ControlAvslutningsdato extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K13: Avslutningsdato";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  private final String KOSTRA_YEAR = Constants.kostraYear.substring(2);
  private final int DATE_IS_VALID = 1;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_13 = RecordFields.getFieldValue(line, 13);

    if (field_13.equalsIgnoreCase("01") ||
        field_13.equalsIgnoreCase("02") ||
        field_13.equalsIgnoreCase("03") ||
        field_13.equalsIgnoreCase("04") ||
        field_13.equalsIgnoreCase("05") ||
        field_13.equalsIgnoreCase("07") ||
        field_13.equalsIgnoreCase("08") ||
        field_13.equalsIgnoreCase("09") ||
        field_13.equalsIgnoreCase("10"))
    {
      String field_14 = RecordFields.getFieldValue(line, 14);
      String year = field_14.substring(4);
//      boolean isBeforeLimit;
//
//      
//      try {
//          isBeforeLimit = 
//              Toolkit.isFirstDateBeforeLastDate_DDMMYYYY(field_14.substring(0,4)+
//              "20"+field_14.substring(4), "3112"+Constants.kostraYear);
//      } catch (Exception e) { isBeforeLimit = false; }

      
      try
      {
        int testResult = DatoFnr.validDateDDMMYY(field_14);
        lineHasError = ! ((testResult == DATE_IS_VALID) && year.equalsIgnoreCase(KOSTRA_YEAR));
      }
      catch (Exception e)
      {
        lineHasError = true;
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
          " record" + (numOfRecords == 1 ? "" : "s") + 
          " er det krysset av for at mottakeren har" + lf + 
          "\tavsluttet/avbrutt " +
          "deltakelsen i introduksjonsordningen," + lf + "\tmen det er ikke oppgitt " + 
          "dato for når deltakelsen er avsluttet/avbrutt," + lf + "\teller feltet " +
          "har ugyldig format." + lf + "\tFeltet er obligatorisk når " +
          "mottakerens program er avsluttet/avbrutt."; 
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