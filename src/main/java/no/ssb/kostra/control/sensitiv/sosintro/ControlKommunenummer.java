package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: ControlKommunenummer.java,v $
 * Revision 1.5  2008/12/12 10:45:11  pll
 * Tatt vekk kontroll av bydelsnr.
 *
 * Revision 1.4  2007/11/07 10:49:45  pll
 * Endret implementasjon av getErrorType().
 *
 * Revision 1.3  2007/10/25 11:37:12  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/10/05 04:39:14  pll
 * Benytter no.ssb.kostra.utils.Regioner isteden for
 * no.ssb.kostra.utils.Regioner2006.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:51  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:31  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.3  2006/01/05 08:31:54  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.2  2006/01/05 08:16:37  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.Regioner;

public final class ControlKommunenummer extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K3: Kommunenummer";
  private Vector<Integer> invalidRegions = new Vector<Integer>();
  private Vector<Integer> divergingRegions = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    String kommunenr = RecordFields.getKommunenummer(line);
    String recordRegion = kommunenr + "00";

    if (! Regioner.kommuneNrIsValid (recordRegion))
    {
      lineHasError = true;
      invalidRegions.add (new Integer (lineNumber));
    }
    else if (! recordRegion.equalsIgnoreCase(region))
    {
      lineHasError = true;
      divergingRegions.add(new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = invalidRegions.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: ukjent kommunenummer i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + invalidRegions.elementAt(i);
        }
        errorReport += ").";
      }
    }
    numOfRecords = divergingRegions.size();
    if (numOfRecords > 0)
    {
//      errorReport += lf + "\tFeil: kommunenummer i " + numOfRecords + " record" + (numOfRecords == 1 ? "" : "s") + " avviker fra oppgitt kommunenummer (" + region + ").";
      errorReport += lf + "\tKommunenummeret som er oppgitt i kontrollprogrammet stemmer " +
      "ikke med kommunenummeret som er oppgitt i recorden (filuttrekket). Kontroller at " +
      "riktig kommunenummer ble oppgitt til kontrollprogrammet. Hvis dette stemmer, må " +
      "kommunenummer i recorden (filuttrekket) rettes.";
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + divergingRegions.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return (invalidRegions.size() > 0 ||
            divergingRegions.size() > 0);
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}
