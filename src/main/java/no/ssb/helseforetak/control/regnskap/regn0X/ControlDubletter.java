package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: ControlDubletter.java,v $
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
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


import java.util.*;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DuplicateChecker;

final class ControlDubletter extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 9, Dublettkontroll:";
  private DuplicateChecker duplicateChecker = new DuplicateChecker();
  

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String bedrNr = RecordFields.getForetaksNummer(line);
    String funksjonsKode = RecordFields.getFunksjon(line);
    String kontoKode = RecordFields.getKontokode(line);

    boolean lineHasError = 
      duplicateChecker.isDuplicate(bedrNr+funksjonsKode+kontoKode, lineNumber);

    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + lf + lf;
    if (foundError())
    {
      HashMap<Integer, Vector<Integer>> errorMap = duplicateChecker.getDuplicateLineNumbers();      
      int numOfRecords = errorMap.size();
      errorReport += "\tRecorder som har samme funksjons- og kontokode, " +
      "gitt samme bedriftsnummer, blir " +
      "summert sammen for hvert bedriftsnummer." + lf + "\tDersom dette er feil må " +
      "recordene slettes før innsending til SSB." + lf;
      if (numOfRecords <= 10)
      {
        Iterator<Integer> keyIterator = (errorMap.keySet()).iterator();
        Iterator<Integer> lineIterator;
        Vector<Integer> container;
        String subLine, token;
        Integer firstOccurrence;
        int subLineLength = 0;
        while (keyIterator.hasNext())
        {
          firstOccurrence = (Integer) keyIterator.next();
          errorReport += lf + "\t- Kombinasjonen av bedriftsnummer, funksjon- og kontokode i record nr. " + firstOccurrence + 
          " er også brukt i følgende record(s):" + lf +"\t";
          container = (Vector<Integer>) errorMap.get(firstOccurrence);
          lineIterator = container.iterator();
          subLine = "";
          while (lineIterator.hasNext())
          {
            if (subLineLength > 70)
            {
              subLine += lf + "\t";
              subLineLength = 0;
            }
            token = (Integer) lineIterator.next() + " ";
            subLine += token;
            subLineLength += token.length();
          }
          errorReport += subLine + lf;          
        }
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return duplicateChecker.duplicatesFound();
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}
