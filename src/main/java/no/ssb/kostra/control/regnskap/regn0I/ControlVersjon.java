package no.ssb.kostra.control.regnskap.regn0I;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlVersjon extends no.ssb.kostra.control.Control {
  
  private Vector<Integer> recordNumbers = new Vector<Integer>();

  
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    
    String field_1 = RecordFields.getFieldValue (line, 1);
    String field_5 = RecordFields.getFieldValue (line, 5);

    boolean lineHasError = 
        field_1.equalsIgnoreCase("0A") && ! field_5.equalsIgnoreCase(" ");
        
    if (lineHasError)
      recordNumbers.add (new Integer (lineNumber));

    return lineHasError;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
    
    String errorReport = lf + 
       "Kontroll 19, korrekt versjon av filuttrekket for regnskapet:" + lf + lf;
    
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0)
    {
      errorReport += "\t KRITISK FEIL: Filuttrekk for sÃ¦rbedrifters regnskap " +
                     "MÃ… IKKE rapporteres på 0A." + lf;

      if (numOfRecords <= 10)
      {
        errorReport += "(Gjelder følgende record" + (numOfRecords == 1 ? "" : "s") + ":";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + recordNumbers.elementAt(i);
        }
        errorReport += ")";
      }
      errorReport += lf + "\tKorreksjon: Rett opp betegnelsen av filen til korrekt verdi for bevilgningsregnskapet for sÃ¦rbedrifter = 0I." + lf + lf;
    }
    return errorReport;
  }

  
  
  
  public boolean foundError()
  {
    return recordNumbers.size() > 0;
  }  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}
