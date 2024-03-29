package no.ssb.kostra.area.famvern.famvern55

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.famvern.famvern55.Famvern55Utils.validateMatrix
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.validation.rule.famvern.famvern55.Familievern55TestUtils

class Famvern55UtilsTest : BehaviorSpec({
    Given("getCombinedList") {
        val columnSize = 4

        val fieldList = listOf(
            Familievern55ColumnNames.MEKLING_SEP_1_COL_NAME,
            Familievern55ColumnNames.MEKLING_SEP_2_COL_NAME,
            Familievern55ColumnNames.MEKLING_SEP_3_COL_NAME,
            Familievern55ColumnNames.MEKLING_SEP_TOT_COL_NAME,
            Familievern55ColumnNames.MEKLING_SAM_1_COL_NAME,
            Familievern55ColumnNames.MEKLING_SAM_2_COL_NAME,
            Familievern55ColumnNames.MEKLING_SAM_3_COL_NAME,
            Familievern55ColumnNames.MEKLING_SAM_TOT_COL_NAME,
            Familievern55ColumnNames.MEKLING_SAK_1_COL_NAME,
            Familievern55ColumnNames.MEKLING_SAK_2_COL_NAME,
            Familievern55ColumnNames.MEKLING_SAK_3_COL_NAME,
            Familievern55ColumnNames.MEKLING_SAK_TOT_COL_NAME,
            Familievern55ColumnNames.MEKLING_TILB_1_COL_NAME,
            Familievern55ColumnNames.MEKLING_TILB_2_COL_NAME,
            Familievern55ColumnNames.MEKLING_TILB_3_COL_NAME,
            Familievern55ColumnNames.MEKLING_TILB_TOT_COL_NAME,
            Familievern55ColumnNames.MEKLING_FLY_1_COL_NAME,
            Familievern55ColumnNames.MEKLING_FLY_2_COL_NAME,
            Familievern55ColumnNames.MEKLING_FLY_3_COL_NAME,
            Familievern55ColumnNames.MEKLING_FLY_TOT_COL_NAME,
            Familievern55ColumnNames.MEKLING_TOT_1_COL_NAME,
            Familievern55ColumnNames.MEKLING_TOT_2_COL_NAME,
            Familievern55ColumnNames.MEKLING_TOT_3_COL_NAME,
            Familievern55ColumnNames.MEKLING_TOT_ALLE_COL_NAME,
        )

        val kostraRecordList = Familievern55TestUtils.familievernRecordInTest(
            mapOf(
                Familievern55ColumnNames.MEKLING_SEP_1_COL_NAME to "1",
                Familievern55ColumnNames.MEKLING_SEP_TOT_COL_NAME to "1",
                Familievern55ColumnNames.MEKLING_TOT_1_COL_NAME to "1",
                Familievern55ColumnNames.MEKLING_TOT_ALLE_COL_NAME to "1",
            )
        ).asList()


        When("getting rows and columns of valid parameters") {

            Then("a 2D-list is made") {
                val combined = validateMatrix(
                    kostraRecordList,
                    fieldList,
                    columnSize
                )

                combined.size shouldBe 0
            }
        }

        When("getting rows and columns of invalid parameters") {
            val kostraRecordListWithInvalidValues = Familievern55TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern55ColumnNames.MEKLING_SEP_1_COL_NAME to "1",
                    Familievern55ColumnNames.MEKLING_SEP_TOT_COL_NAME to "1",
                    Familievern55ColumnNames.MEKLING_TOT_1_COL_NAME to "1",
                    Familievern55ColumnNames.MEKLING_TOT_ALLE_COL_NAME to "2",
                )
            ).asList()

            Then("a 2D-list is made") {
                val combined = validateMatrix(
                    kostraRecordListWithInvalidValues,
                    fieldList,
                    columnSize
                )

                combined.size shouldBe 2
            }
        }

        When("invalid parameters") {
            val invalidFieldList = fieldList.dropLast(1)

            Then("get an IndexOutOfBoundsException") {
                shouldThrow<IllegalArgumentException> {
                    validateMatrix(
                        kostraRecordList,
                        invalidFieldList,
                        columnSize
                    )
                }
            }
        }
    }
})