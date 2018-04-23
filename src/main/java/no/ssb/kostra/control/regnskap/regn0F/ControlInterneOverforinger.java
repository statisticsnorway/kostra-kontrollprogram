package no.ssb.kostra.control.regnskap.regn0F;

import no.ssb.kostra.control.Constants;

final class ControlInterneOverforinger 
    extends no.ssb.kostra.control.Control
{
  private int sumForControl_14_a = 0;
  private int sumForControl_14_b = 0;
  private int sumForControl_14_2_a = 0;
  private int sumForControl_14_2_b = 0;
  private int sumForControl_14_3_a = 0;
  private int sumForControl_14_3_b = 0;

  private final int MAX_VALUE = 30; //tidligere satt lik 100

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
    if (doControl_14_2_a (line))
    {
      lineControlled = true;
      sumForControl_14_2_a += belop;
    }
    if (doControl_14_2_b (line))
    {
      lineControlled = true;
      sumForControl_14_2_b += belop;
    }
    if (doControl_14_3_a (line))
    {
      lineControlled = true;
      sumForControl_14_3_a += belop;
    }
    if (doControl_14_3_b (line))
    {
      lineControlled = true;
      sumForControl_14_3_b += belop;
    }
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 12, interne overføringer:" + lf + lf;
    if (Math.abs (sumForControl_14_a + sumForControl_14_b) >= MAX_VALUE)
    {
      errorReport += "\tFeil: Differanse mellom internkjøp og -salg (art 380 og 780): " +
          (sumForControl_14_a + sumForControl_14_b) + "'. " +
          "(Internkjøp: " + sumForControl_14_a + "', -salg: " + sumForControl_14_b + "')" + lf;
    }
    if (Math.abs (sumForControl_14_2_a + sumForControl_14_2_b) >= MAX_VALUE)
    {
      errorReport += "\tFeil: Differanse mellom kalkulatoriske utgifter og inntekter ved " +
    	  "kommunal tjenesteytelse (art 390 og 790): " +
          (sumForControl_14_2_a + sumForControl_14_2_b) + "'. " +
          "(Internkjøp: " + sumForControl_14_2_a + "', -salg: " + sumForControl_14_2_b + "')" + lf;
    }
    if (Math.abs (sumForControl_14_3_a + sumForControl_14_3_b) >= MAX_VALUE)
    {
      errorReport += "\tFeil: Differanse mellom overføringer av midler og innsamlede midler (art 465 og 865): " +
          (sumForControl_14_3_a + sumForControl_14_3_b) + "'. " +
          "(Internkjøp: " + sumForControl_14_3_a + "', -salg: " + sumForControl_14_3_b + "')" + lf;
    }
    errorReport += lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return (Math.abs (sumForControl_14_a + sumForControl_14_b) >= MAX_VALUE ||
            Math.abs (sumForControl_14_2_a + sumForControl_14_2_b) >= MAX_VALUE ||
            Math.abs (sumForControl_14_3_a + sumForControl_14_3_b) >= MAX_VALUE);
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
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = art.equalsIgnoreCase("380");
      
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
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = art.equalsIgnoreCase("780");
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_14_2_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = (kontoklasse.equalsIgnoreCase("3") || 
          kontoklasse.equalsIgnoreCase("4"));
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = art.equalsIgnoreCase("390");
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_14_2_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = (kontoklasse.equalsIgnoreCase("3") || 
          kontoklasse.equalsIgnoreCase("4"));
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = art.equalsIgnoreCase("790");
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_14_3_a (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = (kontoklasse.equalsIgnoreCase("3") || 
          kontoklasse.equalsIgnoreCase("4"));
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = art.equalsIgnoreCase("465");
      
      return rettKontoklasse && rettFunksjon && rettArt;
    }
    catch (Exception e)
    {
      //Maa logges!
      return false;
    }    
  }

  private boolean doControl_14_3_b (String line)
  {
    try
    {
      String kontoklasse = RecordFields.getKontoklasse (line);
      int funksjon = RecordFields.getFunksjonIntValue (line);
      String art = RecordFields.getArt (line);

      boolean rettKontoklasse = (kontoklasse.equalsIgnoreCase("3") || 
          kontoklasse.equalsIgnoreCase("4"));
      boolean rettFunksjon = ((funksjon >= 41 && funksjon <= 45) || funksjon == 89 );
      boolean rettArt = art.equalsIgnoreCase("865");
      
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
