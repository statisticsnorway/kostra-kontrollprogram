package no.ssb.kostra.control.famvern.s53;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.FieldDefinition;
import no.ssb.kostra.control.Record;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Toolkit;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> inputFileContent = args.getInputFileContent();
        List<FieldDefinition> fieldDefinitions = no.ssb.kostra.control.famvern.s52a.FieldDefinitions.getFieldDefinitions();
        List<Record> records = inputFileContent.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());
        Integer n = records.size();
        Integer l = String.valueOf(n).length();
        final String lf = Constants.lineSeparator;

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(records.stream(), er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        records.stream()
                // utled ALDER
                .map(r -> {
                    try {
                        r.setFieldAsInteger("ALDER", Toolkit.getAlderFromFnr(r.getFieldAsString("PERSON_FODSELSNR")));
                    } catch (Exception e) {
                        r.setFieldAsInteger("ALDER", -1);
                    }

                    return r;
                })
                .close();

        return er;
    }
}
