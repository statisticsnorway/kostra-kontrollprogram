package no.ssb.kostra.control.regnskap.regn0I;

import no.ssb.kostra.control.Constants;

final class Control190 extends no.ssb.kostra.control.Control {

  private int sumForControl_17_a = 0;
  private int sumForControl_17_b = 0;
  private int sumForControl_17_2 = 0;  
  private final int MAX_DIFF = 30;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    boolean lineControlled = false;
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue (line);
      
    } catch (Exception e) {
      return lineControlled;
    }
  
    if (doControl_17_a (line)) {
      lineControlled = true;
      sumForControl_17_a += belop;
    }
    if (doControl_17_b (line)) {
      lineControlled = true;
      sumForControl_17_b += belop;
    }
    if(doControl_17_2(line)) {
      lineControlled = true;
      sumForControl_17_2 += belop;
    }
    return lineControlled;
  }
  
  public String getErrorReport (int totalLineNumber) {
    String errorReport = "Kontroll 17, funksjon 190:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse i interne serviceenheter (funksjon 190): " + lf;
          if (Math.abs (sumForControl_17_a + sumForControl_17_b) >= MAX_DIFF) {
            errorReport += "\t\t" + (sumForControl_17_a + sumForControl_17_b) + "' (Kontoklasse 3)" + lf;
          }           
          if (Math.abs (sumForControl_17_2) >= MAX_DIFF) {
            errorReport += "\t\t" + sumForControl_17_2 + "' (Kontoklasse 4)" + lf;
          }           
          errorReport += "\tKorreksjon: Rett opp i fila slik at funksjon 190 går i 0." +lf + lf;
    }    
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_17_a + sumForControl_17_b) >= MAX_DIFF || Math.abs (sumForControl_17_2) >= MAX_DIFF;
  }  
  
  private boolean doControl_17_a (String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      String funksjon = RecordFields.getFunksjon (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("190");
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }
  
  private boolean doControl_17_b (String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      String funksjon = RecordFields.getFunksjon (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("190");
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
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
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
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
