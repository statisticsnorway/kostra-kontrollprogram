package no.ssb.kostra.validation.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

class Rule001RecordLength(
    val length: Int
) : AbstractRule<List<String>>("Kontroll 001 : Recordlengde", Severity.FATAL) {
    override fun validate(context: List<String>): List<ValidationReportEntry>? = context
        .withIndex()
        .filter { it.value.length != length || it.value.contains("\t") }
        .map {
            createValidationReportEntry(
                messageText = """Korrigér filen slik at alle records er på $length tegn.<br/>
                            Mellomrom brukes for alle blanke posisjoner og avslutter med linjeskift.<br/>
                            Denne feilen hindrer de andre kontrollene i å bli kjørt""".trimIndent(),
                lineNumbers = listOf(it.index + 1)
            )
        }.ifEmpty { null }
}
