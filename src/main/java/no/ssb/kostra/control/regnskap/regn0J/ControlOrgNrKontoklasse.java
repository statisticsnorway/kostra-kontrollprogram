package no.ssb.kostra.control.regnskap.regn0J;


import java.util.Vector;
import no.ssb.kostra.control.Constants;


final class ControlOrgNrKontoklasse extends no.ssb.kostra.control.Control {

  
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    String kontoklasse = RecordFields.getKontoklasse (line);
    String orgnr = RecordFields.getOrgNummer(line);
    boolean invalid_orgnr = 
        orgnr.indexOf(" ") != -1 || orgnr.equalsIgnoreCase("000000000");
    
    boolean lineHasError = !kontoklasse.equalsIgnoreCase("5") || invalid_orgnr;
    
    if (lineHasError)
      lineNumbers.add (new Integer (lineNumber));
    
    return lineHasError;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
  
    String errorReport = 
        "Kontroll 6, kombinasjon organisasjonsnummer og kontoklasse:" + lf + lf;
    
    if (foundError()) {
    
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: manglende samsvar mellom kontoklasse og regnskapstype i " + 
          numOfRecords + " record" + (numOfRecords == 1 ? "" : "s") + "."; 
    
      if (numOfRecords <= 10) {
      
        errorReport += " (Gjelder record nr.";
        
        for (int i=0; i<numOfRecords; i++)
          errorReport += " " + lineNumbers.elementAt(i);
        
        errorReport += ").";
      }
    }
    
    errorReport += lf + "\tKorreksjon: Legg til organisasjonsnummer og/eller benytt kontoklasse 5." + lf + lf;
    return errorReport;
  }

  
  
  
  public boolean foundError() {
    return lineNumbers.size() > 0;
  }  

  
  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
  
  
}
