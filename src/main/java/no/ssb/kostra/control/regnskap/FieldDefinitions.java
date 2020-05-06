package no.ssb.kostra.control.regnskap;

import no.ssb.kostra.control.FieldDefinition;

import java.util.ArrayList;
import java.util.List;

public class FieldDefinitions {
    public static List<FieldDefinition> getFieldDefinitions() {
        return List.of(
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
    }

    public static Integer getFieldLength() {
        return getFieldDefinitions().get(getFieldDefinitions().size()-1).getTo();
    }
}
