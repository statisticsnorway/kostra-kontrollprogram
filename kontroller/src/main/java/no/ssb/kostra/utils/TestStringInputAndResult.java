package no.ssb.kostra.utils;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;

public class TestStringInputAndResult {
    private final Arguments arguments;
    private final String string;
    private final boolean result;
    private final ErrorReport errorReport;
    private final int expectedErrorType;

    public TestStringInputAndResult(Arguments arguments, String string, boolean result, int expectedErrorType) {
        this.arguments = arguments;
        this.errorReport = new ErrorReport(arguments);
        this.string = string;
        this.result = result;
        this.expectedErrorType = expectedErrorType;
    }

    public String getString() {
        return string;
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
                ", string=" + string +
                '}';
    }
}
