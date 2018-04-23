package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: ControlRegion.java,v $
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.3  2006/12/13 11:01:09  pll
 * Sjekker pos. 3-6 i regionnummeret (felt 4). Skal vaere 0000
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:06  pll
 * Import av 2005-filer
 *
 * Revision 1.2  2005/12/14 13:04:22  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.Regioner;

final class ControlRegion extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 3, Region:";

  private Vector<Integer> invalidRegions = new Vector<Integer>();
  private Vector<Integer> divergingRegions = new Vector<Integer>();
  private String region = "";

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    this.region = region;
  
    String regionNr = RecordFields.getRegion(line);
    String helseregionSuffix = regionNr.substring(2);
    regionNr = regionNr.substring(0,2);
    
    if (! Regioner.regionNrIsValid(regionNr) || ! helseregionSuffix.equals("0000"))
    {
      lineHasError = true;
      invalidRegions.add (new Integer (lineNumber));
    }
    else if (! regionNr.equalsIgnoreCase(region.substring(0,2)))
    {
      lineHasError = true;
      divergingRegions.add(new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + lf + lf;
    int numOfRecords = invalidRegions.size();
    if (numOfRecords > 0)
    {      
      errorReport += "\tFeil: ukjent regionskode i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + invalidRegions.elementAt(i);
        }
        errorReport += ")." + lf;
      }
    }
    numOfRecords = divergingRegions.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: regionskode i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + " avviker fra oppgitt regionskode (" + region.substring(0,2) + ")."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + divergingRegions.elementAt(i);
        }
        errorReport += ")." +  lf;
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

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}