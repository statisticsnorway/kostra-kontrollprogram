package no.ssb.kostra.control.regnskap.regn0K;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

/*
 * $Log: ControlKontoklasseOgArtDrift.java,v $
 * Revision 1.8  2010/10/21 07:55:11  pll
 * Ny art: 540
 *
 * Revision 1.7  2010/02/26 08:10:51  pll
 * Endret kravspec.
 *
 * Revision 1.6  2009/04/28 14:53:11  pll
 * Art 220 ut av listen.
 *
 * Revision 1.5  2008/10/16 22:25:58  pll
 * Endring iflg. endret kravspec.
 *
 * Revision 1.4  2008/10/16 12:18:18  pll
 * Lagt til korreksjonstekst.
 *
 * Revision 1.3  2007/10/26 08:27:08  pll
 * Endret implementasjon av getErrorType.
 *
 * Revision 1.2  2007/10/25 11:36:29  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.4  2006/10/19 12:09:22  lwe
 * art 220 skal allikevel ikke vÃ¦re med i listen (krav fra dep, ny kravspec 19. okt)
 *
 * Revision 1.3  2006/09/26 13:30:05  lwe
 * Lagt til art 220
 *
 */

final class ControlKontoklasseOgArtDrift 
    extends no.ssb.kostra.control.Control
{
  private Vector<String[]> invalidCombinations = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String kontoklasse = RecordFields.getKontoklasse (line);


    boolean lineHasError = false;
    
    if (kontoklasse.equalsIgnoreCase("4"))
    { 
      String art = RecordFields.getArt (line);

      if (art.equalsIgnoreCase ("509") ||
          art.equalsIgnoreCase ("540") ||
          art.equalsIgnoreCase ("570") ||
          art.equalsIgnoreCase ("590") ||
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
    String errorReport = "Kontroll 10 b), kombinasjon kontoklasse og art i driftsregnskapet:" + lf + lf;
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
    errorReport += "\tKorreksjon: Rett opp feil kontoklasse eller art." + lf + lf;
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
