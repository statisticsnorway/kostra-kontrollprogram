package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: ControlAargang.java,v $
 * Revision 1.5  2007/12/07 12:51:26  pll
 * Fanger opp Exception isteden for NumberFormatException.
 *
 * Revision 1.4  2007/11/19 07:54:47  pll
 * Endret implementasjon av getErrorType().
 *
 * Revision 1.3  2007/10/25 11:33:57  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/10/23 11:44:51  pll
 * Fjernet hardkodede årstall.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.2  2006/11/27 10:16:55  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:06  pll
 * Import av 2005-filer
 *
 * Revision 1.2  2005/12/14 13:04:21  lwe
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
    String errorReport = ERROR_TEXT + lf + lf;
    int numOfRecords = previousYear.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records tilhører fjorårets resultatregnskap." + lf; 
      if (numOfRecords <= 10)
      {
        errorReport += "\t\t(Gjelder record nr.:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + previousYear.elementAt(i);
        }
        errorReport += ")." + lf;
      }
    }
    numOfRecords = nextYear.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records tilhører neste års resultatbudsjett." + lf; 
      if (numOfRecords <= 10)
      {
        errorReport += "\t\t(Gjelder record nr.:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + nextYear.elementAt(i);
        }
        errorReport += ")." + lf;
      }
    }
    numOfRecords = otherYears.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records tilhører andre års resultatregnskap/-budsjett." + lf; 
      if (numOfRecords <= 10)
      {
        errorReport += "\t\t(Gjelder record nr.:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + otherYears.elementAt(i);
        }
        errorReport += ")." + lf;
      }
    }
    errorReport += lf + lf;
    return errorReport;
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