package no.ssb.kostra.control.sensitiv.meklinger_55;

import no.ssb.kostra.control.Constants;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by ojj on 09.04.2019.
 */
public class Control7 extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K7: Summeringskontroll felt 5.1 til 5.30";
    private Vector<Integer> invalidRegions = new Vector<>();

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError = !SumChecker.validateSum(line, 55, Arrays.asList(51, 52, 53, 54))
                && !SumChecker.validateSum(line, 510, Arrays.asList(56, 57, 58, 59))
                && !SumChecker.validateSum(line, 515, Arrays.asList(511, 512, 513, 514))
                && !SumChecker.validateSum(line, 520, Arrays.asList(516, 517, 518, 519))
                && !SumChecker.validateSum(line, 525, Arrays.asList(521, 522, 523, 524))
                && !SumChecker.validateSum(line, 530, Arrays.asList(526, 527, 528, 529))
                && !SumChecker.validateSum(line, 530, Arrays.asList(55, 510, 515, 520, 525));

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
