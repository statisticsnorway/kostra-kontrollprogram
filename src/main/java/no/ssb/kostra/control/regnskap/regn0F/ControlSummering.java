package no.ssb.kostra.control.regnskap.regn0F;

import no.ssb.kostra.control.Constants;

final class ControlSummering extends no.ssb.kostra.control.Control
{
  private int sumForControl_12_1_a = 0;
  private int sumForControl_12_1_b = 0;
  private int sumForControl_12_2_a = 0;
  private int sumForControl_12_2_b = 0;
  private int sumForControl_12_3 = 0;
/*
  private int sumForControl_12_3_a = 0;
  private int sumForControl_12_3_b = 0;
*/
  private int sumForControl_12_4 = 0;
  private int sumForControl_12_5 = 0;

  private final int MAX_DIFF = 30;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineControlled = false;
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue (line);
      
    } catch (Exception e) {
      return lineControlled;
    }
    
  
    if (doControl_12_1_a (line))
    {
      lineControlled = true;
      sumForControl_12_1_a += belop;
    }
    if (doControl_12_1_b (line))
    {
      lineControlled = true;
      sumForControl_12_1_b += belop;
    }
    if (doControl_12_2_a (line))
    {
      lineControlled = true;
      sumForControl_12_2_a += belop;
    }
    if (doControl_12_2_b (line))
    {
      lineControlled = true;
      sumForControl_12_2_b += belop;
    }
    if (doControl_12_3 (line))
    {
      lineControlled = true;
      sumForControl_12_3 += belop;
    }
/*    if (doControl_12_3_a (line))
    {
      lineControlled = true;
      sumForControl_12_3_a += belop;
    }
    if (doControl_12_3_b (line))
    {
      lineControlled = true;
      sumForControl_12_3_b += belop;
    }
*/
    if (doControl_12_4 (line))
    {
      lineControlled = true;
      sumForControl_12_4 += belop;
    }
    if (doControl_12_5 (line))
    {
      lineControlled = true;
      sumForControl_12_5 += belop;
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 11, summeringskontroller:" + lf + lf;
    if (Math.abs (sumForControl_12_1_a + sumForControl_12_1_b) >= MAX_DIFF)
    {
      errorReport += "\tFeil: Differanse mellom inntekter og utgifter i " + 
      "investeringsregnskapet: " + (sumForControl_12_1_a + sumForControl_12_1_b) + "'";
      errorReport += " (Inntekter: " + sumForControl_12_1_b + "', ";
      errorReport += "utgifter: " + sumForControl_12_1_a + "')." + lf;
    }
    if (Math.abs (sumForControl_12_2_a + sumForControl_12_2_b) >= MAX_DIFF)
    {
      errorReport += "\tFeil: Differanse mellom inntekter og utgifter i " + 
      "driftsregnskapet: " + (sumForControl_12_2_a + sumForControl_12_2_b) + "'";
      errorReport += " (Inntekter: " + sumForControl_12_2_b + "', ";
      errorReport += "utgifter: " + sumForControl_12_2_a + "')." + lf;
    }
    if (sumForControl_12_3 <= 0)
    {
      errorReport += "\tFeil: Det er ikke registrert utgifter i driftsregnskapet." + lf; //"\tFeil: Det er ikke registrert beløp i driftsregnskapet." + lf;
    }
/*
    if (sumForControl_12_3_a <= 0 || sumForControl_12_3_b > 0)
    {
      errorReport += "\tFeil: Det er ikke registrert utgifter i driftsregnskapet." + lf; //"\tFeil: Det er ikke registrert beløp i driftsregnskapet." + lf;
    }
*/
    if (sumForControl_12_4 >= 0) //(sumForControl_12_4 >= 0)
    {
      errorReport += "\tFeil: Det er ikke registrert inntekter i driftsregnskapet." + lf;  //"\tFeil: Tilskudd fra staten mangler." + lf;
    }
    if (sumForControl_12_5 >= 0)  //(sumForControl_12_5 >= 0)
    {
      errorReport += "\tFeil: Tilskudd fra kommunen mangler. Kommunale tilskudd skal føres på funksjon 089, og art 830." + lf;
    }
    errorReport += lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_12_1_a + sumForControl_12_1_b) >= MAX_DIFF ||
           Math.abs (sumForControl_12_2_a + sumForControl_12_2_b) >= MAX_DIFF ||
           sumForControl_12_3 <= 0 ||
/*
           sumForControl_12_3_a <= 0 ||
           sumForControl_12_3_b > 0 ||
*/
           sumForControl_12_4 >= 0 || //sumForControl_12_4 >= 0 ||
           sumForControl_12_5 >= 0; //sumForControl_12_5 >= 0;
  }

  private boolean doControl_12_1_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
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

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
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

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
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

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
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

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }
/*
  private boolean doControl_12_3_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = (art >= 10 && art <= 590);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }
  
  private boolean doControl_12_3_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }
*/

  private boolean doControl_12_4 (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }
/*  
  private boolean doControl_12_4 (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = (art >= 600 && art <= 990);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }
*/
  
  private boolean doControl_12_5 (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 89) || funksjon == 89 );
      boolean rettArt = art.equalsIgnoreCase("830");
      
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
