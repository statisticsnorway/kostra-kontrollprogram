package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: Control22OkonomiskRadgivning.java,v $
 * Revision 1.3  2008/09/04 13:14:12  pll
 * 2008-rapportering.
 *
 * Revision 1.2  2007/10/25 09:09:55  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/09/25 07:44:48  pll
 * Versjon: 2007-rapporteringen.
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control22OkonomiskRadgivning 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K22: Gjeldsrådgivning";
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
          "\tgjeldsrådgivning i forbindelse med utbetaling av " + lf + 
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