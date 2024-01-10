package no.ssb.kostra.area.sosial.sosialhjelp

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class SosialhjelpFieldDefinitionsTest : BehaviorSpec({

    Given("SosialhjelpFieldDefinitions") {
        fun Int.pad() = this.toString().padStart(3, ' ')
        fun String.pad() = this.padStart(3, ' ')
        val sut = SosialhjelpFieldDefinitions.fieldDefinitions

        When("reading record strings") {
            sut.forEach { fieldDefinition ->
                with(fieldDefinition) {
                    println("  - name: $name")
                    println("    dataType: $dataType")
                    println("    from: $from")
                    println("    size: $size")
                    if (mandatory)
                        println("    mandatory: $mandatory")

                    if (dataType.toString() == "DATE_TYPE")
                        println("    datePattern: $datePattern")

                    if (codeList.isNotEmpty()) {
                        println("    codeList:")
                        codeList.forEach {
                            println("    - code: ${it.code}")
                            println("      value: ${it.value}")
                        }
                    }
                }
            }
            Then("records should be as expected") {
                sut.size shouldBe 78
            }
        }
    }
})