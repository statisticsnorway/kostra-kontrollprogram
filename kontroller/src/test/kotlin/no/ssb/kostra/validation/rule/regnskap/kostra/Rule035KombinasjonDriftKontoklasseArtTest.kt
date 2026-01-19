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
            expectedSeverity = Severity.WARNING,
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
                "art = 520, funksjon = 701",
                kostraRecordsInTest(1, 701, 520, 1),
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
                "art = 920, funksjon = 701",
                kostraRecordsInTest(1, 701, 920, 1),
            ),
            ForAllRowItem(
                "all conditions match",
                kostraRecordsInTest(1, 100, 520, 1),
                expectedErrorMessage = "Art (520) er kun gyldig i driftsregnskapet mot funksjonene 281,  325 og 701. " +
                        "Artene 520 Utlån og 920 Mottatte avdrag på utlån er kun gyldig i driftsregnskapet " +
                        "for funksjonene 281 Mottatte avdrag på sosiale utlån, 325 Næringsutlån og " +
                        "701 Tilrettelegging, støttefunksjoner og finansieringsbistand for næringslivet, " +
                        "som har blitt finansiert av driftsinntekter. " +
                        "Fant art (520), funksjon (100).",
            ),
            ForAllRowItem(
                "all conditions match",
                kostraRecordsInTest(1, 100, 920, 1),
                expectedErrorMessage = "Art (920) er kun gyldig i driftsregnskapet mot funksjonene 281,  325 og 701. " +
                        "Artene 520 Utlån og 920 Mottatte avdrag på utlån er kun gyldig i driftsregnskapet " +
                        "for funksjonene 281 Mottatte avdrag på sosiale utlån, 325 Næringsutlån og " +
                        "701 Tilrettelegging, støttefunksjoner og finansieringsbistand for næringslivet, " +
                        "som har blitt finansiert av driftsinntekter. " +
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