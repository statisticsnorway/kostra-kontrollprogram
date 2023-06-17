package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control39FullforteAvsluttedeProgramInntektkilde : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.FULLFORTE_AVSLUTTEDE_PROGRAM_INNTEKTKILDE_39.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = KvalifiseringFieldDefinitions.fieldDefinitions.findByColumnName(STATUS_COL_NAME)
        val value = context.getFieldAsTrimmedString(AVSL_DATO_COL_NAME)

        return createSingleReportEntryList(
            "Feltet 'Hva var deltakerens <b>viktigste</b> inntektskilde umiddelbart etter avslutningen? " +
                    "Må fylles ut dersom det er krysset av for svaralternativ 3 = Deltakeren har fullført program " +
                    "eller avsluttet program etter avtale (gjelder ikke flytting) under feltet for 'Hva er status " +
                    "for deltakelsen i kvalifiseringsprogrammet per 31.12.${arguments.aargang}'?"
        )
    }
}