package no.ssb.kostra.control.regnskap.regn0F;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlAntallSifferBelop extends no.ssb.kostra.control.Control {

  
  private Vector<String[]> suspekteBelop = new Vector<String[]>();

     
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    boolean lineHasError = false;
    String belop = RecordFields.getBelop(line);    
        
    try {
      
      int art = RecordFields.getArtIntValue(line);

      if ((art>=10 && art<=590) || (art>=600 && art<=990)) {
        String digits = belop.replaceAll("[ -]","");
        lineHasError = digits.length() > 6;        
      }
    
    } catch (Exception e) {
      
      // Ugyldige data i posisjon for art, 
      // men det er ikke denne kontrollens ansvar aa
      // rapportere dette...      
    }
        
    if (lineHasError) {
    
      String[] container = new String[2];
      container[0] = Integer.toString (lineNumber);
      container[1] = belop;
      suspekteBelop.add (container);
    }
    
    return lineHasError;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
  
    String errorReport = 
        "Kontroll 15, sjekk at beløpene ikke har mer enn 6 siffer:" + lf + lf;
    int numOfRecords = suspekteBelop.size();
    
    if (numOfRecords > 0) {
    
      errorReport += 
          "\tObs: Er du sikker på at beløpene er oppgitt i 1000 kroner," +
          " f.eks. beløp 30 000 må oppgis som 30 i filen?" + lf +
          "\tSjekk følgende record" + (numOfRecords==1?"":"s") + ":" + lf;
      
      String[] container;
      for (int i=0; i<numOfRecords; i++) {
      
        container = (String[]) suspekteBelop.elementAt(i);
        errorReport += 
            "\t\tRecord nr. " + container[0] + ", beløp = " + container[1] + lf;
      }      
    }
    
    errorReport += lf;
    return errorReport;
  }

  
  
  
  public boolean foundError() {
    return (suspekteBelop.size() > 0);
  }
  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
  
  
} // End class ControllArterNotNegative
