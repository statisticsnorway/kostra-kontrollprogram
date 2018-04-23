package no.ssb.kostra.control.regnskap.regn0K;

import no.ssb.kostra.control.Constants;

final class ControlFunksjon465 extends no.ssb.kostra.control.Control {

  private int sumForControl = 0;
  private int sumForControl_B = 0;
  private final int MAX_POS_DIFF = 30;
  private final int MAX_NEG_DIFF = -30;
  

  boolean lineControlled = false;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    if(isTheRightRecord(line)) {
      try {
        sumForControl += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      } catch (Exception e) {}
    }
      
    if(isTheRightRecord_B(line)) {
      try {
        sumForControl_B += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      } catch (Exception e) {}
    }

  return lineControlled;
  }
  
  public String getErrorReport(int totalLineNumber) {
    String errorReport = "Kontroll 19, funksjon 465:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse på funksjon for interkommunale samarbeid (Â§ 27- samarbeid) (funksjon 465): " + lf;
      if (sumForControl_B >= MAX_POS_DIFF || sumForControl_B <= MAX_NEG_DIFF) {
          errorReport += "\t\t" + sumForControl_B + "' (Kontoklasse 3)" + lf; 
      }
      if (sumForControl >= MAX_POS_DIFF || sumForControl <= MAX_NEG_DIFF) {
          errorReport += "\t\t" + sumForControl + "' (Kontoklasse 4)" + lf; 
      }
         errorReport += "\tKorreksjon: Rett opp i fila slik at funksjon 465 går i 0." + lf + lf;  
    }    
    return errorReport;
  }

  public boolean foundError()
  {
    return lineControlled && ((sumForControl >= MAX_POS_DIFF || sumForControl <= MAX_NEG_DIFF) || (sumForControl_B >= MAX_POS_DIFF || sumForControl_B <= MAX_NEG_DIFF));
  }  
  
  private boolean isTheRightRecord(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse(line);
      String funksjon = RecordFields.getFunksjon(line);
      int art = RecordFields.getArtIntValue(line);
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("465");
      boolean rettArt = ((art >= 10) && (art <= 590)) || ((art >= 600) && (art <= 990));
      return (rettKontoklasse && rettFunksjon && rettArt);
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }
  
  private boolean isTheRightRecord_B(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse(line);
      String funksjon = RecordFields.getFunksjon(line);
      int art = RecordFields.getArtIntValue(line);
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
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