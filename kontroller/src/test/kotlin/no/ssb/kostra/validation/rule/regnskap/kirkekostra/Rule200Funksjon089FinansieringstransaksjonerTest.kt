package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule200Funksjon089FinansieringstransaksjonerTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule200Funksjon089Finansieringstransaksjoner(),
            forAllRows = listOf(
                ForAllRowItem(
                    "isBevilgningRegnskap = true, funksjon = 089, art != 500..580",
                    kostraRecordsInTest("0F", "3", "089 ", "499"),
                    expectedErrorMessage = "Korrigér i fila slik at art (499) " +
                            "er gyldig mot funksjon 089. Gyldige arter er 500-580, 830 og 900-980."
                ),
                ForAllRowItem(
                    "isBevilgningRegnskap = false, funksjon = 089, art != 500..580",
                    kostraRecordsInTest("0X", "3", "089 ", "499")
                ),
                ForAllRowItem(
                    "isBevilgningRegnskap = true, funksjon != 089, art != 500..580",
                    kostraRecordsInTest("0F", "3", "088 ", "499")
                ),
                ForAllRowItem(
                    "isBevilgningRegnskap = true, funksjon = 089, art = 500",
                    kostraRecordsInTest("0F", "3", "089 ", "500")
                ),
                ForAllRowItem(
                    "isBevilgningRegnskap = true, funksjon = 089, art = 830",
                    kostraRecordsInTest("0F", "3", "089 ", "830")
                ),
                ForAllRowItem(
                    "isBevilgningRegnskap = true, funksjon = 089, art = 900",
                    kostraRecordsInTest("0F", "3", "089 ", "900")
                )
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        fun kostraRecordsInTest(
            skjema: String,
            kontoklasse: String,
            funksjon: String,
            art: String
        ) = mapOf(
            FIELD_SKJEMA to skjema,
            FIELD_KONTOKLASSE to kontoklasse,
            FIELD_FUNKSJON to funksjon,
            FIELD_ART to art,
        ).toKostraRecord().asList()
    }
}