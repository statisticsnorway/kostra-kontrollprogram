package no.ssb.kostra.control.regnskap.regn0F;

import no.ssb.kostra.control.Constants;

final class ControlAvskrivinger extends no.ssb.kostra.control.Control
{
  private int sumForControl_16_1_a = 0;
  private int sumForControl_16_1_b = 0;

  @SuppressWarnings("unused")
private boolean field_16_1_b_isFound = false;

  private final int MAX_VALUE = 30;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineControlled = false;
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue (line);
      
    } catch (Exception e) {
      return lineControlled;
    }
    
  
    if (doControl_16_1_a (line))
    {
      lineControlled = true;
      sumForControl_16_1_a += belop;
    }
    if (doControl_16_1_b (line))
    {
      field_16_1_b_isFound = true;
      lineControlled = true;
      sumForControl_16_1_b += belop;
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 14, avskrivninger:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse i avskrivninger (sum art 590 og 990) : " +
          (sumForControl_16_1_a + sumForControl_16_1_b) + "'. " +
          "(Art 590: " + sumForControl_16_1_a + "', art 990: " + sumForControl_16_1_b + "')" + lf;
    }
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_16_1_a + sumForControl_16_1_b) >= MAX_VALUE;
  }  

  private boolean doControl_16_1_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 41 && funksjon <= 44);
      boolean rettArt = art.equalsIgnoreCase("590");
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_16_1_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 41 && funksjon <= 44);
      boolean rettArt = art.equalsIgnoreCase("990");
      
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
