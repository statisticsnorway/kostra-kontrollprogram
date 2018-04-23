package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlViktigsteKildeTilLivsopphold.java,v $
 * Revision 1.4  2009/09/21 06:36:49  pll
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
 * Revision 1.3  2006/09/26 13:40:52  lwe
 * 8 koder i stedet for 7
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

public final class ControlViktigsteKildeTilLivsopphold 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K14: Viktigste kilde til livsopphold. Gyldige verdier";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_11 = RecordFields.getFieldValue(line, 11);
    try
    {
      int field_11_value = Integer.parseInt(field_11);
      lineHasError = (field_11_value < 1 || field_11_value > 9);
    }
    catch (NumberFormatException e)
    {
      lineHasError = true;  
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
          "er mottakerens viktigste kilde til livsopphold" + lf + "\tved siste kontakt med " + 
          "sosial-/NAV-kontoret ikke fylt ut, eller feil kode" + lf + "\ter benyttet. " +
          "Feltet er obligatorisk."; 
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