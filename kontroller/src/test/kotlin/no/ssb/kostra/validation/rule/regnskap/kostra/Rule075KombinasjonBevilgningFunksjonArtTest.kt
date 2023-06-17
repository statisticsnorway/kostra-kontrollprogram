package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule075KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule075KombinasjonBevilgningFunksjonArt()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row("0A", "800 ", "010", false),
            row("0A", "800 ", "870", false),
            row("0A", "800 ", "874", false),
            row("0A", "800 ", "875", false),
            row("0A", "800 ", "877", false),
            row("0A", "100 ", "870", true),
            row("0A", "100 ", "874", true),
            row("0A", "100 ", "875", true),
            row("0A", "100 ", "877", false),
            row("0C", "800 ", "010", false),
            row("0C", "800 ", "870", false),
            row("0C", "800 ", "877", false),
            row("0C", "100 ", "870", true),
            row("0C", "100 ", "874", true),
            row("0C", "100 ", "875", true),
            row("0C", "100 ", "877", false),
            row("0I", "800 ", "010", false),
            row("0I", "800 ", "870", false),
            row("0I", "800 ", "874", false),
            row("0I", "800 ", "875", false),
            row("0I", "800 ", "877", false),
            row("0I", "100 ", "870", true),
            row("0I", "100 ", "874", true),
            row("0I", "100 ", "875", true),
            row("0I", "100 ", "877", false),
            row("0K", "800 ", "010", false),
            row("0K", "800 ", "870", false),
            row("0K", "800 ", "874", false),
            row("0K", "800 ", "875", false),
            row("0K", "800 ", "877", false),
            row("0K", "100 ", "870", true),
            row("0K", "100 ", "874", true),
            row("0K", "100 ", "875", true),
            row("0K", "100 ", "877", false),
            row("0M", "800 ", "010", false),
            row("0M", "800 ", "870", false),
            row("0M", "800 ", "874", false),
            row("0M", "800 ", "875", false),
            row("0M", "800 ", "877", false),
            row("0M", "100 ", "870", true),
            row("0M", "100 ", "874", true),
            row("0M", "100 ", "875", true),
            row("0M", "100 ", "877", false),
            row("0P", "800 ", "010", false),
            row("0P", "800 ", "870", false),
            row("0P", "800 ", "874", false),
            row("0P", "100 ", "870", true),
            row("0P", "100 ", "874", true),
            row("0P", "100 ", "875", true),
            row("0P", "100 ", "877", false),

            ) { skjema, funksjon, art, expectedResult ->
            When("For $skjema, $funksjon, $art -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_FUNKSJON to funksjon,
                            RegnskapConstants.FIELD_ART to art,
                        )
                    )
                )

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.ERROR)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Artene 870, 871, 872, 873, 875 og 876 er kun tillat brukt i kombinasjon med funksjon 800."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})