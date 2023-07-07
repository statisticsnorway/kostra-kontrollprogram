package no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlHarVedlegg;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;
import no.ssb.kostra.program.util.ConversionUtils;

import java.util.List;

import static no.ssb.kostra.program.util.ConversionUtils.fromArguments;
import static no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringValidator.validateKvalifisering;

@SuppressWarnings("SpellCheckingInspection")
public class Main {
    // Konstanter
    private static final String ALDER = "ALDER";
    private static final String FNR_OK = "FNR_OK";
    private static final String KVP_STONAD = "KVP_STONAD";
    private static final List<String> STMND = List.of("STMND_1", "STMND_2", "STMND_3", "STMND_4", "STMND_5", "STMND_6", "STMND_7", "STMND_8", "STMND_9", "STMND_10", "STMND_11", "STMND_12");


    private Main() {
    }

    public static ErrorReport doControls(Arguments arguments) {
        final var errorReport = new ErrorReport(arguments);
        errorReport.incrementCount();
        errorReport.setReportHeaders(List.of("Saksbehandler", "Journalnummer", "Kontroll", "Melding"));
        final var inputFileContent = arguments.getInputContentAsStringList();


        // Sjekker om man skal ha vedlegg
        if (ControlHarVedlegg.doControl(errorReport)) {
            return errorReport;
        }

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        if (ControlRecordLengde.doControl(inputFileContent, errorReport, FieldDefinitions.getFieldLength())) {
            return errorReport;
        }

        final var fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        final var records = inputFileContent.stream()
                .map(p -> new KostraRecord(p, fieldDefinitions))
                .toList();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, errorReport);

        if (errorReport.isEmpty()) {
            var validationResult = validateKvalifisering(fromArguments(arguments, true));

            validationResult.getReportEntries().stream()
                    .map(ConversionUtils::toErrorReportEntry)
                    .forEach(errorReport::addEntry);

            errorReport.setCount(validationResult.getNumberOfControls());
        }

        // Kontroller ferdig
        // Lager statistikkrapport
        {
            if (errorReport.getErrorType() < Constants.CRITICAL_ERROR) {

                final var stonadSum = records.stream().map(r -> r.getFieldAsIntegerDefaultEquals0(KVP_STONAD)).reduce(0, Integer::sum);

                errorReport.addStats(new StatsReportEntry(
                        "Sum"
                        , List.of(
                        new Code("STONAD", "Stønad")
                )
                        , List.of(
                        new StatsEntry("STONAD", stonadSum.toString())
                )
                ));

                final var gyldigeRecordsAlder = records.stream().filter(r -> r.getFieldAsInteger(FNR_OK) == 1 && r.getFieldAsInteger(ALDER) != -1).map(r -> r.getFieldAsInteger(ALDER)).toList();
                errorReport.addStats(new StatsReportEntry(
                        "Kvalifiseringsdeltakere"
                        , List.of(
                        new Code("I_ALT", "I alt")
                        , new Code("0_19", "19 år eller yngre")
                        , new Code("20_24", "20 - 24 år")
                        , new Code("25_29", "25 - 29 år")
                        , new Code("30_39", "30 - 39 år")
                        , new Code("40_49", "40 - 49 år")
                        , new Code("50_66", "50 - 66 år")
                        , new Code("67_999", "67 år eller eldre")
                        , new Code("UGYLDIG_FNR", "Alder ikke mulig å beregne")
                )
                        , List.of(
                        new StatsEntry("I_ALT", String.valueOf(records.size()))
                        , new StatsEntry("0_19", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 0, 19)).count()))
                        , new StatsEntry("20_24", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 20, 24)).count()))
                        , new StatsEntry("25_29", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 25, 29)).count()))
                        , new StatsEntry("30_39", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 30, 39)).count()))
                        , new StatsEntry("40_49", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 40, 49)).count()))
                        , new StatsEntry("50_66", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 45, 66)).count()))
                        , new StatsEntry("67_999", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 67, 999)).count()))
                        , new StatsEntry("UGYLDIG_FNR", String.valueOf(records.stream().filter(r -> r.getFieldAsInteger(FNR_OK) == 0).count()))
                )
                ));

                final var gyldigeRecordsStonadstid = records.stream()
                        .map(r -> STMND
                                .stream()
                                .filter(m -> 0 < r.getFieldAsIntegerDefaultEquals0(m))
                                .count()
                        )
                        .toList();

                errorReport.addStats(new StatsReportEntry(
                        "Stønadsvarighet"
                        , List.of(
                        new Code("1", "1 måned")
                        , new Code("2_3", "2 - 3 måneder")
                        , new Code("4_6", "4 - 6 måneder")
                        , new Code("7_9", "7 - 9 måneder")
                        , new Code("10_11", "10 - 11 måneder")
                        , new Code("12", "12 måneder")
                        , new Code("UOPPGITT", "Uoppgitt")
                )
                        , List.of(
                        new StatsEntry("1", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> i == 1).count()))
                        , new StatsEntry("2_3", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 2, 3)).count()))
                        , new StatsEntry("4_6", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 4, 6)).count()))
                        , new StatsEntry("7_9", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 7, 9)).count()))
                        , new StatsEntry("10_11", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 10, 11)).count()))
                        , new StatsEntry("12", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> Comparator.between(i.intValue(), 12, 12)).count()))
                        , new StatsEntry("UOPPGITT", String.valueOf(gyldigeRecordsStonadstid.stream().filter(i -> i == 0).count()))
                )
                ));

                final var gyldigeRecordsStonad = records.stream()
                        .map(r -> r.getFieldAsIntegerDefaultEquals0(KVP_STONAD))
                        .toList();

                errorReport.addStats(new StatsReportEntry(
                        "Stønad"
                        , List.of(
                        new Code("1_7", " - 7999 kr")
                        , new Code("8_49", "8000 - 49999 kr")
                        , new Code("50_99", "50000 - 99999 kr")
                        , new Code("100_149", "100000 - 149999 kr")
                        , new Code("150_9999", "150000 kr og over")
                        , new Code("0", "Uoppgitt")
                )
                        , List.of(
                        new StatsEntry("1_7", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 1, 7999)).count()))
                        , new StatsEntry("8_49", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 8000, 49999)).count()))
                        , new StatsEntry("50_99", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 50000, 99999)).count()))
                        , new StatsEntry("100_149", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 100000, 149999)).count()))
                        , new StatsEntry("150_9999", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> 150000 <= i).count()))
                        , new StatsEntry("0", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> i == 0).count()))
                )
                ));
            }
        }
        return errorReport;
    }
}
