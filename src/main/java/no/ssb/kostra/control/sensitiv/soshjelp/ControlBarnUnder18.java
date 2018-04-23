package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlBarnUnder18.java,v $
 * Revision 1.6  2009/10/01 06:26:08  pll
 * Bugfix (Feil i tekst).
 *
 * Revision 1.5  2009/09/21 11:05:43  pll
 * Bugfix.
 *
 * Revision 1.4  2009/09/21 06:24:50  pll
 * Versjon: 2009-rapporteringen.
 *
 * Revision 1.3  2009/09/18 13:34:25  pll
 * no message
 *
 * Revision 1.2  2007/10/25 09:11:12  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:49  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:29  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.3  2006/01/05 08:31:52  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.2  2006/01/05 08:16:34  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlBarnUnder18 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K12: Det bor barn under 18 år i husholdningen";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    int antBarnUnder18;
    try {
        antBarnUnder18 = Integer.parseInt(RecordFields.getFieldValue(line, 102));
    } catch (NumberFormatException e) {
        antBarnUnder18 = 0;
    }
        
    boolean lineHasError = false;        
    
    if (antBarnUnder18 > 0) {
        
        String barnUnder18 = RecordFields.getFieldValue(line, 101);
        lineHasError = barnUnder18.equalsIgnoreCase("0") ||
                       barnUnder18.equalsIgnoreCase(" ") ||
                       barnUnder18.equalsIgnoreCase("2");        
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
          " record" + (numOfRecords == 1 ? "" : "s") + " er det oppgitt " +
          "antall barn under 18 år som bor i husholdningen " + lf + 
          "\tsom mottaker eller ektefelle/samboer har forsørgerplikt for, " +
          "men det er ikke" + lf + "\toppgitt at det bor barn i husholdningen. " + lf +
          "\tFeltet er obligatorisk å fylle ut når det er oppgitt antall " +
          "barn under 18 år som bor i husholdningen."; 
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