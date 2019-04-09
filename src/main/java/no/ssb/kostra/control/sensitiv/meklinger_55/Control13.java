package no.ssb.kostra.control.sensitiv.meklinger_55;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by ojj on 09.04.2019.
 */
public class Control13 extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K13: Summeringskontroll felt 11.1 til 11.18";
    private Vector<Integer> invalidRegions = new Vector<>();

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = SumChecker.validateSum(line, 113, Arrays.asList(111, 112))
                || SumChecker.validateSum(line, 116, Arrays.asList(114, 115))
                || SumChecker.validateSum(line, 119, Arrays.asList(117, 118))
                || SumChecker.validateSum(line, 1112, Arrays.asList(1110, 1111))
                || SumChecker.validateSum(line, 1115, Arrays.asList(1113, 1114))
                || SumChecker.validateSum(line, 1118, Arrays.asList(1116, 1117))
                || SumChecker.validateSum(line, 1118, Arrays.asList(113, 116, 119, 1112, 1115));

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


