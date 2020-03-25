package no.ssb.kostra.control.regnskap.regn0Akvartal;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlRecordlengde extends no.ssb.kostra.control.Control {
    private final int RECORD_LENGTH = 48;
    private Vector<Integer> lineNumbers = new Vector<>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = line.length() != RECORD_LENGTH;

        if (lineHasError)
            lineNumbers.add(new Integer(lineNumber));

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        StringBuffer errorReport =
                new StringBuffer("Kontroll 1, recordlengde:" + lf + lf);

        if (foundError()) {

            int numOfRecords = lineNumbers.size();
            errorReport.append("\tFeil: feil antall posisjoner i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + "." + lf +
                    "\tNB! Records med feil lengde tas ikke med i videre kontroller." + lf +
                    "\tDette kan medføre at ytterligere feil oppstår." + lf);

            if (numOfRecords <= 10) {
                errorReport.append("\t(Gjelder record nr.");

                for (int i = 0; i < numOfRecords; i++)
                    errorReport.append(" " + lineNumbers.elementAt(i));

                errorReport.append(").");
            }
        }

        errorReport.append(lf + lf);

        return errorReport.toString();
    }

    public boolean foundError() {
        return lineNumbers.size() > 0;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}