package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlHeltall;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ControlHeltallTest {
    private List<FieldDefinition> fieldDefinitions;
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000", "-i", "src/test/resources/15F/Testfil_21_OK_2020_15F_for_3401.xml"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "Kontrol Recordlengde", "Feil: feil antall posisjoner", Constants.CRITICAL_ERROR);

    }

    @Test
    public void testHeltallOK1() {
        Record p = new Record("0A20194040200                  1120 010    36328", fieldDefinitions);
        Integer value = p.getFieldAsInteger("belop");
        Integer expected = 36328;
        assertEquals(expected, value);
    }

    @Test
    public void testHeltallOK2() {
        Record p = new Record("0A20194040200                  1120 010   -36328", fieldDefinitions);
        Integer value = p.getFieldAsInteger("belop");
        Integer expected = -36328;
        assertEquals(expected, value);
    }

    @Test
    public void testHeltallFail1() {
        Record p = new Record("0A20194040200                  1120 010WILL FAIL", fieldDefinitions);
        Integer value = p.getFieldAsInteger("belop");
        assertNull(value);
//        assertEquals(Integer.valueOf(0), value);

    }

    @Test
    public void testHeltallFail2() {
        ControlHeltall.doControl(
                new Record("0A20194040200                  1120 010         ", fieldDefinitions),
                er,
                ere,
                "belop"
        );

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testHeltallFail3() {
        ControlHeltall.doControl(
                new Record("0A20194040200                  1120 010      ...", fieldDefinitions),
                er,
                ere,
                "belop"
        );

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
