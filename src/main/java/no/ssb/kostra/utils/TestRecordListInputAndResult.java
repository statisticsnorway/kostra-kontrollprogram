package no.ssb.kostra.utils;


import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.Record;

import java.util.List;

public class TestRecordListInputAndResult {
    private final Arguments arguments;
    private final List<Record> recordList;
    private final boolean result;
    private final ErrorReport errorReport;
    private final int expectedErrorType;

    public TestRecordListInputAndResult(Arguments arguments, List<Record> recordList, boolean result, int expectedErrorType) {
        this.arguments = arguments;
        this.errorReport = new ErrorReport(arguments);
        this.recordList = recordList;
        this.result = result;
        this.expectedErrorType = expectedErrorType;
    }

    public List<Record> getRecordList() {
        return recordList;
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
                ", recordList=" + recordList +
                '}';
    }
}