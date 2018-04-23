package no.ssb.kostra.control.regnskap;
/*
 *
 */


import no.ssb.kostra.control.*;
import java.util.Vector;


public class ControlOrgNr extends Control {
  
  private Vector<Integer> lineNumbers = new Vector<Integer>();
  private String previousOrgNr = null;
  
  
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    
    boolean hasError = false;
    String orgNr = line.substring(13, 22);
    
    if (previousOrgNr == null)
      previousOrgNr = orgNr;
    else if (! previousOrgNr.equalsIgnoreCase(orgNr)) {
        hasError = true;
        lineNumbers.add(new Integer(lineNumber));
    }
    
    return hasError;
  }


  
  
  public String getErrorReport (int totalLineNumber) {
    
    StringBuilder errorReport = 
        new StringBuilder("Kontroll av organisasjonsnummer i filuttrekket:" + lf + lf);

    if (foundError()) {
    
      int numOfRecords = lineNumbers.size();
      errorReport.append("\tDet forekommer ulike organisasjonsnummer i filen." + lf);

      if (numOfRecords <= 10) {
      
        errorReport.append("\t(Se record nr.");
        for (int i=0; i<numOfRecords; i++) {
        
          errorReport.append(" " + lineNumbers.elementAt(i));
        }
        errorReport.append(").");
      }
    }
    errorReport.append(lf + "\tKorreksjon: Rett opp i filen " +
        "slik at alle recordene inneholder samme organisasjonsnummer." + lf + lf);
    
    return errorReport.toString();
  }

  
  
  
  public boolean foundError() {
    
    return lineNumbers.size() > 0;
  }

  
  
  
  public int getErrorType() {
    
    return Constants.CRITICAL_ERROR;
  }
  
}
