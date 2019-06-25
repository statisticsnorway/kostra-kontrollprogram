package no.ssb.kostra.control.regnskap.regn0P;

import no.ssb.kostra.control.Constants;

final class ControlOverforingDriftInvestering 
    extends no.ssb.kostra.control.Control
{
  private int sumForControl_15_a = 0;
  private int sumForControl_15_b = 0;

  private final int MAX_VALUE = 30;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineControlled = false;
  
    try {
    
      if (doControl_15_a (line))
      {
        sumForControl_15_a += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }
      if (doControl_15_b (line))
      {
        sumForControl_15_b += RecordFields.getBelopIntValue (line);
        lineControlled = true;
      }
    
    } catch (Exception e) {}
    
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 15, overføring mellom drifts- og investeringsregnskap:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse i overføring mellom drifts- og investeringsregnskapet (art 570 og 970): " +
          (sumForControl_15_a + sumForControl_15_b) + "'. " +
          "(Art 570: " + sumForControl_15_a + "', art 970: " + sumForControl_15_b + "')" + lf + 
              "\tKorreksjon: Rett opp i fila slik at overføringer mellom drifts- og investeringsregnskapet stemmer overens." + lf + lf;
    }
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumForControl_15_a + sumForControl_15_b) >= MAX_VALUE;
  }  

  private boolean doControl_15_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("1");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = art.equalsIgnoreCase("570");
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_15_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("0");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
      boolean rettArt = art.equalsIgnoreCase("970");
      
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