package no.ssb.kostra.control.regnskap.regn0Dkvartal;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.DuplicateChecker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

final class ControlDubletter extends no.ssb.kostra.control.Control {
    private DuplicateChecker duplicateChecker = new DuplicateChecker();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String funksjon = RecordFields.getFunksjon(line);
        String art = RecordFields.getArt(line);

        boolean lineHasError =
                duplicateChecker.isDuplicate(funksjon + art, lineNumber);

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        StringBuilder errorReport = new StringBuilder("Kontroll 8, dubletter:" + lf + lf);
        errorReport.append("\tAdvarsel: dubletter (lik kapittel * sektor) " +
                "summeres sammen." + lf);

        if (foundError()) {
            HashMap<Integer, Vector<Integer>> errorMap = duplicateChecker.getDuplicateLineNumbers();
            int numOfRecords = errorMap.size();
            if (numOfRecords <= 10) {
                Iterator<Integer> keyIterator = (errorMap.keySet()).iterator();
                Iterator<Integer> lineIterator;
                Vector<Integer> container;
                StringBuilder subLine = new StringBuilder();
                String token;
                Integer firstOccurrence;
                int subLineLength = 0;
                while (keyIterator.hasNext()) {
                    firstOccurrence = keyIterator.next();
                    errorReport.append(lf + "\t- Kombinasjonen av kapittel og sektor i record nr. " + firstOccurrence +
                            " er også brukt i følgende record(s):" + lf + "\t");
                    container = errorMap.get(firstOccurrence);
                    lineIterator = container.iterator();
                    subLine.setLength(0);
                    while (lineIterator.hasNext()) {
                        if (subLineLength > 70) {
                            subLine.append(lf + "\t");
                            subLineLength = 0;
                        }
                        token = lineIterator.next() + " ";
                        subLine.append(token);
                        subLineLength += token.length();
                    }
                    errorReport.append(subLine + lf);
                }
            }
        }

        errorReport.append(lf);
        return errorReport.toString();
    }

    public boolean foundError() {
        return duplicateChecker.duplicatesFound();
    }

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}
