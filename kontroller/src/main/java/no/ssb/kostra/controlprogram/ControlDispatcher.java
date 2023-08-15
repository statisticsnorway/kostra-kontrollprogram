package no.ssb.kostra.controlprogram;

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aMain;
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bMain;
import no.ssb.kostra.area.famvern.famvern53.Familievern53Main;
import no.ssb.kostra.area.regnskap.helseforetak.HelseForetakMain;
import no.ssb.kostra.area.regnskap.kostra.KirkeKostraMain;
import no.ssb.kostra.area.regnskap.kostra.KommuneKostraMain;
import no.ssb.kostra.area.regnskap.kostra.KvartalKostraMain;
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringMain;
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpMain;
import no.ssb.kostra.control.barnevern.s15.BarnevernMain;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.program.util.ConversionUtils;
import no.ssb.kostra.validation.ValidationResult;

import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodeList;
import static no.ssb.kostra.program.util.ConversionUtils.fromArguments;

public final class ControlDispatcher {

    private ControlDispatcher() {
    }

    public static ErrorReport doControls(final Arguments arguments) {

        ErrorReport errorReport = new ErrorReport(arguments);

        if (isCodeInCodeList(
                arguments.getSkjema()
                , List.of("0AK1", "0AK2", "0AK3", "0AK4",
                        "0BK1", "0BK2", "0BK3", "0BK4",
                        "0CK1", "0CK2", "0CK3", "0CK4",
                        "0DK1", "0DK2", "0DK3", "0DK4"))) {

            var validationResult = new KvartalKostraMain(fromArguments(arguments, true)).validate();
            toErrorReport(errorReport, validationResult);

        } else if (isCodeInCodeList(
                arguments.getSkjema()
                , List.of("0A", "0B", "0C", "0D",
                        "0I", "0J", "0K", "0L",
                        "0M", "0N", "0P", "0Q"))) {

            var validationResult = new KommuneKostraMain(fromArguments(arguments, true)).validate();
            toErrorReport(errorReport, validationResult);

        } else if (isCodeInCodeList(arguments.getSkjema(), List.of("0F", "0G"))) {
            var validationResult = new KirkeKostraMain(fromArguments(arguments, true)).validate();
            toErrorReport(errorReport, validationResult);

        } else if (isCodeInCodeList(arguments.getSkjema(), List.of("0X", "0Y"))) {
            var validationResult = new HelseForetakMain(fromArguments(arguments, true)).validate();
            toErrorReport(errorReport, validationResult);

        } else if (isCodeInCodeList(arguments.getSkjema(), List.of("11F"))) {
            var validationResult = new SosialhjelpMain(fromArguments(arguments, true)).validate();
            toErrorReport(errorReport, validationResult);

        } else if (isCodeInCodeList(arguments.getSkjema(), List.of("11CF"))) {
            var validationResult = new KvalifiseringMain(fromArguments(arguments, true)).validate();
            toErrorReport(errorReport, validationResult);

        } else if (isCodeInCodeList(arguments.getSkjema(), List.of("15F"))) {
            errorReport = BarnevernMain.doControls(arguments);

        } else if (isCodeInCodeList(arguments.getSkjema(), List.of("52AF"))) {
            var validationResult = new Familievern52aMain(fromArguments(arguments, true)).validate();
            toErrorReport(errorReport, validationResult);

        } else if (isCodeInCodeList(arguments.getSkjema(), List.of("52BF"))) {
            var validationResult = new Familievern52bMain(fromArguments(arguments, true)).validate();
            toErrorReport(errorReport, validationResult);

        } else if (isCodeInCodeList(arguments.getSkjema(), List.of("53F"))) {
            var validationResult = new Familievern53Main(fromArguments(arguments, true)).validate();
            toErrorReport(errorReport, validationResult);

        } else if (isCodeInCodeList(arguments.getSkjema(), List.of("55F"))) {
            errorReport = no.ssb.kostra.control.famvern.s55.Main.doControls(arguments);

        } else {
            errorReport = new ErrorReport(arguments);
            errorReport.addEntry(
                    new ErrorReportEntry(
                            " "
                            , " "
                            , " "
                            , " "
                            , "Ukjent skjema"
                            , "Korrigér filutrekket. Forventet '"
                            + arguments.getSkjema()
                            + "', men fant ikke noe. Avslutter...."
                            , Constants.CRITICAL_ERROR
                    )
            );
        }
        return errorReport;
    }

    private static void toErrorReport(ErrorReport errorReport, ValidationResult validationResult) {
        validationResult.getReportEntries().stream()
                .map(ConversionUtils::toErrorReportEntry)
                .forEach(errorReport::addEntry);

        errorReport.setCount(validationResult.getNumberOfControls());
    }
}
