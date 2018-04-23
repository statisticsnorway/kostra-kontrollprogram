package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: ControlStonadssum.java,v $
 * Revision 1.2  2007/10/25 11:37:12  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:51  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:32  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.4  2006/01/05 08:31:55  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.3  2006/01/05 08:16:38  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.CompatJdk13;

public final class ControlStonadssum extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K15: Stønadssum";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  private final int MAX_SUM_VALUE = 350000;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_16_1 = RecordFields.getFieldValue(line, 1601);
    field_16_1 = CompatJdk13.removeSpace(field_16_1);
  
    try
    {
      int field_16_1_value = Integer.parseInt(field_16_1);
      lineHasError = (field_16_1_value <= 0  || field_16_1_value >= MAX_SUM_VALUE);
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
//      errorReport += lf + "\tFeil: i " + numOfRecords + " record" + (numOfRecords == 1 ? "" : "s") + " er det ikke oppgitt hvor mye mottakeren har fått i" + lf + "\tintroduksjonsstønad i løpet av året " + "(eller beløpet er større eller lik " + MAX_SUM_VALUE + ")," + lf + "\teller feltet inneholder " + "andre tegn enn tall. " + "Feltet er obligatorisk."; 
      errorReport += lf + "\tDet er ikke oppgitt hvor mye mottakeren har fått i " +
      "introduksjonsstønad i løpet av året, eller feltet inneholder andre tegn " +
      "enn tall. Feltet er obligatorisk å fylle ut."; 
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
