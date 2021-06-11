package no.ssb.kostra.utils;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.Record;

public class TestRecordInputAndResult {
    private final Arguments arguments;
    private final Record record;
    private final boolean result;
    private final ErrorReport errorReport;
    private final int expectedErrorType;

    public TestRecordInputAndResult(Arguments arguments, Record record, boolean result, int expectedErrorType) {
        this.arguments = arguments;
        this.errorReport = new ErrorReport(arguments);
        this.record = record;
        this.result = result;
        this.expectedErrorType = expectedErrorType;
    }

    public Record getRecord() {
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