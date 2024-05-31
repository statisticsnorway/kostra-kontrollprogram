package no.ssb.kostra.validation.rule

import no.ssb.kostra.validation.report.Severity

class Rule001RecordLength(
    private val length: Int
) : AbstractNoArgsRule<List<String>>("Kontroll 001 : Recordlengde", Severity.FATAL) {
    override fun validate(context: List<String>) = context
        .withIndex()
        .filter { it.value.length != length || it.value.contains("\t") }
        .map {
            createValidationReportEntry(
                messageText = """Korrigér filen slik at alle records er på $length tegn.<br/>
                            Fant record med lengde på ${it.value.length} tegn.<br/>
                            Mellomrom brukes for alle blanke posisjoner og avslutter med linjeskift.<br/>
                            Denne feilen hindrer de andre kontrollene i å bli kjørt""".trimIndent(),
                lineNumbers = listOf(it.index + 1)
            )
        }.ifEmpty { null }
}
