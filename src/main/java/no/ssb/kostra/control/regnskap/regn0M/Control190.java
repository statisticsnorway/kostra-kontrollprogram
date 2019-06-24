package no.ssb.kostra.control.regnskap.regn0M;

import no.ssb.kostra.control.Constants;

final class Control190 extends no.ssb.kostra.control.Control {

  private int sumForControl_17_a = 0;
  private int sumForControl_17_b = 0;
  private int sumForControl_17_2 = 0;
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
      
    if(doControl_17_a(line)) {
      lineControlled = true;
      sumForControl_17_a += RecordFields.getBelopIntValue (line);
    }
    if(doControl_17_b(line)) {
      lineControlled = true;
      sumForControl_17_b += RecordFields.getBelopIntValue (line);
    }
    if(doControl_17_2(line)) {
      lineControlled = true;
      sumForControl_17_2 += RecordFields.getBelopIntValue (line);
    }
    return lineControlled;
  }
  
  public String getErrorReport(int totalLineNumber) {
    String errorReport = "Kontroll 17, funksjon 190:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse i interne serviceenheter (funksjon 190): " + lf;
          if (Math.abs (sumForControl_17_2) >= MAX_DIFF) {
            errorReport += "\t\t" + sumForControl_17_2 + "' (Kontoklasse 0)" + lf;
          }           
          if (Math.abs (sumForControl_17_a + sumForControl_17_b) >= MAX_DIFF) {
            errorReport += "\t\t" + (sumForControl_17_a + sumForControl_17_b) + "' (Kontoklasse 1)" + lf;
          }           
          errorReport += "\tKorreksjon: Rett opp i fila slik at funksjon 190 går i 0." +lf + lf;
    }    
    return errorReport;
  }

  public boolean foundError()
  {
    return lineControlled && 
        (Math.abs (sumForControl_17_a + sumForControl_17_b) >= MAX_DIFF || Math.abs (sumForControl_17_2) >= MAX_DIFF);
  }  
  
  private boolean doControl_17_a(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse(line);
      String funksjon = RecordFields.getFunksjon(line);
      int art = RecordFields.getArtIntValue(line);
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("190");
      boolean rettArt = ((art >= 10) && (art <= 590));
      return (rettKontoklasse && rettFunksjon && rettArt);
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }
  
  private boolean doControl_17_b(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse(line);
      String funksjon = RecordFields.getFunksjon(line);
      int art = RecordFields.getArtIntValue(line);
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("190");
      boolean rettArt = ((art >= 600) && (art <= 990));
      return (rettKontoklasse && rettFunksjon && rettArt);
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_17_2(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse(line);
      String funksjon = RecordFields.getFunksjon(line);
      int art = RecordFields.getArtIntValue(line);
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("0");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("190");
      boolean rettArt = ((art >= 10) && (art <= 590) || (art >= 600) && (art <= 990));
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