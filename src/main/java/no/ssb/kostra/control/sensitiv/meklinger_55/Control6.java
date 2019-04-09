package no.ssb.kostra.control.sensitiv.meklinger_55;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by ojj on 09.04.2019.
 */
public class Control6 extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K6: Summeringskontroll felt 4.1 til 4.18";
    private Vector<Integer> invalidRegions = new Vector<>();

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = SumChecker.validateSum(line, 43, Arrays.asList(41, 42))
                || SumChecker.validateSum(line, 46, Arrays.asList(44, 45))
                || SumChecker.validateSum(line, 49, Arrays.asList(47, 48))
                || SumChecker.validateSum(line, 412, Arrays.asList(410, 411))
                || SumChecker.validateSum(line, 415, Arrays.asList(413, 414))
                || SumChecker.validateSum(line, 418, Arrays.asList(416, 417))
                || SumChecker.validateSum(line, 418, Arrays.asList(43, 46, 49, 412, 415));

        if (lineHasError) {
            invalidRegions.add(new Integer(lineNumber));
        }

        return lineHasError;
    }


    @Override
    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf;
        int numOfRecords = invalidRegions.size();
        if (numOfRecords > 0) {
            errorReport += lf + "\tFeil: Summeringsfeil i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") + "." +
                    " Tallene summerer seg ikke som de skal.";
            if (numOfRecords <= 10) {
                errorReport += lf + "\t\t(Gjelder record nr.";
                for (int i = 0; i < numOfRecords; i++) {
                    errorReport += " " + invalidRegions.elementAt(i);
                }
                errorReport += ").";
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    @Override
    public String getErrorText() {
        return ERROR_TEXT;
    }

    @Override
    public boolean foundError() {
        return (invalidRegions.size() > 0);
    }

    @Override
    public int getErrorType() {
        return Constants.CRITICAL_ERROR;
    }
}

