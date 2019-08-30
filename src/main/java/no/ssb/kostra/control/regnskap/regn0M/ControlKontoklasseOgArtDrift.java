package no.ssb.kostra.control.regnskap.regn0M;

/*
 *
 */

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlKontoklasseOgArtDrift 
    extends no.ssb.kostra.control.Control
{
  private Vector<String[]> invalidCombinations = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    String kontoklasse = RecordFields.getKontoklasse (line);
    
    if (kontoklasse.equalsIgnoreCase("4"))
    { 
      String art = RecordFields.getArt (line);

      if (art.equalsIgnoreCase ("240") ||
          art.equalsIgnoreCase ("509") ||
          art.equalsIgnoreCase ("540") ||
          art.equalsIgnoreCase ("570") ||
          art.equalsIgnoreCase ("590") ||
          art.equalsIgnoreCase ("800") ||
          art.equalsIgnoreCase ("870") ||
          art.equalsIgnoreCase ("874") ||
          art.equalsIgnoreCase ("875") ||
          art.equalsIgnoreCase ("877") ||
          art.equalsIgnoreCase ("909") ||
          art.equalsIgnoreCase ("990"))
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
    String errorReport = "Kontroll 10, kombinasjon kontoklasse og art i driftsregnskapet:" + lf + lf;
    if (foundError())
    {
      int numOfRecords = invalidCombinations.size();
      errorReport += "\tFeil: Arten er kun tillatt brukt i driftsregnskapet." + lf; 
      for (int i=0; i<numOfRecords; i++)
      {
        String[] container = (String[]) invalidCombinations.elementAt(i);
        errorReport += "\t\tkontoklasse: " + container[0] + 
            " art: " + container[1] + " (Record nr. " + container[2] + ")" + lf;
      }
    }
    errorReport += "\tKorreksjon: Rett opp til art som er gyldig i investeringsregnskapet, eller overfør posteringen til driftsregnskapet." + lf + lf;
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