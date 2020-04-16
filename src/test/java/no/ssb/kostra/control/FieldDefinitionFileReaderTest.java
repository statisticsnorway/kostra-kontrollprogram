package no.ssb.kostra.control;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FieldDefinitionFileReaderTest {
    private List<FieldDefinition> list;
    private String path;

    @Before
    public void beforeTest() {
        path = "/filbeskrivelse_bevilgningsregnskap.json";
    }

    @Test
    public void getFieldDefinitionTest() {
        list = FieldDefinitionFileReader.getFieldDefinition(path);
        assertNotNull(list);
        assertTrue(list.size() == 10);
    }
}
