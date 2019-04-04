package no.ssb.kostra.control.regnskap.regn0A;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlKontoklasseOgArtInvestering 
    extends no.ssb.kostra.control.Control
{
  private Vector<String[]> invalidCombinations = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    
    String kontoklasse = RecordFields.getKontoklasse (line);
    
    if (kontoklasse.equalsIgnoreCase("1"))
    { 
      String art = RecordFields.getArt (line);

      if (art.equalsIgnoreCase ("529") ||
          art.equalsIgnoreCase ("548") ||
          art.equalsIgnoreCase ("670") ||
          art.equalsIgnoreCase ("910") ||
          art.equalsIgnoreCase ("911") ||
          art.equalsIgnoreCase ("929") ||
          art.equalsIgnoreCase ("948") ||
          art.equalsIgnoreCase ("958") ||
          art.equalsIgnoreCase ("970"))
      { 
        lineHasError = true;
        String[] container = {kontoklasse, art, Integer.toString (lineNumber)};  
        invalidCombinations.add (container);
      }
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 9, kombinasjon kontoklasse og art i investeringsregnskapet:" + lf + lf;
    if (foundError())
    {
      int numOfRecords = invalidCombinations.size();
      errorReport += "\tFeil: Arten er kun tillatt brukt i investeringsregnskapet." + lf; 
      for (int i=0; i<numOfRecords; i++)
      {
        String[] container = (String[]) invalidCombinations.elementAt(i);
        errorReport += "\t\tkontoklasse: " + container[0] + 
            " art: " + container[1] + " (Record nr. " + container[2] + ")" + lf;
      }
    }
    errorReport += "\tKorreksjon: Rett opp til art som er gyldig i driftsregnskapet, eller overfør posteringen til investeringsregnskapet" + lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return invalidCombinations.size() > 0;
  }  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}