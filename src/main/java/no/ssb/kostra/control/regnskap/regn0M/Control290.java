package no.ssb.kostra.control.regnskap.regn0M;

import no.ssb.kostra.control.Constants;

final class Control290 extends no.ssb.kostra.control.Control {

  private int sumForControl_18_1 = 0;
  private int sumForControl_18_2 = 0;
  private final int MAX_DIFF = 30;

  boolean lineControlled = false;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    //Kontrollen skal ikke utfores for bydelene i Oslo.
    //Henter regionnr. fra aktuell record (line).
    String region_nr = RecordFields.getRegion(line);
    if (region_nr.substring(0,4).equalsIgnoreCase("0301") && 
        !region_nr.substring(4,6).equalsIgnoreCase("00")) {
      return false;
    }
      
    if(doControl_18_1(line)) {
      try {
        sumForControl_18_1 += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      } catch (NumberFormatException e) {}
    }
      
    if(doControl_18_2(line)) {
      try {
        sumForControl_18_2 += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      } catch (NumberFormatException e) {}
    }
    return lineControlled;
  }
  
  public String getErrorReport(int totalLineNumber) {
    String errorReport = "Kontroll 18, funksjon 290:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse på funksjon for interkommunale samarbeid (§ 27- samarbeid) (funksjon 290): " + lf;
      if (Math.abs (sumForControl_18_1) >= MAX_DIFF)
      {
        errorReport += "\t\t" + Math.abs (sumForControl_18_1) + "' (Kontoklasse 4)" + lf;
      }
      if (Math.abs (sumForControl_18_2) >= MAX_DIFF)
      {
        errorReport += "\t\t" + Math.abs (sumForControl_18_2) + "' (Kontoklasse 3)" + lf;
      }
      errorReport += "\tKorreksjon: Rett opp i fila slik at funksjon 290 går i 0." + lf + lf;  
          
    }    
    return errorReport;
  }

  public boolean foundError()
  {
    return lineControlled && 
        (Math.abs (sumForControl_18_1) >= MAX_DIFF || Math.abs (sumForControl_18_2) >= MAX_DIFF);
  }  
  
  private boolean doControl_18_1(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse(line);
      String funksjon = RecordFields.getFunksjon(line);
      int art = RecordFields.getArtIntValue(line);
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("290");
      boolean rettArt = ((art >= 10) && (art <= 590)) || ((art >= 600) && (art <= 990));
      return (rettKontoklasse && rettFunksjon && rettArt);
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }
  
  private boolean doControl_18_2(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse(line);
      String funksjon = RecordFields.getFunksjon(line);
      int art = RecordFields.getArtIntValue(line);
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("290");
      boolean rettArt = ((art >= 10) && (art <= 590)) || ((art >= 600) && (art <= 990));
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