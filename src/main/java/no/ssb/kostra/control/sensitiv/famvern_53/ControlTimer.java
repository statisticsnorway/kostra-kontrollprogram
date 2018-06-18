package no.ssb.kostra.control.sensitiv.famvern_53;

import no.ssb.kostra.control.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ojj on 18.06.2018.
 */
public class ControlTimer
        extends no.ssb.kostra.control.Control
        implements no.ssb.kostra.control.SingleRecordErrorReport {
    private String ERROR_TEXT;
    private String controlId;
    private String controlTitle;
    private int fieldNoTiltak;
    private int fieldNoTimer;
    private List<Integer> lineNumbers = new ArrayList<>();

    public ControlTimer(String controlId, String controlTitle, int fieldNoTiltak, int fieldNoTimer){
        this.controlId = controlId;
        this.controlTitle = controlTitle;
        this.fieldNoTiltak = fieldNoTiltak;
        this.fieldNoTimer = fieldNoTimer;
        this.ERROR_TEXT = this.ERROR_TEXT.concat(controlId).concat(": ").concat(controlTitle).concat(", timer");
    }

    @Override
    public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
        boolean lineHasError;
        String fieldTiltak = RecordFields.getFieldValue(line, fieldNoTiltak);
        String fieldTimer = RecordFields.getFieldValue(line, fieldNoTimer);

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
                    "\t'\" + controlTitle + \"'. , til tross for at det er oppgitt antall tiltak.";
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