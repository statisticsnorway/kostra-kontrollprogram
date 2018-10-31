package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 *
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.Regioner;

public final class ControlKommunenummer extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K3: Kommunenummer (bydelssjekk for Oslo)";
  private Vector<Integer> invalidRegions = new Vector<Integer>();
  private Vector<Integer> divergingRegions = new Vector<Integer>();
  private String region = "";

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    this.region = region;
  
    String kommunenr = RecordFields.getKommunenummer(line);
    String bydelnr = (RecordFields.getKommunenummer(line).equals("0301")) ?  RecordFields.getBydelsnummer(line) :  "00";

    String recordRegion = kommunenr + bydelnr;

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
      //
      errorReport += lf + "\tFeil: kommunenummer som er oppgitt i" + numOfRecords +
          " record" + (numOfRecords == 1 ? "" : "s") + " stemmer ikke med kommunenummeret som er oppgitt i recorden (filuttrekket). (" + region + ")." + lf +
          "\tKontroller at riktig kommunenummer ble oppgitt til kontrollprogrammet. Hvis dette stemmer, må kommunenummer i recorden (filuttrekket) rettes." +  lf +
          "\tFor Oslo kommune må riktig bydelsnummer oppgis.";
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