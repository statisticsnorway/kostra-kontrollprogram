package no.ssb.helseforetak.control.regnskap.regn0Y;

/**
 * $Log: ControlRegnskapstype.java,v $
 * Revision 1.3  2009/04/06 11:49:39  pll
 * String -> StringBuilder
 *
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:23  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.3  2006/12/13 11:17:12  pll
 * Case sensitiv sjekk av regnskapstype.
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:07  pll
 * Import av 2005-filer
 *
 * Revision 1.3  2005/12/22 13:12:42  lwe
 * etter kravspec: endret feilmeldingstekst
 *
 * Revision 1.2  2005/12/14 13:04:23  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlRegnskapstype extends no.ssb.kostra.control.Control
{
  private Vector<Integer> lineNumbers = new Vector<Integer>();
  private final String ERROR_TEXT = "Kontroll 1, Regnskapstype:";
  private final String EXPECTED_TYPE_REGNSKAP = "0Y";
  private final String NONEXPECTED_TYPE_REGNSKAP = "0X";

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String regnskapsType = RecordFields.getRegnskapstype(line);
    
    boolean lineHasError = ! regnskapsType.equals(EXPECTED_TYPE_REGNSKAP);

    if (lineHasError)
    {
      lineNumbers.add (new Integer (lineNumber));
    }
      
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    StringBuilder errorReport = new StringBuilder (ERROR_TEXT + lf + lf);
    
    int numOfRecords = lineNumbers.size();
    if (numOfRecords > 0)
    {
      errorReport.append("\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records inneholder ikke \"" + EXPECTED_TYPE_REGNSKAP + "\"" + lf +
          "\ti de to første posisjonene." + lf + 
          "\tBalansen skal rapporteres med "+EXPECTED_TYPE_REGNSKAP+" i de to første posisjonene i filen. "+lf + 
          "\tResultatregnskapet skal rapporteres med "+NONEXPECTED_TYPE_REGNSKAP+" i de samme posisjonene. " + lf);
      if (numOfRecords <= 10)
      {
        errorReport.append("\t\t(Gjelder record nr.:");
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport.append(" " + lineNumbers.elementAt(i));
        }
        errorReport.append(")." + lf);
      }
    }
    errorReport.append(lf + lf);
    return errorReport.toString();
  }

  public boolean foundError()
  {
    return lineNumbers.size() > 0;
  }  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}