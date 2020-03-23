package no.ssb.kostra.common;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

public class ControlListContainsCodeInCodelistTest {
    private List<String> codeList;
    private List<String> listOK;
    private List<String> listFail;
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        codeList = Arrays.asList("line01", "line02", "line03", "line04", "line05");
        listOK = Arrays.asList("line01", "line02", "line03", "line04", "line05");
        listFail = Arrays.asList("line01", "line 02", "line03", "line 04", "line05");
        er = new ErrorReport();
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "Kontrol Kode i kodeliste", "Feil: Fant ikke koden () i kodelisten", Constants.CRITICAL_ERROR);
    }

}
