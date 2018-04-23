package no.ssb.kostra.control.regnskap.regn0B;

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
  
    lines += 1;

    try {
    
      if (doControl_8_a (line))
      {
        sumForControl_8_a += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }
      if (doControl_8_b (line))
      {
        sumForControl_8_b += RecordFields.getBelopIntValue (line);
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
    String errorReport = "Kontroll 8, summeringskontroller:" + lf + lf;
    
    if (Math.abs (sumForControl_8_a + sumForControl_8_b) >= MAX_DIFF)
    {
      errorReport += "\tFeil: Differanse mellom aktiva og passiva: " + 
          (sumForControl_8_a + sumForControl_8_b) + "'";
      errorReport += " (Aktiva: " + sumForControl_8_a + "', ";
      errorReport += "passiva: " + sumForControl_8_b + "')." + lf;
      errorReport += "\tKorreksjon: Rett opp differansen mellom aktiva og passiva i balanseregnskapet." + lf + lf;
    }
 
    
    if (sumForControl_8_a <= 0)    {
      errorReport += "\tFeil: Det er ikke registrert beløp i kapitler i aktiva." + lf;
      errorReport += "\tKorreksjon: Rett opp slik at balanseregnskapet inneholder registrering av aktiva i balanse." + lf + lf;
    }
    

    if (sumForControl_8_b >= 0)    {
      errorReport += "\tFeil: Det er ikke registrert beløp i kapitler i passiva." + lf;
      errorReport += "\tKorreksjon: Rett opp slik at balanseregnskapet inneholder registrering av passiva i balanse." + lf + lf;
    }
    
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_8_a + sumForControl_8_b) >= MAX_DIFF || sumForControl_8_a <= 0 || sumForControl_8_b >= 0;
  }

  private boolean doControl_8_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      int art = RecordFields.getArtIntValue (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("2");
      boolean rettFunksjon = (funksjon >= 10 && funksjon <= 27);
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

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("2");
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