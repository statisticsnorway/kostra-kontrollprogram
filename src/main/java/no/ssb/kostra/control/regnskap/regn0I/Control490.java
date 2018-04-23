package no.ssb.kostra.control.regnskap.regn0I;

import no.ssb.kostra.control.Constants;

final class Control490 extends no.ssb.kostra.control.Control {

  private int sumForControl_21 = 0;
  boolean lineControlled = false;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    if(doControl_21(line)) {
      try {
        sumForControl_21 += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      } catch (Exception e) {}
    }
      
    return lineControlled;
  }
  
  public String getErrorReport(int totalLineNumber) {
    String errorReport = "Kontroll 21, art 490:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse i reserverte bevilgninger/avsetninger (art 490): " +
                     sumForControl_21 + "'" + lf + 
          "\tKorreksjon: Rett opp i fila slik at art 490 går i 0." + lf + lf;  
    }    
    return errorReport;
  }

  public boolean foundError()
  {
    return lineControlled && sumForControl_21 != 0;
  }  
  
  private boolean doControl_21(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse(line);
      int funksjon = RecordFields.getFunksjonIntValue(line);
      String art = RecordFields.getArt(line);
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3") || kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = (funksjon >= 100) && (funksjon <= 899);
      boolean rettArt = art.equalsIgnoreCase("490");
      return (rettKontoklasse && rettFunksjon && rettArt);
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }
  
  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }

}