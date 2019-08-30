package no.ssb.kostra.control.regnskap.regn0M;

import no.ssb.kostra.control.Constants;

final class ControlAvskrivinger extends no.ssb.kostra.control.Control
{
  private int sumForControl_16_1_a = 0;
  private int sumForControl_16_1_b = 0;
  private int sumForControl_16_3 = 0;
  private int sumForControl_16_4 = 0;


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

    if (doControl_16_1_a (line))
    {
      sumForControl_16_1_a += RecordFields.getBelopIntValue (line);
      lineControlled = true;
    }
    if (doControl_16_1_b (line))
    {
      sumForControl_16_1_b += RecordFields.getBelopIntValue (line);
      lineControlled = true;
    }
    if (doControl_16_3 (line))
    {
      sumForControl_16_3 += RecordFields.getBelopIntValue (line);
      lineControlled = true;
    }
    if (doControl_16_4 (line))
    {
      sumForControl_16_4 += RecordFields.getBelopIntValue (line);
      lineControlled = true;
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 16, avskrivninger:" + lf;
    if (Math.abs (sumForControl_16_1_a + sumForControl_16_1_b) >= MAX_VALUE)
    {
      errorReport += lf + "\tFeil: Differanse i avskrivninger (sum art 590 og 990) : " +
          (sumForControl_16_1_a + sumForControl_16_1_b) + "'. " +
          "(Art 590: " + sumForControl_16_1_a + "', art 990: " + sumForControl_16_1_b + "')" + lf +
              "\tKorreksjon: Rett opp i fila slik at art 590 stemmer overens med art 990 (margin på +/- 30')." + lf;
    }
    if (sumForControl_16_1_b == 0)
    {
      errorReport += lf + "\tFeil: Motpost avskrivninger mangler (funksjon 1.860, art 990)." + lf +
              "\tKorreksjon: Rett opp i fila slik at motpost avskrivninger er ført på funksjon 860, art 990." + lf;
    }
    if (sumForControl_16_3 != 0)
    {
      errorReport += lf +"\tFeil: Avskrivninger skal føres på tjenestefunksjon." + lf +
              "\tKorreksjon: Rett opp i fila slik at avskrivningskostnadene er ført på tjenestefunksjon." + lf;
    }
    if (sumForControl_16_4 != 0)
    {
      errorReport += lf + "\tFeil: Motpost avskrivninger skal føres på funksjon 860." + lf +
              "\tKorreksjon: Rett opp i fila slik at motpost avskrivninger er ført på funksjon 860, art 990." + lf;
    }
    errorReport += lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return lineControlled && 
        (Math.abs (sumForControl_16_1_a + sumForControl_16_1_b) >= MAX_VALUE ||
         (sumForControl_16_1_b == 0)||
         sumForControl_16_3 != 0 ||
         sumForControl_16_4 != 0);
  }  

  private boolean doControl_16_1_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 899);
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
    String kontoklasse = RecordFields.getKontoklasse (line);
    String funksjon = RecordFields.getFunksjon (line);
    String art = RecordFields.getArt (line);

    boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
    boolean rettFunksjon = (funksjon.equalsIgnoreCase("860"));
    boolean rettArt = art.equalsIgnoreCase("990");
      
    return rettKontoklasse && rettFunksjon && rettArt;
  }

  private boolean doControl_16_3 (String line)
  {
    try
    {
    String kontoklasse = RecordFields.getKontoklasse (line);
    int funksjon = RecordFields.getFunksjonIntValue (line);
    String art = RecordFields.getArt (line);

    boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
    boolean rettFunksjon = (funksjon >= 800 && funksjon <= 899);
    boolean rettArt = art.equalsIgnoreCase("590");
      
    return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_16_4 (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 850) || (funksjon >= 870 && funksjon <= 899);
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