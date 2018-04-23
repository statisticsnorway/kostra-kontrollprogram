package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: ControlDubletter.java,v $
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
 
import java.util.*;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DuplicateChecker;

public final class ControlDubletter extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K6: Dublett på journalnummer";
  private DuplicateChecker duplicateChecker = new DuplicateChecker();
  

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String kontorNumber = RecordFields.getKontornummer (line);
    String journalNumber = RecordFields.getJournalnummer(line);

    boolean lineHasError = 
      duplicateChecker.isDuplicate(kontorNumber+journalNumber, lineNumber); 

    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    if (foundError())
    {
      HashMap<Integer, Vector<Integer>> errorMap = duplicateChecker.getDuplicateLineNumbers();      
      int numOfRecords = errorMap.size();
      errorReport += "\tFeil: journalnummeret i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + 
      " er benyttet på mer enn en sak." + lf;
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
          errorReport += lf + "\t- Journalnummeret i record nr. " + firstOccurrence + 
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
