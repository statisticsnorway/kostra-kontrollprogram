package no.ssb.kostra.control.sensitiv.soshjelp;


import no.ssb.kostra.control.Constants;

import java.util.Vector;

public final class ControlBarnUnder18 extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K12: Det bor barn under 18 år i husholdningen";
    private Vector<Integer> linesWithError = new Vector<Integer>();

    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        int antBarnUnder18;
        try {
            antBarnUnder18 = Integer.parseInt(RecordFields.getFieldValue(line, 102));
        } catch (NumberFormatException e) {
            antBarnUnder18 = 0;
        }

        boolean lineHasError = false;

        if (antBarnUnder18 > 0) {

            String barnUnder18 = RecordFields.getFieldValue(line, 101);
            lineHasError = barnUnder18.equalsIgnoreCase("0") ||
                    barnUnder18.equalsIgnoreCase(" ") ||
                    barnUnder18.equalsIgnoreCase("2");
        }

        if (lineHasError)
            linesWithError.add(new Integer(lineNumber));

        return lineHasError;
    }

    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf;
        int numOfRecords = linesWithError.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + " er det oppgitt " +
                    "antall barn under 18 år som bor i husholdningen " + lf +
                    "\tsom mottaker eller ektefelle/samboer har forsørgerplikt for, " +
                    "men det er ikke" + lf + "\toppgitt at det bor barn i husholdningen. " + lf +
                    "\tFeltet er obligatorisk å fylle ut når det er oppgitt antall " +
                    "barn under 18 år som bor i husholdningen.";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + linesWithError.elementAt(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    public boolean foundError() {
        return linesWithError.size() > 0;
    }

    public String getErrorText() {
        return ERROR_TEXT;
    }

    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}