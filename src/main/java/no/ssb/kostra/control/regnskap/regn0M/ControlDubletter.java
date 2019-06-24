package no.ssb.kostra.control.regnskap.regn0M;

import java.util.*;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DuplicateChecker;



public final class ControlDubletter extends no.ssb.kostra.control.Control
{
  private DuplicateChecker duplicateChecker = new DuplicateChecker();
  

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {  
    String kontoklasse = RecordFields.getKontoklasse(line);
    String funksjon = RecordFields.getFunksjon(line);
    String art = RecordFields.getArt(line);
  
    boolean lineHasError = 
            duplicateChecker.isDuplicate(kontoklasse+funksjon+art, lineNumber);
    
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 11, dubletter:" + lf + lf;
    errorReport += "\tAdvarsel: dubletter (lik kontoklasse * funksjon * art) " +
    "summeres sammen." + lf;

    HashMap<Integer, Vector<Integer>> errorMap = duplicateChecker.getDuplicateLineNumbers();      
    int numOfRecords = errorMap.size();
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
          errorReport += lf + "\t- Kombinasjonen av kontoklasse, funksjon og art i record nr. " + firstOccurrence + 
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
      else
      {
        errorReport += "\t("+numOfRecords+" dubletter funnet.)" + lf;
      }
    errorReport += lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return duplicateChecker.duplicatesFound();
  }  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}