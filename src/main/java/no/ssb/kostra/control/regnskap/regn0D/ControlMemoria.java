package no.ssb.kostra.control.regnskap.regn0D;

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
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue (line);      
      
    } catch (Exception e) {      
      return lineControlled;      
    }
    
    if (isTargetForControl_a (line))
    {
      lineControlled = true;
      sumForControl_a += belop;
    }
    if (isTargetForControl_b (line))
    {
      lineControlled = true;
      sumForControl_b += belop;
    }
    if (isTargetForControl_c (line))
    {
      lineControlled = true;
      sumForControl_c += belop;
    }
    if (isTargetForControl_d (line))
    {
      lineControlled = true;
      sumForControl_d += belop;
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 11, memoriakonti:" + lf + lf;
    
    if (Math.abs (sumForControl_a + sumForControl_b + sumForControl_c + sumForControl_d) >= MAX_DIFF)
    {
      errorReport += "\tAdvarsel: Differanse mellom memoriakonti og motkonto for memoriakonti: " + 
          (sumForControl_a + sumForControl_b + sumForControl_c + sumForControl_d) + "'" + lf + lf;
    }
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
      boolean rettArt = (art >= 0 && art <= 99);
      
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
      boolean rettArt = (art >= 0 && art <= 99);
      
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
      boolean rettArt = (art >= 0 && art <= 99);
      
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
      boolean rettArt = (art >= 0 && art <= 99);
      
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