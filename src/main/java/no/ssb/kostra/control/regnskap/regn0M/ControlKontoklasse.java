package no.ssb.kostra.control.regnskap.regn0M;

import java.util.*;

import no.ssb.kostra.control.Constants;

public final class ControlKontoklasse extends no.ssb.kostra.control.Control
{
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public  boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    String kontoklasse = RecordFields.getKontoklasse (line);
    if (! (kontoklasse.equalsIgnoreCase("0") || kontoklasse.equalsIgnoreCase("1")))
    {
      lineHasError = true;
      lineNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 5, kontoklasse:" + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: ukjent kontoklasse i " + numOfRecords + " record" + (numOfRecords == 1 ? "" : "s") + ".";
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t(Gjelder record nr.";
        String[] list = new String[numOfRecords];

        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + lineNumbers.elementAt(i) + (i>0 ? "" : ", ");
        }
        errorReport += ").";
      }
    }
    errorReport += lf + "\tKorreksjon: Rett kontoklasse. Kontoklasse 0 og 1 er gyldige for bevilgningsregnskapet.";
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return lineNumbers.size() > 0;
  }  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}