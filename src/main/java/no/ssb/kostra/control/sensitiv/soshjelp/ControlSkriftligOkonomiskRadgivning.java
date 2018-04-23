package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlSkriftligOkonomiskRadgivning.java,v $
 * Revision 1.2  2007/10/25 09:11:53  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.1  2006/09/26 13:55:56  lwe
 * Lagt til ny kontroll (for felt 15), kontroll 18 Skriftlig økonomisk rådgivning
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlSkriftligOkonomiskRadgivning 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K18: Skriftlig økonomisk rådgivning";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String field_15 = RecordFields.getFieldValue(line, 15);
    try
    {
      int field_15_value = Integer.parseInt(field_15);
      lineHasError = !(field_15_value == 1 || field_15_value == 2);
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
          "er det ikke angitt om mottakeren er gitt " + lf + 
          "\tskriftlig økonomisk rådgivning i forbindelse med utbetaling av " + lf + 
          "\tøkonomisk sosialhjelp. Feltet er obligatorisk. ";           
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