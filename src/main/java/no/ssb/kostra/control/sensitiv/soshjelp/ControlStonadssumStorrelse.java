package no.ssb.kostra.control.sensitiv.soshjelp;

/**
 * $Log: ControlStonadssumStorrelse.java,v $
 * Revision 1.5  2008/09/09 07:36:31  pll
 * Tekstendring.
 *
 * Revision 1.4  2008/09/04 13:14:11  pll
 * 2008-rapportering.
 *
 * Revision 1.3  2007/10/25 09:12:28  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/09/25 07:45:29  pll
 * Versjon: 2007-rapporteringen.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:50  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:29  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.3  2006/01/05 08:31:53  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.2  2005/10/26 17:20:41  lwe
 * endret fra 350-grense til 400 +lagt til CVS-logging i javadoc i begynnelsen av filen
 * 
 */
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlStonadssumStorrelse extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K21: Stønadssummens størrelse";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  private final String MAX_BELOP = "400.000";

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    boolean field_14_1_isFilled = false;
    boolean field_14_2_isFilled = false;
    int field_14_1_value = 0;
    int field_14_2_value = 0;

    String field_14_1 = RecordFields.getFieldValue(line, 141);
    String field_14_2 = RecordFields.getFieldValue(line, 142);

    //Felt 14.1
    try
    {
      int tmp_14_1 = Integer.parseInt(field_14_1);
      field_14_1_value = tmp_14_1;
      field_14_1_isFilled = true;
    }
    catch (NumberFormatException e)
    {
      //Antar at 14.1 ikke er fylt ut.
    }      

    //Felt 14.2
    try
    {
      int tmp_14_2 = Integer.parseInt(field_14_2);
      field_14_2_value = tmp_14_2;
      field_14_2_isFilled = true;
    }
    catch (NumberFormatException e)
    {
      //Antar at 14.2 ikke er fylt ut.
    }      

    if (! (field_14_1_isFilled && field_14_2_isFilled))
    {
      // Kontroll 20 tar seg av denne tilstanden.
    }
    else
    {
      lineHasError = (field_14_1_value + field_14_2_value) > Integer.parseInt(MAX_BELOP.replace(".",""));
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
          " overstiges Statistisk sentralbyrås kontrollgrense for " + lf +
          "\tsamlet stønadsbeløp på kr " + MAX_BELOP + ",-"; 
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