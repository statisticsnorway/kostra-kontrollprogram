package no.ssb.helseforetak.control.regnskap.regn0Y;

/**
 * $Log: ControlAargang.java,v $
 * Revision 1.6  2009/04/06 11:49:40  pll
 * String -> StringBuilder
 *
 * Revision 1.5  2007/12/07 12:52:20  pll
 * Fanger opp Exception isteden for NumberFormatException.
 *
 * Revision 1.4  2007/11/19 08:05:28  pll
 * Endret implementasjon av getErrorType().
 *
 * Revision 1.3  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/10/23 11:44:52  pll
 * Fjernet hardkodede årstall.
 *
 * Revision 1.1  2007/10/23 10:49:23  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:08  pll
 * Import av 2005-filer
 *
 * Revision 1.2  2005/12/14 13:04:22  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlAargang extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 2, Årgang:";

  private Vector<Integer> previousYear = new Vector<Integer>();
  private Vector<Integer> nextYear = new Vector<Integer>();
  private Vector<Integer> otherYears = new Vector<Integer>();

  private final int YEAR = Integer.parseInt(Constants.kostraYear);

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    try
    {
      int year = RecordFields.getAargangIntValue (line);

      if (year != YEAR)
      {
        lineHasError = true;
        if (year == (YEAR - 1))
        {
          previousYear.add (new Integer (lineNumber));
        }
        else if (year == (YEAR + 1))
        {
          nextYear.add (new Integer (lineNumber));
        }
        else
        {
          otherYears.add (new Integer (lineNumber));
        }
      }
    }
    catch (Exception e)
    {
      lineHasError = true;
      otherYears.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    StringBuilder errorReport = new StringBuilder (ERROR_TEXT + lf + lf);
    int numOfRecords = previousYear.size();
    if (numOfRecords > 0)
    {
      errorReport.append("\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records tilhører fjorårets balanse." + lf); 
      if (numOfRecords <= 10)
      {
        errorReport.append("\t\t(Gjelder record nr.:");
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport.append(" " + previousYear.elementAt(i));
        }
        errorReport.append(")." + lf);
      }
    }
    numOfRecords = nextYear.size();
    if (numOfRecords > 0)
    {
      errorReport.append("\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records tilhører neste års balanse." + lf); 
      if (numOfRecords <= 10)
      {
        errorReport.append("\t\t(Gjelder record nr.:");
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport.append(" " + nextYear.elementAt(i));
        }
        errorReport.append(")." + lf);
      }
    }
    numOfRecords = otherYears.size();
    if (numOfRecords > 0)
    {
      errorReport.append("\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records tilhører andre års balanser." + lf); 
      if (numOfRecords <= 10)
      {
        errorReport.append("\t\t(Gjelder record nr.:");
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport.append(" " + otherYears.elementAt(i));
        }
        errorReport.append(")." + lf);
      }
    }
    errorReport.append(lf + lf);
    return errorReport.toString();
  }

  public boolean foundError()
  {
    return (nextYear.size() > 0 ||
            previousYear.size() > 0 ||
            otherYears.size() > 0);
  }  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}