package no.ssb.kostra.control.regnskap.regn0A;

import no.ssb.kostra.control.Constants;

final class ControlSummering extends no.ssb.kostra.control.Control
{
  @SuppressWarnings("unused") // Den ER brukt i doControl
  private int lines = 0;
  
  private int sumForControl_12_1_a = 0;
  private int sumForControl_12_1_b = 0;
  private int sumForControl_12_2_a = 0;
  private int sumForControl_12_2_b = 0;
  private int sumForControl_12_3 = 0;
  private int sumForControl_12_4 = 0;
  private int sumForControl_12_5 = 0;
  private int sumForControl_12_6 = 0;

  private final int MAX_DIFF = 30;

  boolean lineControlled = false;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    //Kontrollen skal ikke utfores for bydelene i Oslo.
    //Henter regionnr. fra aktuell record (line).
    String region_nr = RecordFields.getRegion(line);
    if (region_nr.substring(0,4).equalsIgnoreCase("0301") && 
        !region_nr.substring(4,6).equalsIgnoreCase("00")) {
      return false;
    }
    
    
    lines += 1;
    if (doControl_12_1_a (line))
    {
      lineControlled = true;
      sumForControl_12_1_a += RecordFields.getBelopIntValue (line);
    }
    if (doControl_12_1_b (line))
    {
      lineControlled = true;
      sumForControl_12_1_b += RecordFields.getBelopIntValue (line);
    }
    if (doControl_12_2_a (line))
    {
      lineControlled = true;
      sumForControl_12_2_a += RecordFields.getBelopIntValue (line);
    }
    if (doControl_12_2_b (line))
    {
      lineControlled = true;
      sumForControl_12_2_b += RecordFields.getBelopIntValue (line);
    }
    if (doControl_12_3 (line))
    {
      lineControlled = true;
      sumForControl_12_3 += RecordFields.getBelopIntValue (line);
    }
    if (doControl_12_4 (line))
    {
      lineControlled = true;
      sumForControl_12_4 += RecordFields.getBelopIntValue (line);
    }
    if (doControl_12_5 (line))
    {
      lineControlled = true;
      sumForControl_12_5 += RecordFields.getBelopIntValue (line);
    }
    if (doControl_12_6 (line))
    {
      lineControlled = true;
      sumForControl_12_6 += RecordFields.getBelopIntValue (line);
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 12, summeringskontroller:" + lf;
    if (Math.abs (sumForControl_12_1_a + sumForControl_12_1_b) >= MAX_DIFF)
    {
      errorReport += lf + "\tFeil: Differanse mellom inntekter og utgifter i " + 
      "investeringsregnskapet: " + (sumForControl_12_1_a + sumForControl_12_1_b) + "'";
      errorReport += " (Inntekter: " + sumForControl_12_1_b + "', ";
      errorReport += "utgifter: " + sumForControl_12_1_a + "')." + lf +
              "\tKorreksjon: Rett opp differansen mellom inntekter og utgifter i investeringsregnskapet." + lf; 
    }
    if (Math.abs (sumForControl_12_2_a + sumForControl_12_2_b) >= MAX_DIFF)
    {
      errorReport += lf + "\tFeil: Differanse mellom inntekter og utgifter i " + 
      "driftsregnskapet: " + (sumForControl_12_2_a + sumForControl_12_2_b) + "'";
      errorReport += " (Inntekter: " + sumForControl_12_2_b + "', ";
      errorReport += "utgifter: " + sumForControl_12_2_a + "')." + lf +
              "\tKorreksjon: Rett opp differansen mellom inntekter og utgifter i driftsregnskapet." + lf; 
    }
    if (sumForControl_12_3 <= 0) {
      errorReport += lf + "\tFeil: Det er ikke registrert utgifter i investeringsregnskapet." + lf +
              "\tKorreksjon: Rett opp slik at fila inneholder utgiftsposteringene i investeringsregnskapet." + lf; 
    }
    if(sumForControl_12_4 >= 0){
      errorReport += lf + "\tFeil: Det er ikke registrert inntekter i investeringsregnskapet." + lf +
              "\tKorreksjon: Rett opp slik at fila inneholder inntektsposteringene i investeringsregnskapet." + lf; 
    }
    if (sumForControl_12_5 <= 0) {
      errorReport += lf + "\tFeil: Det er ikke registrert utgifter i driftsregnskapet." + lf +
              "\tKorreksjon: Rett opp slik at fila inneholder utgiftsposteringene i driftsregnskapet." + lf; 
    }
    if(sumForControl_12_6 >= 0) {
      errorReport += lf +"\tFeil: Det er ikke registrert inntekter i driftsregnskapet." + lf +
              "\tKorreksjon: Rett opp slik at fila inneholder inntektsposteringene i driftsregnskapet." + lf; 
    }
    errorReport += lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return lineControlled && 
        (Math.abs (sumForControl_12_1_a + sumForControl_12_1_b) >= MAX_DIFF ||
         Math.abs (sumForControl_12_2_a + sumForControl_12_2_b) >= MAX_DIFF ||
         sumForControl_12_3 <= 0 ||
         sumForControl_12_4 >= 0  ||
         sumForControl_12_5 <= 0 ||
         sumForControl_12_6 >= 0);
  }

  private boolean doControl_12_1_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("0");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_1_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("0");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_2_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_2_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_3 (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("0");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }
  
  private boolean doControl_12_4 (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("0");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_5 (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_12_6 (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}