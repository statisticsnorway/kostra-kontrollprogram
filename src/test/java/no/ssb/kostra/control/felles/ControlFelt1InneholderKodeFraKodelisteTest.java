package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ControlFelt1InneholderKodeFraKodelisteTest {
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private Record r;

    @Before
    public void beforeTest() {
        args = new Arguments(new String[]{"-s", "0A", "-y", "2020", "-r", "030100"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Kode i kodeliste", "Feil: fant ikke kode i kodeliste", Constants.CRITICAL_ERROR);
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "skjema", "String", "", 1, 2, new ArrayList<>(), "", true),
                new FieldDefinition(2, "aargang", "String", "", 3, 6, new ArrayList<>(), "", true),
                new FieldDefinition(3, "kvartal", "String", "", 7, 7, new ArrayList<>(), "", false),
                new FieldDefinition(4, "region", "String", "", 8, 13, new ArrayList<>(), "", true),
                new FieldDefinition(5, "orgnr", "String", "", 14, 22, new ArrayList<>(), "",false),
                new FieldDefinition(6, "foretaksnr", "String", "", 23, 31, new ArrayList<>(), "",false),
                new FieldDefinition(7, "kontoklasse", "String", "", 32, 32, new ArrayList<>(), "", false),
                new FieldDefinition(8, "funksjon_kapittel", "String", "", 33, 36, new ArrayList<>(), "", false),
                new FieldDefinition(9, "art_sektor", "String", "", 37, 39, new ArrayList<>(), "", false),
                new FieldDefinition(10, "belop", "Integer", "", 40, 48, new ArrayList<>(), "", true)
        );
        r = new Record("0A2020 030100                  0100 090    12345", fieldDefinitions);
    }

    @Test
    public void testOK() {
        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "skjema", List.of(args.getSkjema()));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "aargang", List.of(args.getAargang()));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "kvartal", Collections.singletonList(" "));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "region", List.of(args.getRegion()));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "orgnr", Collections.singletonList("         "));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "foretaksnr", Collections.singletonList("         "));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "kontoklasse", Collections.singletonList("0"));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "kontoklasse", List.of("0", "1"));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "funksjon_kapittel", Collections.singletonList("100 "));
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "art_sektor", Collections.singletonList("090"));
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFail() {
        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "skjema", List.of("FA"));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testFail2() {
        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "kontoklasse", List.of("1", "2"));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }


    @Test
    public void testBlank1() {
        ControlFelt1InneholderKodeFraKodeliste.doControl(r, er, ere, "skjema", List.of(""));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
