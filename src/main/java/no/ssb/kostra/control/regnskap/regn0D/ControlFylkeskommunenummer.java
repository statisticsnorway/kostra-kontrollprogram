package no.ssb.kostra.control.regnskap.regn0D;

import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.Regioner;

final class ControlFylkeskommunenummer 
    extends no.ssb.kostra.control.Control
{
  private Vector<Integer> invalidRegions = new Vector<Integer>();
  private Vector<Integer> divergingRegions = new Vector<Integer>();
  private String region = "";

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    this.region = region;
  
    String fnr = RecordFields.getRegion(line);

    boolean lineHasError = false;
    
    if (! Regioner.fylkeskommuneNrIsValid (fnr))
    {
      lineHasError = true;
      invalidRegions.add (new Integer (lineNumber));
    }
    else if (! fnr.equalsIgnoreCase(region))
    {
      lineHasError = true;
      divergingRegions.add(new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 3, fylkeskommunenummer:" + lf;
    int numOfRecords = invalidRegions.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: ukjent fylkeskommunenummer i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + invalidRegions.elementAt(i) + (i>0 ? "" : ", ");
        }
        errorReport += ").";
      }
    }
    numOfRecords = divergingRegions.size();
    if (numOfRecords > 0)
    {
      errorReport += lf + "\tFeil: fylkeskommunenummer i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + " avviker fra oppgitt fylkeskommunenummer (" + region + ")."; 
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
    errorReport += lf + "\tKorreksjon: Rett fylkeskommunenummeret." + lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return (invalidRegions.size() > 0 ||
            divergingRegions.size() > 0);
  }  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}
