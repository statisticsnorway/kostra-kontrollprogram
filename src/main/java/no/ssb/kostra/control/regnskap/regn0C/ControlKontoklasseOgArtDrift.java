package no.ssb.kostra.control.regnskap.regn0C;

/*
 * $Log: ControlKontoklasseOgArtDrift.java,v $
 * Revision 1.6  2010/02/26 08:01:55  pll
 * Endret kravspec.
 *
 * Revision 1.5  2008/10/14 13:08:45  pll
 * Lagt til korreksjonstekst.
 *
 * Revision 1.4  2007/10/25 12:07:14  pll
 * Endret implementasjon av getErrorType.
 *
 * Revision 1.3  2007/10/25 11:35:52  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/10/04 06:39:35  pll
 * Modifisert ihht. endringer i kravspec 04.10.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:06  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.4  2006/10/19 12:09:22  lwe
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
    errorReport += lf + "\tKorreksjon: Rett opp feil kontoklasse eller art." + lf + lf;
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