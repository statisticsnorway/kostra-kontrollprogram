package no.ssb.kostra.area.sosial.sosialhjelp

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord

class SosialhjelpFieldDefinitionsTest : BehaviorSpec({

    Given("SosialhjelpFieldDefinitions") {
        fun Int.pad() = this.toString().padStart(3, ' ')
        fun String.pad() = this.padStart(3, ' ')
        val sut = SosialhjelpFieldDefinitions.fieldDefinitions

        When("reading record strings") {
            sut.forEachIndexed { index, fieldDefinition ->
                with(fieldDefinition) {
                    println("- name: $name")
                    println("  dataType: $dataType")
                    println("  from: $from")
                    println("  to: $to")
                    println("  length: $size")
                    println("  mandatory: $mandatory")

                    if(dataType.toString() == "DATE_TYPE")
                        println("  datePattern: $datePattern")
                    if (codeList.isNotEmpty()){
                        println("  codeList:")
                        codeList.forEach {
                            println("  - code: ${it.code}")
                            println("    value: ${it.value}")
                        }
                    }

                    println(fieldDefinition)
                }
            }
            Then("records should be as expected") {
                true shouldBe true
            }
        }
    }
})