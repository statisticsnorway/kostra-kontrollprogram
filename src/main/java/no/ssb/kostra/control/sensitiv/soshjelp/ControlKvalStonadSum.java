package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlKvalStonadSum.java,v $
 * Revision 1.3  2008/09/09 07:37:11  pll
 * Tekstendring.
 * final int byttet ut med final String.
 *
 * Revision 1.2  2008/09/05 06:43:38  pll
 * Tekstendring.
 *
 * Revision 1.1  2008/09/04 13:11:07  pll
 * Import
 *
 * Revision 1.2  2007/10/25 09:10:37  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/09/25 08:24:33  pll
 * Versjon: 2007-rapporteringen.
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlKvalStonadSum 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport {
  
  private final String ERROR_TEXT = "K27: Høy kvalifiseringssum";
  private final String MAX_BELOP = "150.000";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    
    boolean lineHasError = false;

    String field_181 = RecordFields.getFieldValue(line, 181);

    try {
      
      int sum = Integer.parseInt(field_181);
      lineHasError = (sum > Integer.parseInt(MAX_BELOP.replace(".","")));
      
    } catch (NumberFormatException e) {      
      //Kontroll av numeriske felter er ikke vaart ansvar.      
    }
            
    if (lineHasError) {
      linesWithError.add(new Integer(lineNumber));
    }

    return lineHasError;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
    
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    
    if (numOfRecords > 0) {
      
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + " " +
          "overstiges Statistisk sentralbyrås kontrollgrense " + lf +
          "\tfor kvalifiseringsstønad på kr. " + 
          MAX_BELOP + ",- ";
      
      if (numOfRecords <= 10) {
        
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++) {
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