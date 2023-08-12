package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoArgsTest

class Rule200Funksjon089FinansieringstransaksjonerTest : BehaviorSpec({
    include(
        validationRuleNoArgsTest(
            sut = Rule200Funksjon089Finansieringstransaksjoner(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "isBevilgningRegnskap = true, funksjon = 089, art != 500..580",
                kostraRecordsInTest(skjema = "0F", funksjon = "089 ", art = "499"),
                expectedErrorMessage = "Korrigér i fila slik at art (499) " +
                        "er gyldig mot funksjon 089. Gyldige arter er 500-580, 830 og 900-980.",
            ),
            ForAllRowItem(
                "isBevilgningRegnskap = false, funksjon = 089, art != 500..580",
                kostraRecordsInTest("0X", "089 ", "499"),
            ),
            ForAllRowItem(
                "isBevilgningRegnskap = true, funksjon != 089, art match",
                kostraRecordsInTest("0F", "088 ", "499"),
            ),
            ForAllRowItem(
                "isBevilgningRegnskap = true, funksjon = 089, art = 500",
                kostraRecordsInTest("0F", "089 ", "500"),
            ),
            ForAllRowItem(
                "isBevilgningRegnskap = true, funksjon = 089, art = 580",
                kostraRecordsInTest("0F", "089 ", "580"),
            ),
            ForAllRowItem(
                "isBevilgningRegnskap = true, funksjon = 089, art = 830",
                kostraRecordsInTest("0F", "089 ", "830"),
            ),
            ForAllRowItem(
                "isBevilgningRegnskap = true, funksjon = 089, art = 900",
                kostraRecordsInTest("0F", "089 ", "900"),
            ),
            ForAllRowItem(
                "isBevilgningRegnskap = true, funksjon = 089, art = 980",
                kostraRecordsInTest("0F", "089 ", "980"),
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            skjema: String,
            funksjon: String,
            art: String
        ): List<KostraRecord> = mapOf(
            FIELD_SKJEMA to skjema,
            FIELD_KONTOKLASSE to "3",
            FIELD_FUNKSJON to funksjon,
            FIELD_ART to art
        ).toKostraRecord(1, fieldDefinitions).asList()
    }
}