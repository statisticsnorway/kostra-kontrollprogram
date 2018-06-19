package no.ssb.kostra.control.sensitiv.famvern_53;

import no.ssb.kostra.control.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ojj on 18.06.2018.
 */
public class ControlTiltak
        extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private String ERROR_TEXT;
    private String controlTitle;
    private int fieldNo;
    private List<Integer> lineNumbers = new ArrayList<>();

    public ControlTiltak(String controlId, String controlTitle, int fieldNo) {
        this.controlTitle = controlTitle;
        this.fieldNo = fieldNo;
        this.ERROR_TEXT = new String().concat(controlId).concat(": ").concat(controlTitle).concat(", tiltak");
    }

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        String tiltak = RecordFields.getFieldValue(line, fieldNo);

        try {
            if (tiltak != null) {
                int value = Integer.parseInt(tiltak);
                return (value < 1);

            } else {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            lineNumbers.add(new Integer(lineNumber));

            return true;
        }
    }

    @Override
    public String getErrorReport(int totalLineNumber) {
        String errorReport = ERROR_TEXT + ":" + lf + lf;
        if (foundError()) {
            int numOfRecords = lineNumbers.size();
            errorReport += "\tFeil: i " + numOfRecords +
                    " record" + (numOfRecords == 1 ? "" : "s") +
                    " det er ikke fylt hvor mange tiltak kontoret har gjennomført når det gjelder " + lf +
                    "\t'" + controlTitle + "'. Sjekk om det er glemt å rapportere tiltak for '" + controlTitle + "'.";
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
