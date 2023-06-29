package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap

class Rule035KombinasjonDriftKontoklasseArt(
    private val illogicalDriftArtList: List<String>
) : AbstractRule<List<KostraRecord>>(
    "Kontroll 035 : Kombinasjon i driftsregnskapet, kontoklasse og art",
    Severity.INFO
) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        kostraRecord.isBevilgningDriftRegnskap()
                && kostraRecord.getFieldAsString(FIELD_ART) in illogicalDriftArtList
                && kostraRecord.getFieldAsIntegerOrDefault(FIELD_BELOP) != 0
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Kun advarsel, hindrer ikke innsending: (${
                kostraRecord.getFieldAsString(
                    FIELD_ART
                )
            }) regnes å være ulogisk i driftsregnskapet, med mindre posteringen gjelder sosiale utlån og næringsutlån eller mottatte avdrag på sosiale utlån og næringsutlån, som finansieres av driftsinntekter.",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}

