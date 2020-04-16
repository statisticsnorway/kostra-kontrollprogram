package no.ssb.kostra.common;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ControlRecordLengthTest {
    private int lengthOK;
    private int lengthFail;
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private String inputFileContent;
    private List<FieldDefinition> fieldDefinitions;
    private List<String> content;
    private List<Record> posteringList;

    @Before
    public void beforeTest() {
        lengthOK = 48;
        lengthFail = 4;
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "Kontrol Recordlengde", "Feil: feil antall posisjoner", Constants.CRITICAL_ERROR);
        inputFileContent = "0A20194040200                  1120 010    36328\n" +
                "0A20194040200                  1121 010     4306\n" +
                "0A20194040200                  1130 010      864";

        byte[] bytes = inputFileContent.getBytes(StandardCharsets.ISO_8859_1);
        InputStream inputStream = new ByteArrayInputStream(bytes);

        try {
            args.readFileFromStdin(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fieldDefinitions = FieldDefinitionFileReader.getFieldDefinition("/filbeskrivelse_bevilgningsregnskap");
    }

    @Test
    public void testContentOK1() {
        content = args.getInputFileContent();

        assertNotNull("Has content", content);
    }

    @Test
    public void testPosteringListOK1() {
        content = args.getInputFileContent();
        posteringList = content.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());

        assertFalse(posteringList.isEmpty());
    }

    @Test
    public void testControlRecordLengthOK1() {
        content = args.getInputFileContent();
        posteringList = content.stream()
                .map(line -> new Record(line, fieldDefinitions))
                .map(postering -> ControlRecordLengde.doControl(postering, er, ere, lengthOK))
                .collect(Collectors.toList());
        assertTrue(er.isEmpty());
        assertTrue(er.getErrorType() == Constants.NO_ERROR);
    }

    @Test
    public void testControlRecordLengthFail1() {
        content = args.getInputFileContent();
        posteringList = content.stream()
                .map(line -> new Record(line, fieldDefinitions))
                .map(postering -> ControlRecordLengde.doControl(postering, er, ere, lengthFail))
                .collect(Collectors.toList());
        assertFalse(er.isEmpty());
        assertTrue(er.getErrorType() == Constants.CRITICAL_ERROR);
    }
}
