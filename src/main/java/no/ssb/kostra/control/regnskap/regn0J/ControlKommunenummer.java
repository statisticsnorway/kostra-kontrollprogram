package no.ssb.kostra.control.regnskap.regn0J;

import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.Regioner;

final class ControlKommunenummer extends no.ssb.kostra.control.Control
{
  private Vector<Integer> invalidRegions = new Vector<Integer>();
  private Vector<Integer> divergingRegions = new Vector<Integer>();
  private String region = "";

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    this.region = region;

    boolean lineHasError = false;
  
    String knr = RecordFields.getRegion(line);
    
    if (! Regioner.kommuneNrIsValid (knr))
    {
      lineHasError = true;
      invalidRegions.add (new Integer (lineNumber));
    }
    else if (! knr.equalsIgnoreCase(region))
    {
      lineHasError = true;
      divergingRegions.add(new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 3, kommunenummer:" + lf;
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
      errorReport += lf + "\tFeil: ukjent kommunenummer i " + numOfRecords +
          " record" + (numOfRecords == 1 ? "" : "s") + ".";
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
    errorReport += lf + "\tKorreksjon: Rett kommunenummeret." + lf + lf;
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
