package no.ssb.kostra.utils;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.KostraRecord;

public class TestRecordInputAndResult {
    private final Arguments arguments;
    private final KostraRecord record;
    private final boolean result;
    private final ErrorReport errorReport;
    private final int expectedErrorType;

    public TestRecordInputAndResult(Arguments arguments, KostraRecord record, boolean result, int expectedErrorType) {
        this.arguments = arguments;
        this.errorReport = new ErrorReport(arguments);
        this.record = record;
        this.result = result;
        this.expectedErrorType = expectedErrorType;
    }

    public KostraRecord getRecord() {
        return record;
    }

    public boolean isResult() {
        return result;
    }

    public ErrorReport getErrorReport() {
        return errorReport;
    }

    public int getExpectedErrorType() {
        return expectedErrorType;
    }

    @Override
    public String toString() {
        return "{" +
                "result=" + result +
                ", expectedErrorType=" + expectedErrorType +
                ", arguments=" + arguments +
                ", record=" + record +
                '}';
    }
}