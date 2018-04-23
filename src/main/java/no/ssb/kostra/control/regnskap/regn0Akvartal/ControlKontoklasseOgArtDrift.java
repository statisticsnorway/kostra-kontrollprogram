package no.ssb.kostra.control.regnskap.regn0Akvartal;

/*
 * $Log: ControlKontoklasseOgArtDrift.java,v $
 * Revision 1.3  2009/04/28 14:53:46  pll
 * Art 220 ut av listen.
 *
 * Revision 1.2  2008/03/13 14:04:46  pll
 * Feiltype endret  til NORMAL_ERROR.
 *
 * Revision 1.1  2008/03/11 11:56:04  pll
 * Import.
 *
 * Revision 1.3  2007/10/25 11:07:46  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/10/04 06:17:20  pll
 * Modifisert ihht. endringer i kravspec 04.10.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:06  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.4  2006/10/19 12:09:21  lwe
 * art 220 skal allikevel ikke være med i listen (krav fra dep, ny kravspec 19. okt)
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
    
    if (kontoklasse.equalsIgnoreCase("0"))
    { 
      String art = RecordFields.getArt (line);

      if (art.equalsIgnoreCase ("509") ||
    	  art.equalsIgnoreCase ("540") ||
          art.equalsIgnoreCase ("570") ||
          art.equalsIgnoreCase ("590") ||
//          art.equalsIgnoreCase ("729") ||
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
    errorReport += lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return invalidCombinations.size() > 0;
  }  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}