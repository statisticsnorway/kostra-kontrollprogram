package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.plus

class Rule020KombinasjonDriftKontoklasseFunksjonTest : BehaviorSpec({
    Given("context") {
        val sut = Rule020KombinasjonDriftKontoklasseFunksjon(
            listOf("100 ")
        )
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 1, name = RegnskapConstants.FIELD_KONTOKLASSE),
            FieldDefinition(from = 3, to = 7, name = RegnskapConstants.FIELD_FUNKSJON)
        ).associateBy { it.name }

        forAll(
            row(
                """'${RegnskapConstants.TITLE_DERIVED_KONTOKLASSE}' is '${RegnskapConstants.ACCOUNT_TYPE_DRIFT}' 
                    |and ${RegnskapConstants.TITLE_FUNKSJON} is valid""".trimMargin(),
                listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_KONTOKLASSE to RegnskapConstants.ACCOUNT_TYPE_DRIFT,
                            RegnskapConstants.FIELD_FUNKSJON to "100 "
                        )
                    )
                        .plus(RegnskapConstants.DERIVED_KONTOKLASSE to RegnskapConstants.ACCOUNT_TYPE_DRIFT)
                        .plus(RegnskapConstants.DERIVED_ACCOUNTING_TYPE to RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING)
                )
            )
        ) { description, kostraRecordList ->
            When(description) {
                Then("validation should pass with no errors") {
                    sut.validate(kostraRecordList).shouldBeNull()
                }
            }
        }

        forAll(
            row(
                """'${RegnskapConstants.TITLE_DERIVED_KONTOKLASSE}' is '${RegnskapConstants.ACCOUNT_TYPE_INVESTERING}' 
                    |and ${RegnskapConstants.TITLE_FUNKSJON} is invalid""".trimMargin(),
                listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_KONTOKLASSE to RegnskapConstants.ACCOUNT_TYPE_INVESTERING,
                            RegnskapConstants.FIELD_FUNKSJON to "XXX "
                        )
                    )
                        .plus(RegnskapConstants.DERIVED_KONTOKLASSE to RegnskapConstants.ACCOUNT_TYPE_INVESTERING)
                        .plus(RegnskapConstants.DERIVED_ACCOUNTING_TYPE to RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING)
                )
            )
        ) { description, kostraRecordList ->
            When(description) {
                Then("validation should pass with no errors") {
                    sut.validate(kostraRecordList).shouldBeNull()
                }
            }
        }


        forAll(
            row(
                """'${RegnskapConstants.TITLE_DERIVED_KONTOKLASSE}' is '${RegnskapConstants.ACCOUNT_TYPE_DRIFT}' 
                    |and ${RegnskapConstants.TITLE_FUNKSJON} is invalid""".trimMargin(),

                listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_KONTOKLASSE to RegnskapConstants.ACCOUNT_TYPE_DRIFT,
                            RegnskapConstants.FIELD_FUNKSJON to "XXX "
                        )
                    )
                        .plus(RegnskapConstants.DERIVED_KONTOKLASSE to RegnskapConstants.ACCOUNT_TYPE_DRIFT)
                        .plus(RegnskapConstants.DERIVED_ACCOUNTING_TYPE to RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING)
                )
            )
        ) { description, kostraRecordList ->
            When(description) {
                Then("validation should result in errors") {
                    val result = sut.validate(kostraRecordList)
                    println(result)
                    result.shouldNotBeNull()
                }
            }
        }
    }
})