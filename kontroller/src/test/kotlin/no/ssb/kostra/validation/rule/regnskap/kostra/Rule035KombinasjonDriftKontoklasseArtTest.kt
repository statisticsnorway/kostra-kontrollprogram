package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest

class Rule035KombinasjonDriftKontoklasseArtTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule035KombinasjonDriftKontoklasseArt(listOf("520")),
            forAllRows = listOf(
                ForAllRowItem(
                    "all conditions match",
                    kostraRecordsInTest(1, 520, 1),
                    expectedErrorMessage = "Kun advarsel, hindrer ikke innsending: (520) regnes å være ulogisk i " +
                            "driftsregnskapet, med mindre posteringen gjelder sosiale utlån og næringsutlån " +
                            "eller mottatte avdrag på sosiale utlån og næringsutlån, som finansieres av " +
                            "driftsinntekter.",
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = false",
                    kostraRecordsInTest(0, 520, 1),
                ),
                ForAllRowItem(
                    "art != 520",
                    kostraRecordsInTest(1, 521, 1),
                ),
                ForAllRowItem(
                    "belop = 0",
                    kostraRecordsInTest(1, 520, 0),
                )
            ),
            expectedSeverity = Severity.INFO,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            kontoklasse: Int,
            art: Int,
            belop: Int
        ) = mapOf(
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to "$kontoklasse",
            FIELD_ART to "$art",
            FIELD_BELOP to "$belop"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()
    }
}