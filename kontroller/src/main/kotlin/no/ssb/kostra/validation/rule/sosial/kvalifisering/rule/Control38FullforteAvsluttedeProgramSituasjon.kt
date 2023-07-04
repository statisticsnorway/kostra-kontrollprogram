package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_AAP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ANNET_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ARBLONNSTILS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ARBMARK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_OK_AVKLAR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ORDINAERTARB_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_SKOLE_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_UFORE_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_UKJENT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_UTEN_OK_AVKLAR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.FULLFORT_PROGRAM
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control38FullforteAvsluttedeProgramSituasjon : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.FULLFORTE_AVSLUTTEDE_PROGRAM_SITUASJON_38.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) = context[STATUS_COL_NAME]
        .takeIf { statusCode -> statusCode == FULLFORT_PROGRAM }
        ?.takeIf {
            qualifyingFieldNames
                .mapNotNull { fieldName -> context.fieldAs<String?>(fieldName)?.let { fieldName to it } }
                .none { (fieldName, fieldValue) ->
                    fieldDefinitions.byColumnName(fieldName).codeExists(fieldValue)
                }
        }?.let {
            createSingleReportEntryList(
                "Feltet 'Ved fullført program eller program avsluttet etter avtale (gjelder ikke " +
                        "flytting) – hva var deltakerens situasjon umiddelbart etter avslutningen'? " +
                        "Må fylles ut dersom det er krysset av for svaralternativ 3 = Deltakeren har fullført " +
                        "program eller avsluttet program etter avtale (gjelder ikke flytting) under feltet for " +
                        "'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12.${arguments.aargang}'?"
            )
        }

    companion object {
        internal val qualifyingFieldNames = setOf(
            AVSL_ORDINAERTARB_COL_NAME,
            AVSL_ARBLONNSTILS_COL_NAME,
            AVSL_ARBMARK_COL_NAME,
            AVSL_SKOLE_COL_NAME,
            AVSL_UFORE_COL_NAME,
            AVSL_AAP_COL_NAME,
            AVSL_OK_AVKLAR_COL_NAME,
            AVSL_UTEN_OK_AVKLAR_COL_NAME,
            AVSL_ANNET_COL_NAME,
            AVSL_UKJENT_COL_NAME
        )
    }
}