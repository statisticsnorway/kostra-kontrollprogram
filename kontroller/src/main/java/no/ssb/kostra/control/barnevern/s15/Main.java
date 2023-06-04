package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.program.ConversionUtils;

import static no.ssb.kostra.program.ConversionUtils.fromArguments;
import static no.ssb.kostra.validation.rule.barnevern.BarnevernValidator.validateBarnevern;

public final class Main {
    private Main() {
        throw new IllegalStateException("Static processing class");
    }

    public static ErrorReport doControls(final Arguments args) {
        final var errorReport = new ErrorReport(args);

        validateBarnevern(fromArguments(args)).stream()
                .map(ConversionUtils::toErrorReportEntry)
                .forEach(errorReport::addEntry);

        return errorReport;
    }
}
