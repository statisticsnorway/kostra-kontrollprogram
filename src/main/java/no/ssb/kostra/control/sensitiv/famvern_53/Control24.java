package no.ssb.kostra.control.sensitiv.famvern_53;

import no.ssb.kostra.control.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ojj on 03.05.2018.
 */
public class Control24
        extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K24: Annet, tiltak";
    private List<Integer> lineNumbers = new ArrayList<>();

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError;
        String tiltak = RecordFields.getFieldValue(line, 121);

        try {
            int value = Integer.parseInt(tiltak);
            lineHasError = value < 1;
        } catch (NumberFormatException e) {
            lineHasError = true;
        }

        if (lineHasError)
            lineNumbers.add(new Integer(lineNumber));

        return lineHasError;
    }

    @Override
    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf + lf;
        if (foundError()) {
            int numOfRecords = lineNumbers.size();
            errorReport += "\tFeil: i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") +
                    " det er ikke fylt hvor mange tiltak kontoret har gjennomført når det gjelder " + lf +
                    "'Annet'. Sjekk om det er glemt å rapportere 'Annet'.";
            if (numOfRecords <= 10) {
                String commaSeparatedNumbers = lineNumbers.stream()
                        .map(item -> item.toString())
                        .collect(Collectors.joining(", "));
                errorReport += lf + "\t\t(Gjelder record nr." + commaSeparatedNumbers + ").";
            }
        }
        errorReport += lf + lf;
        return errorReport;
    }

    @Override
    public boolean foundError() {
        return lineNumbers.size() > 0;
    }

    @Override
    public String getErrorText() {
        return ERROR_TEXT;
    }

    @Override
    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}
