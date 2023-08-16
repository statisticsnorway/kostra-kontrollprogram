package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames

class TempSplit : BehaviorSpec({
    Given("context") {
        val rowSize = 6
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

        fun getRows(fieldList: List<String>, columnSize: Int) = fieldList.chunked(columnSize)

        fun getColumns(fieldList: List<String>, rowSize: Int) = fieldList
            .withIndex()
            .groupBy {
                it.index % (fieldList.size / rowSize)
            }
            .mapValues {
                it.value.map { indexedValue -> indexedValue.value }
            }
            .entries
            .map {
                it.value
            }
            .toList()

        fun getCombinedList(fieldList: List<String>, columnSize: Int, rowSize: Int) =
            getRows(fieldList, columnSize) + getColumns(fieldList, rowSize)


        When("getting rows"){
            val rows = getRows(fieldList, columnSize)

            Then("a 2D-list is made"){
//                println(rows)
                rows.size shouldBe 6
            }
        }

        When("getting columns"){
            val columns = getColumns(fieldList, rowSize)

            Then("a 2D-list is made"){
//                println(columns)
                columns.size shouldBe 4
            }
        }

        When("getting rows and columns"){
            val combined = getCombinedList(fieldList, columnSize, rowSize)
            Then("a 2D-list is made"){
                println("combined -> $combined")
                combined.size shouldBe columnSize + rowSize
            }
        }

        When("Exparimenting"){
            fun fromListTo2dArray(fieldList: List<String>, columnSize: Int): Array<Array<String>> =
                fieldList.chunked(columnSize).map { it.toTypedArray() }.toTypedArray()

            fun switchOrder2dArray(arrayOfArray: Array<Array<String>>): Array<Array<String>> {
                val rowSize = arrayOfArray[0].size
                val colSize = arrayOfArray.size
                val result = Array(colSize) {Array(rowSize) {""} }

                for (i:Int in 0 .. arrayOfArray.size){
                    for (j:Int in 0 .. arrayOfArray[i].size){
                        result[j][i] = arrayOfArray[i][j]
                    }
                }

                return result
            }

            Then("W"){
//                println(fieldList)
                val step1 = fromListTo2dArray(fieldList, 4)
                println(step1)
//                val step2 = switchOrder2dArray(step1)
//                println(step2)
                1 shouldBe 1
            }
        }
    }
})