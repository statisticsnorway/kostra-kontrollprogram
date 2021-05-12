package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static no.ssb.kostra.control.regnskap.FieldDefinitions.getFieldDefinitions;
import static org.junit.Assert.*;

public class ControlUlovligTegnITallfeltTest {
    InputStream sysInBackup;
    private Arguments args;

    public void setupTest(String inputFileContent) {
        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        args = new Arguments(new String[]{"-s", "0C", "-y", "2020", "-r", "340000"});
    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void testDoControlOK1() {
        Record r = new Record("0C2020 340000                  1860 990  -503152", getFieldDefinitions());
        ErrorReport er = new ErrorReport(args);
        ErrorReportEntry ere = new ErrorReportEntry(" ", " ", " ", " "
                , "Kontroll beløp, ugyldig tegn i tallfelt"
                , "Fant tegn for tabulator i tallfeltet. Mellomrom skal brukes for blanke tegn. Korrigér feil beløp (" + r.getFieldAsString("belop") + ")"
                , Constants.CRITICAL_ERROR
        );

        assertFalse(ControlUlovligTegnITallfelt.doControl(r, er, ere, "belop"));
    }

    @Test
    public void testDoControlFail1() {
        Record r = new Record("0C2020 340000                  1860 990 \t-503152", getFieldDefinitions());
        ErrorReport er = new ErrorReport(args);
        ErrorReportEntry ere = new ErrorReportEntry(" ", " ", " ", " "
                , "Kontroll beløp, ugyldig tegn i tallfelt"
                , "Fant tegn for tabulator i tallfeltet. Mellomrom skal brukes for blanke tegn. Korrigér feil beløp (" + r.getFieldAsString("belop") + ")"
                , Constants.CRITICAL_ERROR
        );

        assertTrue(ControlUlovligTegnITallfelt.doControl(r, er, ere, "belop"));
    }

    @Test
    public void testDoControlFail2() {
        Record r = new Record("0C2020 340000                  1860 990  .......", getFieldDefinitions());
        ErrorReport er = new ErrorReport(args);
        ErrorReportEntry ere = new ErrorReportEntry(" ", " ", " ", " "
                , "Kontroll beløp, ugyldig tegn i tallfelt"
                , "Fant tegn for tabulator i tallfeltet. Mellomrom skal brukes for blanke tegn. Korrigér feil beløp (" + r.getFieldAsString("belop") + ")"
                , Constants.CRITICAL_ERROR
        );

        assertTrue(ControlUlovligTegnITallfelt.doControl(r, er, ere, "belop"));
    }
}
