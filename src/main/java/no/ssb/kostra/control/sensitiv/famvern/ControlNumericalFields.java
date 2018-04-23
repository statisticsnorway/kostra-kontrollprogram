package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: ControlNumericalFields.java,v $
 * Revision 1.5  2009/06/18 10:18:28  pll
 * Endret til kritisk kontrolltype.
 *
 * Revision 1.4  2008/12/17 13:04:43  pll
 * Bugfix.
 *
 * Revision 1.3  2008/12/17 11:23:09  pll
 * Ny implementasjon.
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
 * Revision 1.6  2006/02/23 11:51:31  lwe
 * gjort klassen public
 *
 * Revision 1.5  2006/01/05 08:16:33  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.CompatJdk13;

public class ControlNumericalFields extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K2: Ulovlig tegn i tallfeltet.";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String concatenatedNumericalFields = 
        RecordFields.getAllNumericalFieldsConcatenated(line);
    
    boolean lineHasError = ! CompatJdk13.isNumericalWithSpace(concatenatedNumericalFields);
    
    if (lineHasError)
      lineNumbers.add(new Integer (lineNumber));
    
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    errorReport += "\tDet forekommer andre tegn enn tall eller " +
    "blankt" + lf + "\ti et eller flere felter hvor det kun skal være oppgitt tall." + lf;
    int numOfErrors = lineNumbers.size();
    if (numOfErrors <= 10)
    {
      errorReport += "\t\t(Gjelder record: ";
      for (int i=0; i<numOfErrors; i++)
      {
        errorReport += " " + lineNumbers.elementAt(i);
      }
      errorReport += ")" + lf;
    }
    errorReport += lf;
    return errorReport;
  }
  public boolean foundError()
  {
    return lineNumbers.size()>0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}
