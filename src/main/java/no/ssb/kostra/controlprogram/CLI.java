package no.ssb.kostra.controlprogram;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodeList;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class CLI {

    public static void main(final String[] args) {

        ErrorReport errorReport;
        int error_type_found;

        try {
            final var arguments = new Arguments(args);

            if (isCodeInCodeList(
                    arguments.getSkjema()
                    , List.of("0AK1", "0AK2", "0AK3", "0AK4",
                            "0BK1", "0BK2", "0BK3", "0BK4",
                            "0CK1", "0CK2", "0CK3", "0CK4",
                            "0DK1", "0DK2", "0DK3", "0DK4"))) {
                errorReport = no.ssb.kostra.control.regnskap.kvartal.Main.doControls(arguments);

            } else if (isCodeInCodeList(
                    arguments.getSkjema()
                    , List.of("0A", "0B", "0C", "0D",
                            "0I", "0J", "0K", "0L",
                            "0M", "0N", "0P", "0Q"))) {
                errorReport = no.ssb.kostra.control.regnskap.kostra.Main.doControls(arguments);

            } else if (isCodeInCodeList(arguments.getSkjema(), List.of("0F", "0G"))) {
                errorReport = no.ssb.kostra.control.regnskap.kirkekostra.Main.doControls(arguments);

            } else if (isCodeInCodeList(arguments.getSkjema(), List.of("0X", "0Y"))) {
                errorReport = no.ssb.kostra.control.regnskap.helseforetak.Main.doControls(arguments);

            } else if (isCodeInCodeList(arguments.getSkjema(), List.of("11F"))) {
                errorReport = no.ssb.kostra.control.sosial.s11_sosialhjelp.Main.doControls(arguments);

            } else if (isCodeInCodeList(arguments.getSkjema(), List.of("11CF"))) {
                errorReport = no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad.Main.doControls(arguments);

            } else if (isCodeInCodeList(arguments.getSkjema(), List.of("15F"))) {
                errorReport = no.ssb.kostra.control.barnevern.s15.Main.doControls(arguments);

            } else if (isCodeInCodeList(arguments.getSkjema(), List.of("52AF"))) {
                errorReport = no.ssb.kostra.control.famvern.s52a.Main.doControls(arguments);

            } else if (isCodeInCodeList(arguments.getSkjema(), List.of("52BF"))) {
                errorReport = no.ssb.kostra.control.famvern.s52b.Main.doControls(arguments);

            } else if (isCodeInCodeList(arguments.getSkjema(), List.of("53F"))) {
                errorReport = no.ssb.kostra.control.famvern.s53.Main.doControls(arguments);

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
                                , "Korrig??r filutrekket. Forventet '"
                                + arguments.getSkjema()
                                + "', men fant ikke noe. Avslutter...."
                                , Constants.CRITICAL_ERROR
                        )
                );
            }

            error_type_found = errorReport.getErrorType();

            final var printStream = new PrintStream(System.out, true, StandardCharsets.ISO_8859_1);
            printStream.print(errorReport.generateReport());

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            error_type_found = Constants.PARAMETER_ERROR;

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            error_type_found = Constants.CRITICAL_ERROR;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            error_type_found = Constants.SYSTEM_ERROR;
        }

        System.exit(error_type_found);
    }
}
