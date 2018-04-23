package no.ssb.kostra.control.regnskap.regn0C;

import no.ssb.kostra.control.Constants;

final class Control465 extends no.ssb.kostra.control.Control {

  private int sumForControl_18_1 = 0;
  private int sumForControl_18_2 = 0;
  private final int MAX_DIFF = 30;

  boolean lineControlled = false;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        
    if(doControl_18_1(line)) {
      try {
        sumForControl_18_1 += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      } catch (Exception e) {}
    }
    
    if(doControl_18_2(line)) {
      try {
        sumForControl_18_2 += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      } catch (Exception e) {}
    }
    return lineControlled;
  }
  
  public String getErrorReport(int totalLineNumber) {
    String errorReport = "Kontroll 18, funksjon 465:" + lf + lf;
    if (foundError())
    {
        if (Math.abs (sumForControl_18_1) >= MAX_DIFF) {
          errorReport += "\tFeilmelding:" + "\tDifferanse på funksjon for interkommunale samarbeid (§ 27- samarbeid) (funksjon 465):" + lf;
          errorReport += "\t\t" + Math.abs (sumForControl_18_1) + "' (kontoklasse 1)" + lf;
          errorReport +=  "\tKorreksjon:" + "\tRett opp i fila slik at funksjon 465 går i 0." + lf + lf + lf;
        }
        
        if (Math.abs (sumForControl_18_2) >= MAX_DIFF) {
          errorReport += "\tFeilmelding:" + "\tDifferanse på funksjon for interkommunale samarbeid (§ 27- samarbeid) (funksjon 465):" + lf;
          errorReport += "\t\t" + Math.abs (sumForControl_18_2) + "' (kontoklasse 0)" + lf;  
          errorReport += "\tKorreksjon: Rett opp i fila slik at funksjon 465 går i 0." + lf + lf;
        }
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
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("0");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("465");
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
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("465");
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