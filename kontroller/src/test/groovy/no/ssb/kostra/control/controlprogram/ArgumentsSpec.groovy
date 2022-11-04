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
        Arguments args = new Arguments(new String[]{"-s", "test", "-y", "0000", "-r", "0000", "-a", hasAttachment, "-e", runAsExternalProcess})
        args.getInputContentAsStringList()


        then:
        noExceptionThrown()
        args.harVedlegg() == hasExpectedAttachment
        args.hasInputContent() == hasExpectedInputContent
        args.getInputContentAsStringList().size() == noOfRecords
        args.isRunAsExternalProcess() == isRunAsExternalProcess

        where:
        hasAttachment | attachment  | runAsExternalProcess || noOfRecords | hasExpectedAttachment | hasExpectedInputContent | isRunAsExternalProcess
        "1"           | "123456789" | "0"                  || 1           | true                  | true                    | false
        "1"           | "123456789" | "1"                  || 1           | true                  | true                    | true
        "1"           | " "         | "0"                  || 0           | true                  | false                   | false
        "0"           | "123456789" | "0"                  || 1           | false                 | true                    | false
        "0"           | ""          | "0"                  || 0           | false                 | false                   | false
        "0"           | " "         | "0"                  || 0           | false                 | false                   | false
        "0"           | "  "        | "0"                  || 0           | false                 | false                   | false
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
