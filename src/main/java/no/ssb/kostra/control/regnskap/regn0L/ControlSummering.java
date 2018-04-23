package no.ssb.kostra.control.regnskap.regn0L;

import no.ssb.kostra.control.Constants;

final class ControlSummering extends no.ssb.kostra.control.Control
{
  @SuppressWarnings("unused")
private int lines = 0;
  
  private int sumForControl_8_a = 0;
  private int sumForControl_8_b = 0;

  private final int MAX_DIFF = 11;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineControlled = false;
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue (line);
      
    } catch (Exception e) {
      return lineControlled;
    }
  
    lines += 1;
    
    if (doControl_8_a (line))
    {
      lineControlled = true;
      sumForControl_8_a += belop;
    }
    if (doControl_8_b (line))
    {
      lineControlled = true;
      sumForControl_8_b += belop;
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 11, summeringskontroller:" + lf + lf;
    
    if (Math.abs (sumForControl_8_a + sumForControl_8_b) >= MAX_DIFF)
    {
      errorReport += "\tFeil: Differanse mellom aktiva og passiva: " + 
          (sumForControl_8_a + sumForControl_8_b) + "'";
      errorReport += " (Aktiva: " + sumForControl_8_a + "', ";
      errorReport += "passiva: " + sumForControl_8_b + "')." + lf;
    }
    errorReport += "\tKorreksjon: Rett opp differansen mellom aktiva og passiva i balanseregnskapet." + lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_8_a + sumForControl_8_b) >= MAX_DIFF;
  }

  private boolean doControl_8_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("5");
      boolean rettFunksjon = (funksjon >= 10 && funksjon <= 28);
      boolean rettArt = (art >= 0 && art <= 999);
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_8_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("5");
      boolean rettFunksjon = (funksjon >= 31 && funksjon <= 5990);
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
    return Constants.CRITICAL_ERROR;
  }

}
