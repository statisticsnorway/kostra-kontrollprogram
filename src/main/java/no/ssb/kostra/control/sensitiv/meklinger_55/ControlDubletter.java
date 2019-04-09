package no.ssb.kostra.control.sensitiv.meklinger_55;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DuplicateChecker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public final class ControlDubletter extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K4: Dublett på fylkenummer";
    private DuplicateChecker duplicateChecker = new DuplicateChecker();


    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String fylkeNumber = RecordFields.getFieldValue(line, 1);

        boolean lineHasError =
                duplicateChecker.isDuplicate(fylkeNumber, lineNumber);

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf + lf;
        if (foundError()) {
            HashMap<Integer, Vector<Integer>> errorMap = duplicateChecker.getDuplicateLineNumbers();
            int numOfRecords = errorMap.size();
            errorReport += "\tFeil: Fylkesnummeret i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") +
                    " er benyttet på mer enn en record." + lf;
            if (numOfRecords <= 10) {
                Iterator<Integer> keyIterator = (errorMap.keySet()).iterator();
                Iterator<Integer> lineIterator;
                Vector<Integer> container;
                String subLine, token;
                Integer firstOccurrence;
                int subLineLength = 0;
                while (keyIterator.hasNext()) {
                    firstOccurrence = (Integer) keyIterator.next();
                    errorReport += lf + "\t- Fylkesnummeret i record nr. " + firstOccurrence +
                            " er også brukt i følgende record(s):" + lf + "\t";
                    container = (Vector<Integer>) errorMap.get(firstOccurrence);
                    lineIterator = container.iterator();
                    subLine = "";
                    while (lineIterator.hasNext()) {
                        if (subLineLength > 70) {
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

    public boolean foundError() {
        return duplicateChecker.duplicatesFound();
    }

    public String getErrorText() {
        return ERROR_TEXT;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}
