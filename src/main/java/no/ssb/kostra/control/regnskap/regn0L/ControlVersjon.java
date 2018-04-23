package no.ssb.kostra.control.regnskap.regn0L;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlVersjon extends no.ssb.kostra.control.Control {
    
  private Vector<Integer> recordNumbers = new Vector<Integer>();

  
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
      
    String field_1 = RecordFields.getFieldValue (line, 1);
    String field_5 = RecordFields.getFieldValue (line, 5);    

    boolean errorIsFound = 
            (field_1.equalsIgnoreCase("0D") && ! field_5.equalsIgnoreCase(" "));

    if (errorIsFound)
      recordNumbers.add (new Integer (lineNumber));
    
    return errorIsFound;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
      
    String errorReport = 
           "Kontroll 12, Korrekt versjon av filuttrekk:" + lf + lf;
    
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0) {
        
      errorReport += "\tKRITISK FEIL: filuttrekk for balanseregnskap" +
                     lf + "\tfor sÃ¦rbedrifter MÃ… IKKE rapporteres på  0D. "
                     + lf;
      
      if (numOfRecords <= 10) {
          
        errorReport += 
                "\t\t(Gjelder følgende record" + (numOfRecords == 1 ? "" : "s") + ":";
        for (int i=0; i<numOfRecords; i++)
          errorReport += " " + recordNumbers.elementAt(i);
        
        errorReport += ")";
      }
      errorReport += lf + "\tKorreksjon: Rett opp betegnelsen av filen til korrekt verdi for balanseregnskapet for sÃ¦rbedrifter 0L." + lf + lf;
    }
    return errorReport;
  }

  
  
  
  public boolean foundError() {
    return recordNumbers.size() > 0;
  } 

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
  
}