package no.ssb.kostra.control.regnskap;

import no.ssb.kostra.felles.FieldDefinition;

import java.util.ArrayList;
import java.util.List;

import static no.ssb.kostra.felles.Constants.INTEGER_TYPE;
import static no.ssb.kostra.felles.Constants.STRING_TYPE;

@SuppressWarnings("SpellCheckingInspection")
public class FieldDefinitions {
    private FieldDefinitions() {
    }

    public static List<FieldDefinition> getFieldDefinitions() {
        return List.of(
                new FieldDefinition(1, "skjema", STRING_TYPE, "", 1, 2, new ArrayList<>(), "", true),
                new FieldDefinition(2, "aargang", STRING_TYPE, "", 3, 6, new ArrayList<>(), "", true),
                new FieldDefinition(3, "kvartal", STRING_TYPE, "", 7, 7, new ArrayList<>(), "", false),
                new FieldDefinition(4, "region", STRING_TYPE, "", 8, 13, new ArrayList<>(), "", true),
                new FieldDefinition(5, "orgnr", STRING_TYPE, "", 14, 22, new ArrayList<>(), "", false),
                new FieldDefinition(6, "foretaksnr", STRING_TYPE, "", 23, 31, new ArrayList<>(), "", false),
                new FieldDefinition(7, "kontoklasse", STRING_TYPE, "", 32, 32, new ArrayList<>(), "", false),
                new FieldDefinition(8, "funksjon_kapittel", STRING_TYPE, "", 33, 36, new ArrayList<>(), "", false),
                new FieldDefinition(9, "art_sektor", STRING_TYPE, "", 37, 39, new ArrayList<>(), "", false),
                new FieldDefinition(10, "belop", INTEGER_TYPE, "", 40, 48, new ArrayList<>(), "", true));
    }

    public static Integer getFieldLength() {
        return getFieldDefinitions().get(getFieldDefinitions().size() - 1).getTo();
    }
}
