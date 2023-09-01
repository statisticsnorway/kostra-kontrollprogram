package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_SOSHJELP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_TYPE_SOSHJ_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.JA
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule021Ytelser : AbstractNoArgsRule<List<KostraRecord>>(
    KvalifiseringRuleId.YTELSER_21.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it[YTELSE_SOSHJELP_COL_NAME] == JA }
        .filter { fieldDefinition.codeIsMissing(it[YTELSE_TYPE_SOSHJ_COL_NAME]) }
        .map {
            createValidationReportEntry(
                "Feltet for 'Hadde deltakeren i løpet av de siste to månedene før " +
                        "registrert søknad ved NAV-kontoret en eller flere av følgende ytelser?' " +
                        "inneholder ugyldige verdier.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        private val fieldDefinition = fieldDefinitions.byColumnName(YTELSE_TYPE_SOSHJ_COL_NAME)
    }
}