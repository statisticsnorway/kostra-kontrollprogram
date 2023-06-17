package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId
import no.ssb.kostra.validation.util.SsnValidationUtils.isValidSocialSecurityIdOrDnr

class Fodselsnummer05 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.FODSELSNUMMER_05.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? =
        if (!isValidSocialSecurityIdOrDnr(context.getFieldAsTrimmedString(PERSON_FODSELSNR_COL_NAME))) {
            createSingleReportEntryList(
                "Det er ikke oppgitt fødselsnummer/d-nummer på deltakeren eller fødselsnummeret/d-nummeret " +
                        "inneholder feil. Med mindre det er snakk om en utenlandsk statsborger som ikke er tildelt " +
                        "norsk personnummer eller d-nummer, skal feltet inneholde deltakeren " +
                        "fødselsnummer/d-nummer (11 siffer)."
            )
        } else null
}