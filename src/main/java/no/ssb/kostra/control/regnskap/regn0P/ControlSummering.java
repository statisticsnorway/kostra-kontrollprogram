package no.ssb.kostra.control.regnskap.regn0P;

import no.ssb.kostra.control.Constants;

final class ControlSummering extends no.ssb.kostra.control.Control
{
  @SuppressWarnings("unused")
private int lines = 0;
  
  private int sumForControl_12_1_a = 0;
  private int sumForControl_12_1_b = 0;
  private int sumForControl_12_2_a = 0;
  private int sumForControl_12_2_b = 0;
  private int sumForControl_12_3 = 0;
  private int sumForControl_12_4 = 0;
  private int sumForControl_12_5 = 0;
  private int sumForControl_12_6 = 0;

  private boolean field_12_3_isFound = false;
  private final int MAX_DIFF = 30;

  
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasBeenControlled = false;

    int belop = 0;
    try {
      belop = RecordFields.getBelopIntValue (line);
    } catch (Exception e) {
      return lineHasBeenControlled;
    }
              
    lines += 1;
    if (doControl_12_1_a(line))
    {
      lineHasBeenControlled = true;
      sumForControl_12_1_a += belop;
    }
    if (doControl_12_1_b(line))
    {
      lineHasBeenControlled = true;
      sumForControl_12_1_b += belop;
    }
    if (doControl_12_2_a(line))
    {
      lineHasBeenControlled = true;
      sumForControl_12_2_a += belop;
    }
    if (doControl_12_2_b(line))
    {
      lineHasBeenControlled = true;
      sumForControl_12_2_b += belop;
    }
    if (doControl_12_3(line))
    {
      field_12_3_isFound = true;
      lineHasBeenControlled = true;
      sumForControl_12_3 += belop;
    }
    if (doControl_12_4(line))
    {
      lineHasBeenControlled = true;
      sumForControl_12_4 += belop;
    }
    if (doControl_12_5(line))
    {
      lineHasBeenControlled = true;
      sumForControl_12_5 += belop;
    }
    if (doControl_12_6(line))
    {
      lineHasBeenControlled = true;
      sumForControl_12_6 += belop;
    }
    return lineHasBeenControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 12, summeringskontroller:" + lf + lf;
    
    if (Math.abs (sumForControl_12_1_a + sumForControl_12_1_b) >= MAX_DIFF) {
      errorReport += "\tFeil: Differanse mellom inntekter og utgifter i " + 
      "investeringsregnskapet: " + (sumForControl_12_1_a + sumForControl_12_1_b) + "'";
      errorReport += " (Inntekter: " + sumForControl_12_1_b + "', ";
      errorReport += "utgifter: " + sumForControl_12_1_a + "')." + lf +
       "\tKorreksjon: Rett opp differansen mellom inntekter og utgifter i investeringsregnskapet." + lf + lf;
    }
    if (Math.abs (sumForControl_12_2_a + sumForControl_12_2_b) >= MAX_DIFF) {
      errorReport += "\tFeil: Differanse mellom inntekter og utgifter i " + 
      "driftsregnskapet: " + (sumForControl_12_2_a + sumForControl_12_2_b) + "'";
      errorReport += " (Inntekter: " + sumForControl_12_2_b + "', ";
      errorReport += "utgifter: " + sumForControl_12_2_a + "')." + lf +
       "\tKorreksjon: Rett opp differansen mellom inntekter og utgifter i driftsregnskapet." + lf + lf;
    }
    if ((sumForControl_12_3 <= 0 && field_12_3_isFound)) { 
      errorReport += "\tFeil: Det er ikke registrert utgifter i investeringsregnskapet." + lf +
       "\tKorreksjon: Rett opp slik at fila inneholder utgiftsposteringene i investeringsregnskapet" + lf + lf;
    }
    if(sumForControl_12_4 >= 0) {
      errorReport += "\tFeil: Det er ikke registrert inntekter i investeringsregnskapet." + lf +
       "\tKorreksjon: Rett opp slik at fila inneholder inntektsposteringene i investeringsregnskapet." + lf + lf;
    }
    if (sumForControl_12_5 <= 0) {
      errorReport += "\tFeil: Det er ikke registrert utgifter i driftsregnskapet." + lf +
       "\tKorreksjon: Rett opp slik at fila inneholder utgiftsposteringene i driftsregnskapet." + lf + lf;
    }
    if (sumForControl_12_6 >= 0) {
      errorReport += "\tFeil: Det er ikke registrert inntekter i driftsregnskapet." + lf +
       "\tKorreksjon: Rett opp slik at fila inneholder inntektsposteringene i driftsregnskapet." + lf + lf;
    }
    errorReport += lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_12_1_a + sumForControl_12_1_b) >= MAX_DIFF ||
           Math.abs (sumForControl_12_2_a + sumForControl_12_2_b) >= MAX_DIFF ||
           (sumForControl_12_3 <= 0 && field_12_3_isFound) ||
           sumForControl_12_4 >= 0  ||
           (sumForControl_12_5 <= 0) ||
           sumForControl_12_6 >= 0;
  }

  private boolean doControl_12_1_a(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_1_b(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_2_a(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_2_b(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_3(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }
  
  private boolean doControl_12_4(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_5(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_6(String line) {
    try {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    } catch (Exception e) {
      //Maa logges!
      return false;
    }    
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}