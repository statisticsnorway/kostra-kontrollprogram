package no.ssb.helseforetak.control.regnskap.regn0Y;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class Control_19 extends no.ssb.kostra.control.Control {
    private final String ERROR_TEXT =
            "Kontroll 19, Konti 190, 192, 194, 195 inneholder kun positive beløp:";


    private Vector<Integer> linesWithError = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = false;

        String kontokode = RecordFields.getKontokode(line);

        if (kontokode.equalsIgnoreCase("190") ||
                kontokode.equalsIgnoreCase("192") ||
                kontokode.equalsIgnoreCase("194") ||
                kontokode.equalsIgnoreCase("195")) {

            try {
                int belop = Integer.parseInt(RecordFields.getBelop(line));
                lineHasError = belop < 0;
            } catch (NumberFormatException e) {
                //Kan ikke avgjøre om beløp er mindre enn 0.
            }
        }

        if (lineHasError)
            linesWithError.add(new Integer(lineNumber));

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        StringBuilder errorReport = new StringBuilder(ERROR_TEXT + lf + lf);
        int numOfRecords = linesWithError.size();
        if (numOfRecords > 0) {
            errorReport.append("\tFeil (i " + numOfRecords + " records): " +
                    "Det er ført kostnader på kontoen" + lf);
            if (numOfRecords <= 10) {
                errorReport.append("\t\t(Gjelder record nr.:");
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport.append(" " + linesWithError.elementAt(i));
                }
                errorReport.append(")." + lf);
            } else
                errorReport.append("\t\tGjelder flere enn 10 records.");
        }
        errorReport.append(lf + lf);
        return errorReport.toString();
    }

    public boolean foundError() {
        return linesWithError.size() > 0;
    }


    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}