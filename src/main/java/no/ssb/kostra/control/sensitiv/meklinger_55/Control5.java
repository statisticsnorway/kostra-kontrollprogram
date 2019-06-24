package no.ssb.kostra.control.sensitiv.meklinger_55;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by ojj on 09.04.2019.
 */
public class Control5 extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K5: Summeringskontroll felt 3.1 til 3.30";
    private Vector<Integer> invalidRegions = new Vector<>();

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = !SumChecker.validateSum(line, 34, Arrays.asList(31, 32, 33))
                && !SumChecker.validateSum(line, 39, Arrays.asList(36, 37, 38))
                && !SumChecker.validateSum(line, 314, Arrays.asList(311, 312, 313))
                && !SumChecker.validateSum(line, 319, Arrays.asList(316, 317, 318))
                && !SumChecker.validateSum(line, 324, Arrays.asList(321, 322, 323))
                && !SumChecker.validateSum(line, 329, Arrays.asList(326, 327, 328))
                && !SumChecker.validateSum(line, 329, Arrays.asList(34, 39, 314, 319, 324));

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
