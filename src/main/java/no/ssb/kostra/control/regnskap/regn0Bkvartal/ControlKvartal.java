package no.ssb.kostra.control.regnskap.regn0Bkvartal;

import java.util.Vector;
import no.ssb.kostra.control.Constants;




final class ControlKvartal extends no.ssb.kostra.control.Control {
  
  private Vector<Integer> recordNumbers = new Vector<Integer>();
  private int kvartal;

  public ControlKvartal (int kvartal) {
    this.kvartal = kvartal;
  }
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    
    String kvartal_data = RecordFields.getKvartal (line);
    boolean lineHasError = ! kvartal_data.equalsIgnoreCase(Integer.toString(kvartal));

    if (lineHasError)
      recordNumbers.add (new Integer (lineNumber));
    
    return lineHasError;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
    
    StringBuffer errorReport = 
        new StringBuffer("Kontroll 9, kvartal:" + lf + lf);
    
    int numOfRecords = recordNumbers.size();
    
    if (numOfRecords > 0) {
      
      errorReport.append ("\t Advarsel: For kvartalsregnskapet skal posisjon " +
          "7 angi aktuelt kvartal. " + lf + 
          "\t\t" + numOfRecords + " av " + totalLineNumber + " records "+
          "har ikke utfylt kvartal, eller verdien er forskjellig fra " +
          "aktuelt kvartal("+ kvartal +").");
      
      if (numOfRecords <= 10) {
        
        errorReport.append ("(Gjelder følgende records:");
        
        for (int i=0; i<numOfRecords; i++) 
          errorReport.append (" " + recordNumbers.elementAt(i));
        
        errorReport.append (")");
      }
      errorReport.append (lf + lf);
    }
    
    return errorReport.toString();
  }

  
  
  
  public boolean foundError() {
    return recordNumbers.size() > 0;
  }  

  
  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
  
  
}