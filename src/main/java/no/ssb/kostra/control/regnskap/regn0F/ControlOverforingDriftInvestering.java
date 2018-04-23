package no.ssb.kostra.control.regnskap.regn0F;

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
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue (line);
      
    } catch (Exception e) {
      return lineControlled;
    }
  
    if (doControl_15_a (line))
    {
      lineControlled = true;
      sumForControl_15_a += belop;
    }
    if (doControl_15_b (line))
    {
      lineControlled = true;
      sumForControl_15_b += belop;
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 13, overfø¸ring mellom drifts- og investeringsregnskap:" + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Differanse i overfø¸ring mellom drifts- og investeringsregnskapet (art 570 og 970): " +
          (sumForControl_15_a + sumForControl_15_b) + "'. " +
          "(Art 570: " + sumForControl_15_a + "', art 970: " + sumForControl_15_b + "')" + lf + lf;
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

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
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

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
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
