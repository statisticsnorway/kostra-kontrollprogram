package no.ssb.kostra.control.regnskap.regn0K;

import no.ssb.kostra.control.Constants;

final class ControlInterneOverforinger 
    extends no.ssb.kostra.control.Control
{
  private int sumForControl_14_a = 0;
  private int sumForControl_14_b = 0;

  private final int MAX_VALUE = 50;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineControlled = false;
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue (line);
      
    } catch (Exception e) {
      return lineControlled;
    }
  
    if (doControl_14_a (line))
    {
      lineControlled = true;
      sumForControl_14_a += belop;
    }
    if (doControl_14_b (line))
    {
      lineControlled = true;
      sumForControl_14_b += belop;
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 14, interne overføringer:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse mellom internkjøp og -salg (art 290 og 790): " +
          (sumForControl_14_a + sumForControl_14_b) + "'. " +
          "(Internkjøp: " + sumForControl_14_a + "', -salg: " + sumForControl_14_b + "')" + lf +
          "\tKorreksjon: Rett opp i fila slik at internkjøp og internsalg stemmer overens (margin på +/- "+MAX_VALUE+"')." + lf + lf;  //RAZ 01.07.2013
    }    
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_14_a + sumForControl_14_b) >= MAX_VALUE;
  }  

  private boolean doControl_14_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = (kontoklasse.equalsIgnoreCase("3") || 
          kontoklasse.equalsIgnoreCase("4"));
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = art.equalsIgnoreCase("290");
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_14_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = (kontoklasse.equalsIgnoreCase("3") || 
          kontoklasse.equalsIgnoreCase("4"));
      boolean rettFunksjon = (funksjon >= 400 && funksjon <= 899);
      boolean rettArt = art.equalsIgnoreCase("790");
      
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
