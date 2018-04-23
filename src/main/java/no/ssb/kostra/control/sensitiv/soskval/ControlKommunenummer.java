package no.ssb.kostra.control.sensitiv.soskval;

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

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String kommunenr = RecordFields.getKommunenummer(line);
//    String recordRegion = kommunenr + "00";
    String bydelnr = (RecordFields.getKommunenummer(line) == "0301") ?  RecordFields.getBydelsnummer(line) :  "00";
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
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + " stemmer ikke " +
              "kommunenummeret som er oppgitt i kontrollprogrammet stemmer" + lf +
              "\tikke med kommunenummeret som er oppgitt recorden (filuttrekket)." + lf +
              "\tKontroller at riktig kommunenummer ble oppgitt til " +
              "kontrollprogrammet." + lf + "\tHvis dette stemmer, må kommunenummer " +
              "i recorden (filuttrekket) rettes. For Oslo kommune må riktig bydelsnummer oppgis.";
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