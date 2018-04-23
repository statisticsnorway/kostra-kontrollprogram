package no.ssb.kostra.control.regnskap.regn0I;

import no.ssb.kostra.control.Constants;

final class ControlInterneTransaksjoner extends no.ssb.kostra.control.Control {
  
  private int sumForControl_18_1 = 0;
  private int sumForControl_18_2 = 0;
  private int sumForControl_18_3 = 0;
  private int sumForControl_18_4_a = 0;
  private int sumForControl_18_4_b = 0;


  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    
    boolean lineControlled = false;
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue (line);
      
    } catch (Exception e) {
      return lineControlled;
    }
  
    if (isTargetForControl_18_1 (line)) {
      
      lineControlled = true;
      sumForControl_18_1 += belop;
    }
    
    if (isTargetForControl_18_2 (line)) {
      
      lineControlled = true;
      sumForControl_18_2 += belop;
    }
    
    if (isTargetForControl_18_3 (line)) {
      
      lineControlled = true;
      sumForControl_18_3 += belop;
    }
    
    if (isTargetForControl_18_4_a (line)) {
      
      lineControlled = true;
      sumForControl_18_4_a += belop;
    }
    
    if (isTargetForControl_18_4_b (line)) {
      
      lineControlled = true;
      sumForControl_18_4_b += belop;
    }
    
    return lineControlled;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
    
    String errorReport = lf +  "Kontroll 18, interne transaksjoner " +
                         "mellom særbedrift og eierkommuner:" + lf + lf;
    
    if (sumForControl_18_1 == 0)
      errorReport += "\tMerk: Det er IKKE registrert interne transaksjoner " +
                     "mellom særbedrift og eierkommuner på de regnskapsarter" +
                     " som er definert i kontoplanen." + lf;
    
    if (sumForControl_18_2 != 0)
      errorReport += "\tMerk: Registrerte interne kjøp fra eierkommuner " +
                     "må avstemmes med eierkommunene." + lf;
    
    if (sumForControl_18_3 != 0)
      errorReport += "\tMerk: Registrerte interne salg til eierkommuner " +
                     "må avstemmes med eierkommunene." + lf;
    
    if (sumForControl_18_4_a == 0 && sumForControl_18_4_b != 0)
      errorReport += "\tMerk: OBS - salgsinntekter artene 600-660 " +
                     "skal ikke benyttes for interne transaksjoner." + lf;
    
    errorReport += lf + "\tKontroll 18 hindrer ikke innsending." + lf;
    
    return errorReport;
  }

  
  
  
  public boolean foundError() {    
    return  (sumForControl_18_1 == 0 || sumForControl_18_2 != 0 ||
             sumForControl_18_3 != 0 || 
             (sumForControl_18_4_a == 0 && sumForControl_18_4_b != 0));
  }  

  
  
  
  private boolean isTargetForControl_18_1 (String line) {    
    try {
      
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art.equalsIgnoreCase("375") ||
                         art.equalsIgnoreCase("380") ||
                         art.equalsIgnoreCase("475") ||
                         art.equalsIgnoreCase("480") ||
                         art.equalsIgnoreCase("775") ||
                         art.equalsIgnoreCase("780") ||
                         art.equalsIgnoreCase("880") ||
                         art.equalsIgnoreCase("895"));
      
      return rettKontoklasse && rettFunksjon && rettArt;
      
    } catch (Exception e) {      
      //Maa logges!
      return false;
    }    
  }

  
  
  
  private boolean isTargetForControl_18_2 (String line) {
    try {
      
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art.equalsIgnoreCase("375") ||
                         art.equalsIgnoreCase("380") ||
                         art.equalsIgnoreCase("475") ||
                         art.equalsIgnoreCase("480"));
      
      return rettKontoklasse && rettFunksjon && rettArt;
      
    } catch (Exception e) {      
      //Maa logges!
      return false;
    }    
  }

  
  
  
  private boolean isTargetForControl_18_3 (String line) {
    try {
      
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art.equalsIgnoreCase("775") ||
                         art.equalsIgnoreCase("780") ||
                         art.equalsIgnoreCase("880") ||
                         art.equalsIgnoreCase("895"));
      
      return rettKontoklasse && rettFunksjon && rettArt;
      
    } catch (Exception e) {      
      //Maa logges!
      return false;
    }    
  }

  
  
  
  private boolean isTargetForControl_18_4_a (String line) {
    try {
      
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art.equalsIgnoreCase("775") ||
                         art.equalsIgnoreCase("780") ||
                         art.equalsIgnoreCase("880") ||
                         art.equalsIgnoreCase("895"));
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }
  
  
  
  
  private boolean isTargetForControl_18_4_b (String line) {
    try {
      
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue(line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art >= 600 && art <= 660);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}
