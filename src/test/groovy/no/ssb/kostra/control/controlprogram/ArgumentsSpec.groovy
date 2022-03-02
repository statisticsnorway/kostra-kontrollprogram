package no.ssb.kostra.control.controlprogram


import no.ssb.kostra.controlprogram.Arguments
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets

class ArgumentsSpec extends Specification {
    @Unroll
    def "test behaviour of attachment"() {
        given:
        ByteArrayInputStream is = new ByteArrayInputStream(attachment.getBytes(StandardCharsets.ISO_8859_1))
        System.setIn(is)

        when:
        Arguments args = new Arguments(new String[]{"-s", "test", "-y", "0000", "-r", "0000", "-a", hasAttachment})
        args.getInputContentAsStringList()


        then:
        noExceptionThrown()
        args.harVedlegg() == hasExpectedAttachment
        args.hasInputContent() == hasExpectedInputContent
        args.getInputContentAsStringList().size() == noOfRecords

        where:
        hasAttachment | attachment  || noOfRecords | hasExpectedAttachment | hasExpectedInputContent
        "1"           | "123456789" || 1           | true                  | true
        "1"           | " "         || 0           | true                  | false
        "0"           | "123456789" || 1           | false                 | true
        "0"           | ""          || 0           | false                 | false
        "0"           | " "         || 0           | false                 | false
        "0"           | "  "        || 0           | false                 | false
    }

    @Unroll
    def "test all-args ctor"() {
        when:
        Arguments args = new Arguments(
                "test",
                "0000",
                " ",
                "000000",
                "UOPPGITT",
                "         ",
                "         ",
                hasAttachment,
                false,
                attachment
        )

        then:
        noExceptionThrown()
        args.harVedlegg() == hasExpectedAttachment
        args.hasInputContent() == hasExpectedInputContent
        args.getInputContentAsStringList().size() == noOfRecords

        where:
        hasAttachment | attachment           || noOfRecords | hasExpectedAttachment | hasExpectedInputContent
        true          | List.of("123456789") || 1           | true                  | true
        true          | List.of(" ")         || 0           | true                  | false
        false         | List.of("123456789") || 1           | false                 | true
        false         | List.of("")          || 0           | false                 | false
        false         | List.of(" ")         || 0           | false                 | false
        false         | List.of("  ")        || 0           | false                 | false
    }
}
