package no.ssb.kostra.area.sosial.kvalifisering

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.program.extension.toKostraRecord

class KvalifiseringFieldDefinitionsTest : BehaviorSpec({

    Given("KvalifiseringFieldDefinitions") {

        When("reading record strings") {
            val kostraRecords = javaClass.getResource("/testfiler/11CF/kvalifisering_minimal.dat")!!.readText()
                .lines()
                .filter { it.length == 136 }
                .mapIndexed { index, line ->
                    line.toKostraRecord(index + 1, KvalifiseringFieldDefinitions.fieldDefinitions)
                }

            Then("records should be as expected") {
                kostraRecords.size shouldBe 2

                assertSoftly(kostraRecords.first()) {
                    it[PERSON_JOURNALNR_COL_NAME] shouldStartWith "JOURNNR"
                    it[PERSON_FODSELSNR_COL_NAME] shouldBe "02020212345"
                }
            }
        }
    }
})
