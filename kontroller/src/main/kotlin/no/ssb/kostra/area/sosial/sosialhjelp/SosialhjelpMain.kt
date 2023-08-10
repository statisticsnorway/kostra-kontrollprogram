package no.ssb.kostra.area.sosial.sosialhjelp

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.Validator
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.sosial.rule.*
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule.*

class SosialhjelpMain(arguments: KotlinArguments) : Validator(arguments) {
    override val fieldDefinitions = SosialhjelpFieldDefinitions

    override val fatalRules = listOf(
        Rule001RecordLength(SosialhjelpFieldDefinitions.fieldLength)
    )

    override val validationRules = listOf(
        Rule003Kommunenummer(),
        Rule003Bydelsnummer(),
        Rule004OppgaveAar(),
        Rule005Fodselsnummer(),
        Rule005aFoedselsnummerDubletter(),
        Rule005bJournalnummerDubletter(),
        Rule006AlderUnder18Aar(),
        Rule007AlderEr68AarEllerOver(),
        Rule008Kjonn(),
        Rule009Sivilstand(),
        Rule010HarBarnUnder18(),
        Rule011HarBarnUnder18MotAntallBarnUnder18(),
        Rule012AntallBarnUnder18MotHarBarnUnder18(),
        Rule013AntallBarnUnder18(),

        Rule014ViktigsteKildeTilLivsOppholdGyldigeVerdier(),
        Rule015ViktigsteKildeTilLivsOppholdKode1(),
        Rule016ViktigsteKildeTilLivsOppholdKode2(),
        Rule017ViktigsteKildeTilLivsOppholdKode4(),
        Rule018ViktigsteKildeTilLivsOppholdKode6(),
        Rule019ViktigsteKildeTilLivsOppholdKode8(),
        Rule020ViktigsteKildeTilLivsOppholdKode3(),
        Rule021ViktigsteKildeTilLivsOppholdKode5(),
        Rule022TilknytningTilTrygdesystemetOgAlder(),
        Rule023TilknytningTilTrygdesystemetOgBarn(),
        Rule024TilknytningTilTrygdesystemetOgArbeidssituasjon(),
        Rule024BTilknytningTilTrygdesystemetOgArbeidssituasjon(),
        Rule025ArbeidssituasjonGyldigeKoder(),
        Rule026Stonadsmaaneder(),
        Rule027StonadssumMangler(),
        Rule030StonadssumMaksimum(),
        Rule031StonadssumMinimum(),
        Rule032OkonomiskRaadgivningGyldigeKoder(),
        Rule033UtarbeidelseAvIndividuellPlanGyldigeKoder(),
        Rule035BoligsituasjonGyldigeKoder(),
        Rule036BidragSum(),
        Rule037LaanSum(),
        Rule038DufNummer(),
        Rule039Vilkar(),
        Rule040VilkarSamEkt(),
        Rule041DatoForUtbetalingsvedtak(),
        Rule042TilOgMedDatoForUtbetalingsvedtak(),
        Rule043UtfyltVilkar()
    )
}
// TODO() Legg til stats
/*
        // Lager statistikkrapport
        {
            if (errorReport.getErrorType() < Constants.CRITICAL_ERROR) {
                final var bidragSum = records.stream().map(r -> r.getFieldAsIntegerDefaultEquals0(BIDRAG)).reduce(0, Integer::sum);
                final var laanSum = records.stream().map(r -> r.getFieldAsIntegerDefaultEquals0(LAAN)).reduce(0, Integer::sum);
                final var stonadSum = bidragSum + laanSum;

                errorReport.addStats(new StatsReportEntry(
                        "Sum"
                        , List.of(
                        new Code(STONAD, "Stønad")
                        , new Code(BIDRAG, "Bidrag")
                        , new Code(LAAN, "Lån"))
                        , List.of(
                        new StatsEntry(STONAD, Integer.toString(stonadSum))
                        , new StatsEntry(BIDRAG, bidragSum.toString())
                        , new StatsEntry(LAAN, laanSum.toString()))));

                final var gyldigeRecordsAlder = records.stream().filter(r -> r.getFieldAsInteger(FNR_OK) == 1 && r.getFieldAsInteger(ALDER) != -1).map(r -> r.getFieldAsInteger(ALDER)).toList();
                errorReport.addStats(new StatsReportEntry(
                        "Tilfeller"
                        , List.of(
                        new Code("I_ALT", "I alt")
                        , new Code("0_17", "Under 18")
                        , new Code("18_24", "18 - 24")
                        , new Code("25_44", "25 - 44")
                        , new Code("45_66", "45 - 66")
                        , new Code("67_999", "67 og over")
                        , new Code("UGYLDIG_FNR", "Ugyldig fnr")
                )
                        , List.of(
                        new StatsEntry("I_ALT", String.valueOf(records.size()))
                        , new StatsEntry("0_17", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 0, 17)).count()))
                        , new StatsEntry("18_24", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 18, 24)).count()))
                        , new StatsEntry("25_44", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 25, 44)).count()))
                        , new StatsEntry("45_66", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 45, 66)).count()))
                        , new StatsEntry("67_999", String.valueOf(gyldigeRecordsAlder.stream().filter(i -> Comparator.between(i, 67, 999)).count()))
                        , new StatsEntry("UGYLDIG_FNR", String.valueOf(records.stream().filter(r -> r.getFieldAsInteger(FNR_OK) != 1).count()))
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
                        "Stønadstid"
                        , List.of(
                        new Code("1", "1 måned")
                        , new Code("2_3", "2 - 3 måneder")
                        , new Code("4_6", "4 - 6 måneder")
                        , new Code("7_9", "7 - 9 måneder")
                        , new Code("10_11", "10 - 11 måneder")
                        , new Code("12", "12 måneder")
                        , new Code("UOPPGITT", UOPPGITT))
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
                        .map(r -> r.getFieldAsIntegerDefaultEquals0(BIDRAG) + r.getFieldAsIntegerDefaultEquals0(LAAN))
                        .toList();
                errorReport.addStats(new StatsReportEntry(
                        "Stønad"
                        , List.of(
                        new Code("1_9", "1 - 9999")
                        , new Code("10_49", "10000 - 49999")
                        , new Code("50_99", "50000 - 99999")
                        , new Code("100_149", "100000 - 149999")
                        , new Code("150_9999", "150000 og over")
                        , new Code("0", UOPPGITT)
                )
                        , List.of(
                        new StatsEntry("1_9", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 1, 9999)).count()))
                        , new StatsEntry("10_49", String.valueOf(gyldigeRecordsStonad.stream().filter(i -> Comparator.between(i, 10000, 49999)).count()))
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

 */