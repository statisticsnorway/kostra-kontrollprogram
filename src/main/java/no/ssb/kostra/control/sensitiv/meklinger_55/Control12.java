package no.ssb.kostra.control.sensitiv.meklinger_55;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by ojj on 09.04.2019.
 */
public class Control12 extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K12: Summeringskontroll felt 10.1 til 10.24";
    private Vector<Integer> invalidRegions = new Vector<>();

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = !SumChecker.validateSum(line, 104, Arrays.asList(101, 102, 103))
                && !SumChecker.validateSum(line, 108, Arrays.asList(105, 106, 107))
                && !SumChecker.validateSum(line, 1012, Arrays.asList(109, 1010, 1011))
                && !SumChecker.validateSum(line, 1016, Arrays.asList(1013, 1014, 1015))
                && !SumChecker.validateSum(line, 1020, Arrays.asList(1017, 1018, 1019))
                && !SumChecker.validateSum(line, 1024, Arrays.asList(1021, 1022, 1023))
                && !SumChecker.validateSum(line, 1024, Arrays.asList(104, 108, 1012, 1016, 1020));

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
        return Constants.NORMAL_ERROR;
    }
}



