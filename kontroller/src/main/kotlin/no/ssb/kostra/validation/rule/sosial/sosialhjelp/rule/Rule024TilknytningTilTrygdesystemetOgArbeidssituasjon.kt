package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpConstants.UNKNOWN
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule024TilknytningTilTrygdesystemetOgArbeidssituasjon : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K024_TRYGDESYSTEMET_ARBEIDSSITUASJON.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it[TRYGDESIT_COL_NAME] in listOf("04", "07")
        }.filterNot {
            it[ARBSIT_COL_NAME] in listOf("02", "04", "07")
        }.map {
            val trygdesituasjon = fieldDefinitions.byColumnName(TRYGDESIT_COL_NAME).codeList.first { item ->
                item.code == it[TRYGDESIT_COL_NAME]
            }

            val arbeidssituasjon = fieldDefinitions.byColumnName(ARBSIT_COL_NAME).codeList.firstOrNull { item ->
                item.code == it[ARBSIT_COL_NAME]
            } ?: Code(it[ARBSIT_COL_NAME], UNKNOWN)

            createValidationReportEntry(
                "Mottakeren mottar trygd ($trygdesituasjon), " +
                        "men det er oppgitt ugyldig kode ($arbeidssituasjon) på arbeidssituasjon."
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
                individId = it[SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME],
            )
        }.ifEmpty { null }
}
