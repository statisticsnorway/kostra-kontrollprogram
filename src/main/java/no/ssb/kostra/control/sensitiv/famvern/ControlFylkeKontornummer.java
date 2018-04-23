package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: ControlFylkeKontornummer.java,v $
 * Revision 1.6  2009/01/05 17:39:09  pll
 * Flyttet impl. av metoden hasCorrectKontorNr
 * til no.ssb.utils.FamilievernKontorNrChecker.
 *
 * Revision 1.5  2008/12/17 13:11:27  pll
 * Endret som følge av endringer i kravspec.
 *
 * Revision 1.4  2008/12/12 15:24:55  pll
 * no message
 *
 * Revision 1.3  2008/12/11 11:33:39  pll
 * RecordFields.getFylkesnummer har endret navn til
 * RecordFields.getRegionNr
 *
 * Revision 1.2  2007/10/25 11:37:03  pll
 * Implementerer getErrorType.
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
import no.ssb.kostra.utils.FamilievernKontorNrChecker;

public final class ControlFylkeKontornummer extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K5: Manglende samsvar mellom regions- og kontornummer";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String fylkesNumber = RecordFields.getRegionNr(line);
    String kontorNumber = RecordFields.getKontornummer(line);
  
    boolean lineHasError = ! FamilievernKontorNrChecker.hasCorrectKontorNr (fylkesNumber, kontorNumber); 

    if (lineHasError)
    {
      lineNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + 
      " stemmer ikke regions- og kontornummer overens."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + lineNumbers.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }
  
  public boolean foundError()
  {
    return lineNumbers.size() > 0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}
