package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: ControlFylkesnummer.java,v $
 * Revision 1.7  2009/04/02 08:44:06  pll
 * Nye regionnr.
 *
 * Revision 1.6  2008/12/12 15:24:27  pll
 * Tekstendring.
 *
 * Revision 1.5  2008/12/11 14:01:17  pll
 * RC1.
 *
 * Revision 1.4  2008/12/11 11:33:38  pll
 * RecordFields.getFylkesnummer har endret navn til
 * RecordFields.getRegionNr
 *
 * Revision 1.3  2007/10/25 11:37:03  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/10/05 04:37:36  pll
 * Benytter no.ssb.kostra.utils.Regioner isteden for
 * no.ssb.kostra.utils.Regioner2006.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:49  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:28  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.6  2006/01/05 08:16:33  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlFylkesnummer extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K3: Regionsnummer";
  private Vector<Integer> invalidRegions = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    String recordRegion = RecordFields.getRegionNr(line);

    if (
   	    !recordRegion.equalsIgnoreCase("667200") && 
   	    !recordRegion.equalsIgnoreCase("667300") && 
   	    !recordRegion.equalsIgnoreCase("667400") && 
   	    !recordRegion.equalsIgnoreCase("667500") && 
   	    !recordRegion.equalsIgnoreCase("667600") 
    )
    {
      lineHasError = true;
      invalidRegions.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = invalidRegions.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: ukjent regionsnummer i " + numOfRecords + 
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
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return (invalidRegions.size() > 0 );
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}
