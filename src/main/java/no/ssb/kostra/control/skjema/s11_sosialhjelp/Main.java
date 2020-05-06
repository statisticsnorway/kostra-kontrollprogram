package no.ssb.kostra.control.skjema.s11_sosialhjelp;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.FieldDefinition;
import no.ssb.kostra.control.Record;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> list1 = args.getInputFileContent();
        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<Record> records = list1.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());
        String saksbehandler = "Filuttrekk";
        String journalnummer = "Linje ";
        Integer n = records.size();
        Integer l = String.valueOf(n).length();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(records.stream(), er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        return er;
    }
}
