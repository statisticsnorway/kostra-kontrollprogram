package no.ssb.kostra.control.regnskap.regn0I;

import no.ssb.kostra.control.Constants;

final class Control290 extends no.ssb.kostra.control.Control {

  private int sumForControl_20 = 0;
  private int sumForControl_20_2 = 0;
  private final int MAX_DIFF = 30;

  boolean lineControlled = false;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    if(doControl_20(line)) {
      try {
        sumForControl_20 += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      } catch (Exception e) {}
    }
      
    if(doControl_20_2(line)) {
      try {
        sumForControl_20_2 += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      } catch (Exception e) {}
    }

    return lineControlled;
  }
  
  public String getErrorReport(int totalLineNumber) {
    String errorReport = "Kontroll 20, funksjon 290:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse på funksjon for interkommunale samarbeid (§ 27- samarbeid) (funksjon 290): " + lf;
      if (Math.abs (sumForControl_20_2) >= MAX_DIFF) {
        errorReport += "\t\t" + Math.abs (sumForControl_20_2) + "' (Kontoklasse 3)" + lf;
      }
      if (Math.abs (sumForControl_20) >= MAX_DIFF) {
         errorReport += "\t\t" + Math.abs (sumForControl_20) + "' (Kontoklasse 4)" + lf;
      }
          errorReport += "\tKorreksjon: Rett opp i fila slik at funksjon 290 går i 0." + lf + lf;  
    }    
    return errorReport;
  }

  public boolean foundError()
  {
    return lineControlled && (Math.abs (sumForControl_20) >= MAX_DIFF || Math.abs (sumForControl_20_2) >= MAX_DIFF);
  }  
  
  private boolean doControl_20(String line) {
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
  
  private boolean doControl_20_2(String line) {
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