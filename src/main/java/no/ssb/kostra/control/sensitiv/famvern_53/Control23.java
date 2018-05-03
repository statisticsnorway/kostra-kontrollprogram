package no.ssb.kostra.control.sensitiv.famvern_53;

import no.ssb.kostra.control.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ojj on 03.05.2018.
 */
public final class Control23
        extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private final String ERROR_TEXT = "K23: Foreldreveiledning, timer";
    private List<Integer> lineNumbers = new ArrayList<>();

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError;
        String fieldTiltak = RecordFields.getFieldValue(line, 111);
        String fieldTimer = RecordFields.getFieldValue(line, 112);

        try {
            int tiltak = Integer.parseInt(fieldTiltak);
            int timer = Integer.parseInt(fieldTimer);
            lineHasError = 0 < tiltak && timer < 1;
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
                    " record" + (numOfRecords == 1 ? "" : "s") + lf +
                    "Feltet er ikke fylt ut." + lf +
                    "Det er ikke fylt ut hvor mange timer kontoret har gjennomført når det gjelder " + lf +
                    "\t'Tilsyn'. , til tross for at det er oppgitt antall tiltak.";
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

    public int getErrorType() {
        return Constants.NORMAL_ERROR;
    }
}