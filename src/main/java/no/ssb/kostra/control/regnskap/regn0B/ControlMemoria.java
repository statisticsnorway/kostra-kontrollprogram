package no.ssb.kostra.control.regnskap.regn0B;

import no.ssb.kostra.control.Constants;

final class ControlMemoria extends no.ssb.kostra.control.Control
{
  @SuppressWarnings("unused")
private int lines = 0;
  
  private int sumForControl_a = 0;
  private int sumForControl_b = 0;
  private int sumForControl_c = 0;
  private int sumForControl_d = 0;

  private final int MAX_DIFF = 10;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineControlled = false;
  
    lines += 1;
    
    try {

      if (isTargetForControl_a (line))
      {
        sumForControl_a += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }
      if (isTargetForControl_b (line))
      {
        sumForControl_b += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }
      if (isTargetForControl_c (line))
      {
        sumForControl_c += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }
      if (isTargetForControl_d (line))
      {
        sumForControl_d += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }
      
    } catch (Exception e) {
    
      // Exception her er sannsynligvis NumberFormatException.
      // Det er da OK at lineControlled = false;
    }    
    
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 11, memoriakonti:" + lf + lf;
    
    if (Math.abs (sumForControl_a + sumForControl_b + sumForControl_c + sumForControl_d) >= MAX_DIFF)
    {
      errorReport += "\tAdvarsel: Differanse mellom memoriakonti og motkonto for memoriakonti: " + 
          (sumForControl_a + sumForControl_b + sumForControl_c + sumForControl_d) + "'";
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_a + sumForControl_b + sumForControl_c + sumForControl_d) >= MAX_DIFF;
  }

  private boolean isTargetForControl_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("2");
      boolean rettFunksjon = (funksjon == 9100);
      boolean rettArt = (art >= 0 && art <= 999);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean isTargetForControl_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("2");
      boolean rettFunksjon = (funksjon == 9200);
      boolean rettArt = (art >= 0 && art <= 999);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean isTargetForControl_c (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("2");
      boolean rettFunksjon = (funksjon == 9999);
      boolean rettArt = (art >= 0 && art <= 999);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean isTargetForControl_d (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("2");
      boolean rettFunksjon = (funksjon == 9110);
      boolean rettArt = (art >= 0 && art <= 999);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }

}