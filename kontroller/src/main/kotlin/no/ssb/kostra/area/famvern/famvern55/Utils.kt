package no.ssb.kostra.area.famvern.famvern55

import no.ssb.kostra.program.KostraRecord

object Utils {
    data class calculationItem(
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
            val calculationList = getRows(fieldList, columnSize) + getColumns(fieldList, fieldList.size / columnSize)
            kostraRecordList.map { kostraRecord ->
                calculationList.mapNotNull { fieldList ->
                    val itemList = fieldList.dropLast(1).map { item ->
                        item to kostraRecord.fieldAsIntOrDefault(item)
                    }
                    val itemListSum = itemList.sumOf { item -> item.second }
                    val sumItem = fieldList.last().let { item ->
                        item to kostraRecord.fieldAsIntOrDefault(item)
                    }

                    if (itemListSum != sumItem.second) {
                        calculationItem(sumItem, itemList, listOf(kostraRecord.lineNumber))
                    } else null

                }
            }.flatten().ifEmpty { emptyList() }
        } else
            throw IndexOutOfBoundsException("FieldList (${fieldList.size}) != ($columnSize * ${fieldList.size / columnSize})")

}
/*
context.map {
        Utils.validateMatrix(fieldList, columns).mapNotNull { fieldList ->
            val itemList = fieldList.dropLast(1).map { item -> item to it.fieldAsIntOrDefault(item) }
            val itemListSum = itemList.sumOf { item -> item.second }
            val sumItem = fieldList.last().let { item -> item to it.fieldAsIntOrDefault(item) }

 //           "Summen (${sumItem.first}) med verdi (${sumItem.second}) er ulik summen ($itemListSum) av følgende liste ($itemList)".also { that -> println(that) }

            if (itemListSum != sumItem.second) {
                createValidationReportEntry(
                    messageText = "Summen (${sumItem.first}) med verdi (${sumItem.second}) " +
                            "er ulik summen ($itemListSum) av følgende liste ($itemList)",
                    lineNumbers = listOf(it.lineNumber)
                )
            } else null
        }
    }.flatten().ifEmpty { null }
 */