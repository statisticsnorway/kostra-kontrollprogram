package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: Control_20.java,v $
 * Revision 1.3  2010/01/11 08:49:18  pll
 * Endret etter endringer i kravspec (jf. e-post 11.01.10).
 *
 * Revision 1.2  2009/11/09 13:29:05  pll
 * Tekstendring.
 *
 * Revision 1.1  2009/10/13 12:05:42  pll
 * Import.
 *
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_20 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K20: Lavt beløp per måned";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  private int LIMIT = 8000; 

  
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    
    String field_16 = RecordFields.getFieldValue(line, 1601);
    try {
      int belop = Integer.parseInt(field_16);
      lineHasError = belop <= LIMIT;
    } catch (NumberFormatException e) {}
       
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
//      errorReport += lf + "\tFeil: i " + numOfRecords + " record" + (numOfRecords == 1 ? "" : "s") + " er utbetalt introduksjonsstønad i løpet av rapporteringsåret" + lf + "\tlavere enn " + "SSBs kontrollgrense på "+ LIMIT +". " + "Kontroller at alle" + lf + "\tstønadsmåneder er krysset av " + "og at beløpet er riktig.";
      errorReport += lf + "\tUtbetalt introduksjonsstønad per måned er lavere enn SSBs " +
      "kontrollgrense på kr " + LIMIT + "Kontroller at alle stønadsmåneder er krysset " +
      "av og at beløpet er riktig."; 
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