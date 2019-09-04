package no.ssb.kostra.control.regnskap.regn0M;

import no.ssb.kostra.control.Constants;

final class Control490 extends no.ssb.kostra.control.Control {

  private int sumForControl_19 = 0;

  boolean lineControlled = false;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    //Kontrollen skal ikke utfores for bydelene i Oslo.
    //Henter regionnr. fra aktuell record (line).
    String region_nr = RecordFields.getRegion(line);
    if (region_nr.substring(0,4).equalsIgnoreCase("0301") && 
        !region_nr.substring(4,6).equalsIgnoreCase("00")) {
      return false;
    }
   
    String art = RecordFields.getArt(line);
    if (art.equalsIgnoreCase("490")) {
      sumForControl_19 += RecordFields.getBelopIntValue(line);
      lineControlled = true;
    }
       
    return lineControlled;
  }
  
  public String getErrorReport(int totalLineNumber) {
    String errorReport = "Kontroll 19, art 490:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse i reserverte bevilgninger/avsetninger (art 490): ";
      errorReport += sumForControl_19 + "'" + lf + 
              "\tKorreksjon: Rett opp i fila slik at art 490 går i 0." + lf + lf;  
          
    }    
    return errorReport;
  }

  public boolean foundError()
  {
    return lineControlled && sumForControl_19 != 0; 
  }  
  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}