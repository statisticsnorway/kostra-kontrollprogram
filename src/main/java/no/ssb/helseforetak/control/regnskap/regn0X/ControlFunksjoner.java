package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: ControlFunksjoner.java,v $
 * Revision 1.8  2009/10/15 09:59:09  pll
 * Tekstendring.
 *
 * Revision 1.7  2009/10/03 12:18:34  pll
 * Versjon: 2009-rapporteringen
 *
 * Revision 1.6  2009/04/06 13:02:50  pll
 * Bugfix.
 *
 * Revision 1.5  2008/04/30 09:07:03  pll
 * String-objekter erstattet med StringBuilder-objekter.
 *
 * Revision 1.4  2007/11/19 07:54:47  pll
 * Endret implementasjon av getErrorType().
 *
 * Revision 1.3  2007/11/05 11:16:22  pll
 * Endringer før 2007-rapporteringen.
 *
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.3  2006/12/13 11:16:21  pll
 * Tatt ut funksjon 880.
 * Gir ekstra feilmelding hvis ikke blank i 4. posisjon i felt 8 (funksjon).
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:06  pll
 * Import av 2005-filer
 *
 * Revision 1.4  2005/12/22 12:59:38  lwe
 * etter kravspec: endret/sjekket gyldige funksjoner (ut: 440, 645, 655, 860, 880, inn: 410, 470, 641, 642, 643, 651, 652, 653, 681)
 *
 * Revision 1.3  2005/12/22 12:39:47  lwe
 * etter kravspec: endret/sjekket gyldige funksjoner
 *
 * Revision 1.2  2005/12/14 13:04:21  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlFunksjoner extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 7, Funksjoner, gyldige:";
  
  private String[] validFunksjoner = 
  {
    "400", "460", "600", "606", "620", "630", "636", "637", "641", "642", "651", "681", "840"
  };

  private Vector<String[]> invalidFunksjoner = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    String funksjon = RecordFields.getFunksjon (line);
    
    if (! validFunksjon (funksjon))
    {
      lineHasError = true;
      String[] container = new String[2];
      container[0] = Integer.toString (lineNumber);
      container[1] = funksjon;
      invalidFunksjoner.add (container);
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    StringBuilder errorReport = new StringBuilder(ERROR_TEXT + lf + lf);
    int numOfRecords = invalidFunksjoner.size();
    if (numOfRecords > 0)
    {
      errorReport.append("\tFeil: Ukjent" + (numOfRecords ==1 ? "" : "e") + 
          " funksjonskode" + (numOfRecords ==1 ? "" : "r"));
      if (numOfRecords <= 10)
      {
        errorReport.append(":"+lf);
          for (int i=0; i<numOfRecords; i++)
          {
            String[] container = (String[]) invalidFunksjoner.elementAt(i);
            errorReport.append("\t\tfunksjon " + container[1] + 
                " (Record nr. " + container[0] + ")" + (container[1].length()==4?" NB! Funksjonskodene skal rapporteres med en blank posisjon til slutt.":"") + lf);
          }
      } else {
        errorReport.append("."+lf+"\t\tGjelder "+numOfRecords+" records.");
      }
      
    }
    errorReport.append(lf + lf);
    return errorReport.toString();
  }

  public boolean foundError()
  {
    return invalidFunksjoner.size() > 0;
  }

  private boolean validFunksjon (String funksjon)
  {
    for (int i=validFunksjoner.length-1; i>=0; i--)
    {
      if (funksjon.equalsIgnoreCase(validFunksjoner[i]))
      {
        return true;
      }
    }
    return false;
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}