package no.ssb.kostra.validation.rule.regnskap

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest


class Rule015DuplicatesTest : BehaviorSpec({
    Given("valid context") {
        val sut = Rule015Duplicates(
            listOf(FIELD_KONTOKLASSE, FIELD_FUNKSJON, FIELD_ART)
                    to listOf(TITLE_KONTOKLASSE, TITLE_FUNKSJON, TITLE_ART)
        )

        forAll(
            row(
                "No duplicates",
                listOf(
                    mapOf(
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "710",
                        FIELD_BELOP to "1"
                    ).toKostraRecord(1, fieldDefinitions),
                    mapOf(
                        FIELD_KONTOKLASSE to "0",
                        FIELD_FUNKSJON to "200 ",
                        FIELD_ART to "610",
                        FIELD_BELOP to "2"
                    ).toKostraRecord(2, fieldDefinitions)
                )
            )
        ) { description, kostraRecordList ->
            When(description) {
                Then("validation should pass with no errors") {
                    sut.validate(kostraRecordList, argumentsInTest).shouldBeNull()
                }
            }
        }

        forAll(
            row(
                "Has duplicates",
                listOf(
                    mapOf(
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "710",
                        FIELD_BELOP to "1"
                    ).toKostraRecord(1, fieldDefinitions),
                    mapOf(
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "710",
                        FIELD_BELOP to "2"
                    ).toKostraRecord(2, fieldDefinitions),
                    mapOf(
                        FIELD_KONTOKLASSE to "0",
                        FIELD_FUNKSJON to "200 ",
                        FIELD_ART to "610",
                        FIELD_BELOP to "3"
                    ).toKostraRecord(3, fieldDefinitions),
                    mapOf(
                        FIELD_KONTOKLASSE to "0",
                        FIELD_FUNKSJON to "200 ",
                        FIELD_ART to "610",
                        FIELD_BELOP to "4"
                    ).toKostraRecord(4, fieldDefinitions)
                )
            )
        ) { description, kostraRecordList ->
            When(description) {
                val validationReportEntries = sut.validate(kostraRecordList, argumentsInTest)

                Then("validation should result in errors") {
                    validationReportEntries.shouldNotBeNull()
                }
            }
        }
    }
})