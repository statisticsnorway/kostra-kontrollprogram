package no.ssb.kostra.control.regnskap.regn0G;

import no.ssb.kostra.control.Constants;

final class ControlSummering extends no.ssb.kostra.control.Control {
  
  
  private int sumAktiva = 0;
  private int sumPassiva = 0;
  private final int MAX_DIFF = 10;

  
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    boolean lineControlled = false;
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue (line);
      
    } catch (Exception e) {
      return lineControlled;
    }
  
    if (isAktiva (line)) {
    
      lineControlled = true;
      sumAktiva += belop;
    }
    
    if (isPassiva (line)) {
    
      lineControlled = true;
      sumPassiva += belop;
    }
    
    return lineControlled;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
  
    String errorReport = "Kontroll 7, summeringskontroll:" + lf + lf;
    
    if (foundError()) {
    
      errorReport += "\tFeil: Differanse mellom aktiva og passiva: " + 
          (sumAktiva + sumPassiva) + "'";
      errorReport += " (Aktiva: " + sumAktiva + "', ";
      errorReport += "passiva: " + sumPassiva + "')." + lf;
      
    } else {
      
      errorReport += "\tIngen feil funnet." + lf;
      
    }   
    errorReport += lf;
    
    return errorReport;
  }

  
  
  
  public boolean foundError() {  
    return Math.abs (sumAktiva + sumPassiva) >= MAX_DIFF;
  }

  
  
  
  private boolean isAktiva (String line) {
  
    try {
    
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("5");
      boolean rettFunksjon = (funksjon >= 10 && funksjon <= 27);
      
      return rettKontoklasse && rettFunksjon;
      
    } catch (Exception e) {    
      //Maa logges!
      return false;
    }    
  }

  
  
  
  private boolean isPassiva (String line) {
  
    try {
    
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("5");
      boolean rettFunksjon = (funksjon >= 31 && funksjon <= 5990);
      
      return rettKontoklasse && rettFunksjon;
      
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }

  
  
  
  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }

  
} // End class ControlSummering.
