package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoArgsTest
import kotlin.text.trim

class Rule035KombinasjonDriftKontoklasseArtTest : BehaviorSpec({
    include(
        validationRuleNoArgsTest(
            sut = Rule035KombinasjonDriftKontoklasseArt(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "isBevilgningDriftRegnskap = false",
                kostraRecordsInTest(0, 520, 100, 1),
            ),
            ForAllRowItem(
                "belop = 0",
                kostraRecordsInTest(1, 100, 520, 0),
            ),
            ForAllRowItem(
                "art !in (520, 920)",
                kostraRecordsInTest(1, 281, 100, 1),
            ),
            ForAllRowItem(
                "art = 520, funksjon = 281",
                kostraRecordsInTest(1, 281, 520, 1),
            ),
            ForAllRowItem(
                "art = 520, funksjon = 325",
                kostraRecordsInTest(1, 325, 520, 1),
            ),
            ForAllRowItem(
                "art = 920, funksjon = 281",
                kostraRecordsInTest(1, 281, 920, 1),
            ),
            ForAllRowItem(
                "art = 920, funksjon = 325",
                kostraRecordsInTest(1, 325, 920, 1),
            ),
            ForAllRowItem(
                "all conditions match",
                kostraRecordsInTest(1, 100, 520, 1),
                expectedErrorMessage = "Feilmelding: (520) er kun gyldig i driftsregnskapet mot funksjonene 281 og 325. " +
                        "Art 520 Utlån – kun gyldig i drift for sosiale utlån, (funksjon 281) og næringsutlån (funksjon 325) som finansieres av driftsinntekter. " +
                        "Art 920 Mottatte avdrag på utlån – kun gyldig i drift for mottatte avdrag på sosiale utlån (funksjon 281) og næringsutlån (funksjon 325) som har blitt finansiert av driftsinntekter. " +
                        "Fant art (520), funksjon (100).",
            ),
            ForAllRowItem(
                "all conditions match",
                kostraRecordsInTest(1, 100, 920, 1),
                expectedErrorMessage = "Feilmelding: (920) er kun gyldig i driftsregnskapet mot funksjonene 281 og 325. " +
                        "Art 520 Utlån – kun gyldig i drift for sosiale utlån, (funksjon 281) og næringsutlån (funksjon 325) som finansieres av driftsinntekter. " +
                        "Art 920 Mottatte avdrag på utlån – kun gyldig i drift for mottatte avdrag på sosiale utlån (funksjon 281) og næringsutlån (funksjon 325) som har blitt finansiert av driftsinntekter. " +
                        "Fant art (920), funksjon (100).",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            kontoklasse: Int,
            funksjon: Int,
            art: Int,
            belop: Int
        ) = mapOf(
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to "$kontoklasse",
            FIELD_FUNKSJON to "$funksjon ",
            FIELD_ART to "$art",
            FIELD_BELOP to "$belop"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()
    }
}