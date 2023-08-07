package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.VKLO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule024BTilknytningTilTrygdesystemetOgArbeidssituasjon : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K024B_TRYGDESYSTEMET_ARBEIDSSITUASJON.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it[VKLO_COL_NAME] == "3" && it[TRYGDESIT_COL_NAME] == "11"
        }.filter {
            it[ARBSIT_COL_NAME] == "08"
        }.map {
            val trygdesituasjon =
                fieldDefinitions.byColumnName(TRYGDESIT_COL_NAME).codeList.first { item ->
                    item.code == it[TRYGDESIT_COL_NAME]
                }

            val arbeidssituasjon =
                fieldDefinitions.byColumnName(ARBSIT_COL_NAME).codeList.first { item ->
                    item.code == it[ARBSIT_COL_NAME]
                }

            createValidationReportEntry(
                "Mottakeren mottar trygd ($trygdesituasjon), " +
                        "men det er oppgitt ugyldig kode ($arbeidssituasjon) på arbeidssituasjon."
            )
        }.ifEmpty { null }
}
