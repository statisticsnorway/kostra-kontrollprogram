package no.ssb.kostra.area.famvern.famvern55

import no.ssb.kostra.program.KostraRecord

object Utils {
    data class CalculationItem(
        val sumItem: Pair<String, Int>,
        val itemList: List<Pair<String, Int>>,
        val lineNumbers: List<Int>
    )

    private fun getRows(fieldList: List<String>, columnSize: Int) = fieldList.chunked(columnSize)

    private fun getColumns(fieldList: List<String>, rowSize: Int) = fieldList
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

    fun validateMatrix(kostraRecordList: List<KostraRecord>, fieldList: List<String>, columnSize: Int) =
        if (fieldList.size % columnSize == 0) {
            val calculationList =
                if (fieldList.size / columnSize > 1)
                    getRows(fieldList, columnSize) + getColumns(fieldList, fieldList.size / columnSize)
                else
                    getRows(fieldList, columnSize)

            kostraRecordList.flatMap { kostraRecord ->
                calculationList.mapNotNull { fieldList ->
                    val itemList = fieldList.dropLast(1).map { item ->
                        item to kostraRecord.fieldAsIntOrDefault(item)
                    }
                    val itemListSum = itemList.sumOf { item -> item.second }
                    val sumItem = fieldList.last().let { item ->
                        item to kostraRecord.fieldAsIntOrDefault(item)
                    }

                    if (itemListSum != sumItem.second) {
                        CalculationItem(sumItem, itemList, listOf(kostraRecord.lineNumber))
                    } else null

                }
            }.ifEmpty { emptyList() }
        } else
            throw IndexOutOfBoundsException("FieldList (${fieldList.size}) != ($columnSize * ${fieldList.size / columnSize})")

}