package no.ssb.kostra.control.felles

import no.ssb.kostra.control.regnskap.FieldDefinitions
import no.ssb.kostra.felles.FieldDefinition
import no.ssb.kostra.felles.Record
import spock.lang.Specification

class UtilsSpec extends Specification {
    private static final List<FieldDefinition> definitions = FieldDefinitions.getFieldDefinitions();

    def "should add linenumbering"() {
        given:
        def recordList = new ArrayList<Record>()

        Map recordMap = new HashMap<String, String>()
        recordMap.putAll(Map.of("skjema", skjema, "kontoklasse", kontoklasse, "funksjon_kapittel", funksjon))
        recordList.add(new Record(recordMap, definitions))

        when:
        def recordListWithLinenumbers = Utils.addLineNumbering(recordList)
        def record = (Record) recordListWithLinenumbers.get(0)
        def testResult = record.getFieldAsInteger("linjenummer")

        then:
        testResult == expectedResult

        where:
        skjema | kontoklasse | funksjon || expectedResult
        "0A"   | "1"         | "100 "   || 1
        "0A"   | "1"         | "841 "   || 1
    }
}
