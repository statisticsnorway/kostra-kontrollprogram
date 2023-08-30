package no.ssb.kostra.area.sosial.kvalifisering


import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.report.StatsEntry
import no.ssb.kostra.validation.report.StatsReportEntry
import no.ssb.kostra.validation.rule.Rule000HasAttachment
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.sosial.extensions.ageInYears
import no.ssb.kostra.validation.rule.sosial.extensions.hasFnr
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.*
import no.ssb.kostra.validation.rule.sosial.rule.*

class KvalifiseringMain(
    arguments: KotlinArguments
) : PositionedFileValidator(arguments) {
    override val fieldDefinitions = KvalifiseringFieldDefinitions

    override val preValidationRules = listOf(
        Rule000HasAttachment(),
        Rule001RecordLength(KvalifiseringFieldDefinitions.fieldLength)
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

        Rule014RegistreringsDato(),
        Rule015VedtakDato(),
        Rule016BegyntDato(),
        Rule019KvalifiseringsprogramIAnnenKommune(),
        Rule020KvalifiseringsprogramIAnnenKommuneKommunenummer(),
        Rule021Ytelser(),
        Rule026MottattStotte(),
        Rule027MottattOkonomiskSosialhjelp(),
        Rule028MaanederMedKvalifiseringsstonad(),
        Rule029KvalifiseringssumMangler(),
        Rule030HarVarighetMenManglerKvalifiseringssum(),
        Rule031HarKvalifiseringssumMenManglerVarighet(),
        Rule032KvalifiseringssumOverMaksimum(),
        Rule036StatusForDeltakelseIKvalifiseringsprogram(),
        Rule037DatoForAvsluttetProgram(),
        Rule038FullforteAvsluttedeProgramSituasjon(),
        Rule039FullforteAvsluttedeProgramInntektkilde()


    )

    override fun createStats(kostraRecordList: List<KostraRecord>): List<StatsReportEntry> {
        val sumStonad = kostraRecordList.sumOf { it.fieldAsIntOrDefault(KVP_STONAD_COL_NAME) }

        val ageList = kostraRecordList
            .filter { it.hasFnr() && it.ageInYears(arguments) > -1 }
            .map { it.ageInYears(arguments) }

        val stonadstidList = kostraRecordList
            .map { kostraRecord ->
                (1..12)
                    .map {
                        "STMND_$it"
                    }.count { fieldName ->
                        kostraRecord.fieldDefinition(fieldName).codeExists(kostraRecord[fieldName])
                    }
            }

        val stonadList = kostraRecordList
            .map { it.fieldAsIntOrDefault(KVP_STONAD_COL_NAME) }

        return listOf(
            StatsReportEntry(
                content = "Sum",
                codeList = listOf(
                    Code(KVP_STONAD_COL_NAME, "Stønad"),
                ),
                statsEntryList = listOf(
                    StatsEntry(KVP_STONAD_COL_NAME, "$sumStonad")
                )
            ),
            StatsReportEntry(
                content = "Kvalifiseringsdeltakere",
                codeList = listOf(
                    Code("I_ALT", "I alt"),
                    Code("0_19", "19 år eller yngre"),
                    Code("20_24", "20 - 24 år"),
                    Code("25_29", "25 - 29 år"),
                    Code("30_39", "30 - 39 år"),
                    Code("40_49", "40 - 49 år"),
                    Code("50_66", "50 - 66 år"),
                    Code("67_999", "67 år eller eldre"),
                    Code("UGYLDIG_FNR", "Alder ikke mulig å beregne")
                ),
                statsEntryList = listOf(
                    StatsEntry("I_ALT", kostraRecordList.size.toString()),
                    StatsEntry("0_19", ageList.count { it in 0..19 }.toString()),
                    StatsEntry("20_24", ageList.count { it in 20..24 }.toString()),
                    StatsEntry("25_29", ageList.count { it in 25..29 }.toString()),
                    StatsEntry("30_39", ageList.count { it in 30..39 }.toString()),
                    StatsEntry("40_49", ageList.count { it in 40..49 }.toString()),
                    StatsEntry("50_66", ageList.count { it in 50..66 }.toString()),
                    StatsEntry("67_999", ageList.count { it in 67..999 }.toString()),
                    StatsEntry("UGYLDIG_FNR", kostraRecordList.count { !it.hasFnr() }.toString()),
                )
            ),
            StatsReportEntry(
                content = "Stønadstid",
                codeList = listOf(
                    Code("1", "1 måned"),
                    Code("2_3", "2 - 3 måneder"),
                    Code("4_6", "4 - 6 måneder"),
                    Code("7_9", "7 - 9 måneder"),
                    Code("10_11", "10 - 11 måneder"),
                    Code("12", "12 måneder"),
                    Code("UOPPGITT", "Uoppgitt")
                ),
                statsEntryList = listOf(
                    StatsEntry("1", stonadstidList.count { it == 1 }.toString()),
                    StatsEntry("2_3", stonadstidList.count { it in 2..3 }.toString()),
                    StatsEntry("4_6", stonadstidList.count { it in 4..6 }.toString()),
                    StatsEntry("7_9", stonadstidList.count { it in 7..9 }.toString()),
                    StatsEntry("10_11", stonadstidList.count { it in 10..11 }.toString()),
                    StatsEntry("12", stonadstidList.count { it == 12 }.toString()),
                    StatsEntry("UOPPGITT", stonadstidList.count { it == 0 }.toString()),
                )
            ),
            StatsReportEntry(
                content = "Stønad",
                codeList = listOf(
                    Code("1_7", "1 - 7999"),
                    Code("8_49", "8000 - 49999"),
                    Code("50_99", "50000 - 99999"),
                    Code("100_149", "100000 - 149999"),
                    Code("150_9999", "150000 og over"),
                    Code("0", "Uoppgitt")
                ),
                statsEntryList = listOf(
                    StatsEntry("1_7", stonadList.count { it in 1..7_999 }.toString()),
                    StatsEntry("8_49", stonadList.count { it in 8_000..49_999 }.toString()),
                    StatsEntry("50_99", stonadList.count { it in 50_000..99_999 }.toString()),
                    StatsEntry("100_149", stonadList.count { it in 100_000..149_000 }.toString()),
                    StatsEntry("150_9999", stonadList.count { it in 150_000..9_999_999 }.toString()),
                    StatsEntry("0", stonadList.count { it == 0 }.toString()),
                )
            )
        )
    }
}
