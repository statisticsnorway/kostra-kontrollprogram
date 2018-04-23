package no.ssb.kostra.control.regnskap.regn0F;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlArterNotNegative extends no.ssb.kostra.control.Control {

  
  private Vector<String[]> negativeBelop = new Vector<String[]>();

     
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    boolean lineHasError = false;
    int art = 0, belop = 0;
        
    try {
      
      art = RecordFields.getArtIntValue(line);

      if (art>=10 && art<=590) {
        belop = RecordFields.getBelopIntValue(line);
        lineHasError = belop < 0;        
      }
    
    } catch (Exception e) {
      
      // Ugyldige data i posisjon for art eller belop, 
      // men det er ikke denne kontrollens ansvar aa
      // rapportere dette...      
    }
        
    if (lineHasError) {
    
      String[] container = new String[3];
      container[0] = Integer.toString (lineNumber);
      container[1] = RecordFields.getArt(line);
      container[2] = Integer.toString(belop);
      negativeBelop.add (container);
    }
    
    return lineHasError;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
  
    String errorReport = "Kontroll 15, sjekk av negative tall:" + lf + lf;
    int numOfRecords = negativeBelop.size();
    
    if (numOfRecords > 0) {
    
      errorReport += "\tAdvarsel: Utgifter skal ha positive beløp. Beløpet er negativt " + "i " + numOfRecords + 
          " record" + (numOfRecords ==1 ? "" : "s") + ":" + lf;
      
      String[] container;
      for (int i=0; i<numOfRecords; i++) {
      
        container = (String[]) negativeBelop.elementAt(i);
        errorReport += "\t\tart " + container[1] + ", beløp = " + container[2] + 
            " (Record nr. " + container[0] + ")" + lf;
      }      
    }
    
    errorReport += lf;
    return errorReport;
  }

  
  
  
  public boolean foundError() {
    return (negativeBelop.size() > 0);
  }
  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
  
  
} // End class ControllArterNotNegative
