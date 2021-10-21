package no.ssb.kostra.utils;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;

public class TestArgumentsInputAndResult {
    private final Arguments arguments;
    private final boolean result;
    private final ErrorReport errorReport;
    private final int expectedErrorType;

    public TestArgumentsInputAndResult(Arguments arguments, boolean result, int expectedErrorType) {
        this.arguments = arguments;
        this.errorReport = new ErrorReport(arguments);
        this.result = result;
        this.expectedErrorType = expectedErrorType;
    }

    public Arguments getArguments() {
        return arguments;
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
                '}';
    }
}
