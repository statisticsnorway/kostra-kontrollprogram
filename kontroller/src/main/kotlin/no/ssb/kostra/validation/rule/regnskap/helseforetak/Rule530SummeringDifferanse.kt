package no.ssb.kostra.validation.rule.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isResultatRegnskap

class Rule530SummeringDifferanse : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 530 : Sum inntekter og kostnader = 0",
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isResultatRegnskap() }
        .takeIf { it.any() }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { differanse -> differanse in -100..100 }
        ?.let { differanse ->
            createSingleReportEntryList(
                messageText = "Sjekk at sum art 300 til og med art 899 skal være 0, her ($differanse). Differanse +/- 100' kroner godtas."
            )
        }
}