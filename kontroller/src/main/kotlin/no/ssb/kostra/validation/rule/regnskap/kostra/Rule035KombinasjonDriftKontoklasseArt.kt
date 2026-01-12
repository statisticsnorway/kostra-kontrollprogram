package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap

class Rule035KombinasjonDriftKontoklasseArt : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 035 : Kombinasjon i driftsregnskapet, kontoklasse og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        kostraRecord.isBevilgningDriftRegnskap()
                && kostraRecord.fieldAsIntOrDefault(FIELD_BELOP) != 0
                && kostraRecord[FIELD_ART] in artList
                && kostraRecord[FIELD_FUNKSJON] !in funksjonList
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Feilmelding: (${kostraRecord[FIELD_ART]}) er kun gyldig i driftsregnskapet mot funksjonene 281 og 325. " +
                    "Art 520 Utlån – kun gyldig i drift for sosiale utlån, (funksjon 281) og næringsutlån (funksjon 325) som finansieres av driftsinntekter. " +
                    "Art 920 Mottatte avdrag på utlån – kun gyldig i drift for mottatte avdrag på sosiale utlån (funksjon 281) og næringsutlån (funksjon 325) som har blitt finansiert av driftsinntekter. " +
                    "Fant art (${kostraRecord[FIELD_ART]}), funksjon (${kostraRecord[FIELD_FUNKSJON].trim()}).",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }

    companion object {
        val artList = listOf("520", "920")
        val funksjonList = listOf("281 ", "325 ")
    }
}

