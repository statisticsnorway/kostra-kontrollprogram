package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap

class Rule050KombinasjonInvesteringKontoklasseArt(
    private val invalidInvesteringArtList: List<String>
) : AbstractRule<List<KostraRecord>>(
    "Kontroll 050 : Ugyldig kombinasjon i investeringsregnskapet, kontoklasse og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filter { kostraRecord ->
        kostraRecord.isBevilgningInvesteringRegnskap()
                && kostraRecord[FIELD_ART] in invalidInvesteringArtList
                && kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) != 0
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Korrigér ugyldig art '${kostraRecord[FIELD_ART]}' i investeringsregnskapet til en gyldig " +
                    "art i investeringsregnskapet eller overfør posteringen til driftsregnskapet.",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}