package no.ssb.kostra.control.regnskap.regn0I;

import no.ssb.kostra.control.Constants;

final class ControlAvskrivinger extends no.ssb.kostra.control.Control
{
  private int sumForControl_16_1_a = 0;
  private int sumForControl_16_1_b = 0;
  private int sumForControl_16_3 = 0;
  private int sumForControl_16_4 = 0;

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
    if (doControl_16_3 (line))
    {
      lineControlled = true;
      sumForControl_16_3 += belop;
    }
    if (doControl_16_4 (line))
    {
      lineControlled = true;
      sumForControl_16_4 += belop;
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 16, avskrivninger:" + lf + lf;
    if (Math.abs (sumForControl_16_1_a + sumForControl_16_1_b) >= MAX_VALUE)
    {
      errorReport += "\tFeil: Differanse i avskrivninger (sum art 590 og 990) : " +
          (sumForControl_16_1_a + sumForControl_16_1_b) + "'. " +
          "(Art 590: " + sumForControl_16_1_a + "', art 990: " + sumForControl_16_1_b + "')" + lf +
          "\tKorreksjon: Rett opp i fila slik at art 590 stemmer overens med art 990 (margin på +/- "+MAX_VALUE+"')." + lf + lf;
    }
    if (sumForControl_16_1_b == 0 && field_16_1_b_isFound)
    {
      errorReport += "\tFeil: Motpost avskrivninger mangler (funksjon 3.860, art 990)." + lf +
              "\tKorreksjon: Rett opp i fila slik at motpost avskrivninger er ført på funksjon 860, art 990." + lf + lf;
    }
    if (sumForControl_16_3 != 0)
    {
      errorReport += "\tFeil: Avskrivninger skal føres på tjenestefunksjon." + lf +
              "\tKorreksjon: Rett opp i fila slik at avskrivningskostnadene er ført på tjenestefunksjon." + lf + lf;
    }
    if (sumForControl_16_4 != 0)
    {
      errorReport += "\tFeil: Motpost avskrivninger skal føres på funksjon 860." + lf +
              "\tKorreksjon: Rett opp i fila slik at motpost avskrivninger er ført på funksjon 860, art 990." + lf + lf;
    }
    return errorReport + lf;
  }

  public boolean foundError()
  {
    return 
        (Math.abs (sumForControl_16_1_a + sumForControl_16_1_b) >= MAX_VALUE ||
         (sumForControl_16_1_b == 0 && field_16_1_b_isFound) ||
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
    String kontoklasse = RecordFields.getKontoklasse (line);
    String funksjon = RecordFields.getFunksjon (line);
    String art = RecordFields.getArt (line);

    boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
    boolean rettFunksjon = (funksjon.equalsIgnoreCase("860"));
    boolean rettArt = art.equalsIgnoreCase("590");
      
    return rettKontoklasse && rettFunksjon && rettArt;
  }

  private boolean doControl_16_4 (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = kontoklasse.equalsIgnoreCase("3");
      boolean rettFunksjon = (funksjon >= 100 && funksjon <= 399);
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
