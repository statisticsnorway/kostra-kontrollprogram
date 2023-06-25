package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.program.util.ConversionUtils;

import static no.ssb.kostra.program.util.ConversionUtils.fromArguments;
import static no.ssb.kostra.validation.rule.barnevern.BarnevernValidator.validateBarnevern;

public final class BarnevernMain {
    private BarnevernMain() {
        throw new IllegalStateException("Static processing class");
    }

    public static ErrorReport doControls(final Arguments arguments) {
        final var errorReport = new ErrorReport(arguments);

        var result = validateBarnevern(fromArguments(arguments, false));

        result.getReportEntries().stream()
                .map(ConversionUtils::toErrorReportEntry)
                .forEach(errorReport::addEntry);

        errorReport.setCount(result.getNumberOfControls());
        return errorReport;
    }
}
