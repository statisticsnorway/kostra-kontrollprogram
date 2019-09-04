package no.ssb.kostra.control.regnskap.regn0M;

import no.ssb.kostra.control.Constants;

final class ControlOverforingDriftInvestering 
    extends no.ssb.kostra.control.Control
{
  private int sumForControl_15_a = 0;
  private int sumForControl_15_b = 0;

  private final int MAX_VALUE = 30;

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
  
    if (doControl_15_a (line))
    {
      lineControlled = true;
      sumForControl_15_a += RecordFields.getBelopIntValue (line);
    }
    if (doControl_15_b (line))
    {
      lineControlled = true;
      sumForControl_15_b += RecordFields.getBelopIntValue (line);
    }
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
    return lineControlled && 
        (Math.abs (sumForControl_15_a + sumForControl_15_b) >= MAX_VALUE);
  }  

  private boolean doControl_15_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
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

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("4");
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