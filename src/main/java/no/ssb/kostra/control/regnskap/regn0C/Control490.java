package no.ssb.kostra.control.regnskap.regn0C;

import no.ssb.kostra.control.Constants;

final class Control490 extends no.ssb.kostra.control.Control {

  private int sumForControl_17_a = 0;
  private int sumForControl_17_b = 0;
  private int sumForControl_17_2 = 0;
  private final int MAX_DIFF = 30;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
  
    boolean lineControlled = false;

    try {
    
      if (doControl_17_a (line)) {
        sumForControl_17_a += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }
      if (doControl_17_b (line)) {
        sumForControl_17_b += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }
    
      if (doControl_17_2 (line)) {
        sumForControl_17_2 += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }

    } catch (Exception e) {}
    
    return lineControlled;
  }
  
  public String getErrorReport (int totalLineNumber) {
    String errorReport = "Kontroll 17, funksjon 490:" + lf + lf;
    if (foundError())
    {     
          if (Math.abs (sumForControl_17_2) >= MAX_DIFF) {
        	errorReport += "\tFeilmelding:" + "\tDifferanse i interne serviceenheter (funksjon 490):" + lf;
            errorReport += "\t\t" + sumForControl_17_2 + "' (Kontoklasse 1)" + lf;
            errorReport +=  "\tKorreksjon:" + "\tRett opp i fila slik at funksjon 490 går i 0." + lf + lf + lf;
          }

          if (Math.abs (sumForControl_17_a + sumForControl_17_b) >= MAX_DIFF) {
        	errorReport += "\tFeilmelding:" + "\tDifferanse i interne serviceenheter (funksjon 490):" + lf;
            errorReport += "\t\t" + (sumForControl_17_a + sumForControl_17_b) + "' (Kontoklasse 0)" + lf;
            errorReport +=  "\tKorreksjon:" + "\tRett opp i fila slik at funksjon 490 går i 0." + lf + lf;
          }           
    }    
    return errorReport;
  }

//  public String getErrorReport (int totalLineNumber) {
//	    String errorReport = "Kontroll 17, funksjon 490:" + lf + lf;
//	    if (foundError())
//	    {
//	      errorReport += "\tFeil: Differanse i interne serviceenheter (funksjon 490): " + lf;
//	          if (Math.abs (sumForControl_17_2) >= MAX_DIFF) {
//	            errorReport += "\t\t" + sumForControl_17_2 + "' (Kontoklasse 0)" + lf;
//	          }           
//	          if (Math.abs (sumForControl_17_a + sumForControl_17_b) >= MAX_DIFF) {
//	            errorReport += "\t\t" + (sumForControl_17_a + sumForControl_17_b) + "' (Kontoklasse 1)" + lf;
//	          }           
//	          errorReport +=  "\tKorreksjon: Rett opp i fila slik at funksjon 490 går i 0." + lf + lf;
//	    }    
//	    return errorReport;
//	  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_17_a + sumForControl_17_b) >= MAX_DIFF || Math.abs (sumForControl_17_2) >= MAX_DIFF;
  }  
  
  private boolean doControl_17_a (String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      String funksjon = RecordFields.getFunksjon (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("490");
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

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("490");
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
      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("0");
      boolean rettFunksjon = funksjon.equalsIgnoreCase("490");
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