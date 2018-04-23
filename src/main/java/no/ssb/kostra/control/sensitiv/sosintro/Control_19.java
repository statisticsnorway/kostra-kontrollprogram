package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: Control_19.java,v $
 * Revision 1.1  2009/10/13 12:01:55  pll
 * Import.
 *
 * Revision 1.1  2009/10/13 10:56:19  pll
 *
 * Revision 1.1  2009/10/13 10:46:44  pll
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;


public final class Control_19 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K19: Høyt beløp per måned";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  private int UPPER_LIMIT = 20000; 
  private String UPPER_LIMIT_STR = "20 000"; 
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    String field;
    int antall = 0;
    
    for (int i=1; i<=12; i++) {
      
      field = RecordFields.getFieldValue(line, 1500+i);
      if (field.equalsIgnoreCase((i>9?"":"0")+Integer.toString(i)))
        antall++;
    }
    
    if (antall > 0) {
      String field_16 = RecordFields.getFieldValue(line, 1601);
      try {
        int belop = Integer.parseInt(field_16);
        lineHasError = belop/antall >= UPPER_LIMIT;
      } catch (NumberFormatException e) {}
    }
    
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
          " record" + (numOfRecords == 1 ? "" : "s") + 
          " er utbetalt introduksjonsstønad per måned" + lf + "\thøyere enn " +
          "SSBs kontrollgrense på "+ UPPER_LIMIT_STR +". " +
          "Kontroller at alle" + lf + "\tstønadsmåneder er krysset av " +
          "og at beløpet er riktig."; 
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