package no.ssb.kostra.control.sensitiv.famvern_b;

/*
 * 
 */
 
import java.util.*;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DuplicateChecker;

public final class ControlDubletter extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K6: Dublett på gruppenummer";
  private DuplicateChecker duplicateChecker = new DuplicateChecker();
  

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String gruppeNr = RecordFields.getGruppeNr(line);
    
    boolean lineHasError = 
      duplicateChecker.isDuplicate(gruppeNr, lineNumber);

    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    if (foundError())
    {
      HashMap<Integer, Vector<Integer>> errorMap = duplicateChecker.getDuplicateLineNumbers();      
      int numOfRecords = errorMap.size();
      errorReport += "\tFeil: Gruppenummeret i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + " er benyttet " + lf + 
      "\tpå mer enn en sak." + lf;
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
