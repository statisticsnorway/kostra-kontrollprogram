package no.ssb.kostra.area.sosial.kvalifisering


import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.report.StatsEntry
import no.ssb.kostra.validation.report.StatsEntryHeading
import no.ssb.kostra.validation.report.StatsReportEntry
import no.ssb.kostra.validation.rule.Rule000HasAttachment
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.sosial.extensions.varighetAsStatsEntries
import no.ssb.kostra.validation.rule.sosial.kvalifisering.extensions.deltakereByAlderAsStatsEntries
import no.ssb.kostra.validation.rule.sosial.kvalifisering.extensions.stonadAsStatsEntries
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

    override fun createStats(kostraRecordList: List<KostraRecord>): List<StatsReportEntry> =
        listOf(
            StatsReportEntry(
                heading = StatsEntryHeading("Stønad", "Sum"),
                entries = listOf(
                    StatsEntry("I alt", "${kostraRecordList.sumOf { it.fieldAsIntOrDefault(KVP_STONAD_COL_NAME) }}")
                )
            ),
            StatsReportEntry(
                heading = StatsEntryHeading("Alder", "Deltakere"),
                entries = listOf(StatsEntry("I alt", kostraRecordList.size.toString()))
                    .plus(kostraRecordList.deltakereByAlderAsStatsEntries(arguments))
            ),
            StatsReportEntry(
                heading = StatsEntryHeading("Stønadsvarighet", "Deltakere"),
                entries = kostraRecordList.varighetAsStatsEntries()
            ),
            StatsReportEntry(
                heading = StatsEntryHeading("Stønad", "Deltakere"),
                entries = kostraRecordList.stonadAsStatsEntries()
            )
        )
}
