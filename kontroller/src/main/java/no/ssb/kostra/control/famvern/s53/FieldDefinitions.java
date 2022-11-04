package no.ssb.kostra.control.famvern.s53;

import no.ssb.kostra.felles.FieldDefinition;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class FieldDefinitions {
    public static List<FieldDefinition> getFieldDefinitions() {
        return List.of(
                new FieldDefinition(1, "FYLKE_NR",
                        "String",
                        "textBox",
                        1, 2
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(2, "KONTORNR",
                        "String",
                        "textBox",
                        3, 5
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(51, "TILTAK_PUBLIKUM_TILTAK",
                        "Integer",
                        "textBox",
                        6, 9
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(52, "TILTAK_PUBLIKUM_TIMER",
                        "Integer",
                        "textBox",
                        10, 13
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(61, "VEILEDNING_STUDENTER_TILTAK",
                        "Integer",
                        "textBox",
                        14, 17
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(62, "VEILEDNING_STUDENTER_TIMER",
                        "Integer",
                        "textBox",
                        18, 21
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(71, "VEILEDNING_HJELPEAP_TILTAK",
                        "Integer",
                        "textBox",
                        22, 25
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(72, "VEILEDNING_HJELPEAP_TIMER",
                        "Integer",
                        "textBox",
                        26, 29
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(91, "INFO_MEDIA_TILTAK",
                        "Integer",
                        "textBox",
                        30, 33
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(92, "INFO_MEDIA_TIMER",
                        "Integer",
                        "textBox",
                        34, 37
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(101, "TILSYN_TILTAK",
                        "Integer",
                        "textBox",
                        38, 41
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(102, "TILSYN_TIMER",
                        "Integer",
                        "textBox",
                        42, 45
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(111, "FORELDREVEIL_TILTAK",
                        "Integer",
                        "textBox",
                        46, 49
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(112, "FORELDREVEIL_TIMER",
                        "Integer",
                        "textBox",
                        50, 53
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(121, "ANNET_TILTAK",
                        "Integer",
                        "textBox",
                        54, 57
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(122, "ANNET_TIMER",
                        "Integer",
                        "textBox",
                        58, 61
                        , List.of(),
                        "",
                        false)
        );
    }

    public static Integer getFieldLength() {
        return getFieldDefinitions().get(getFieldDefinitions().size() - 1).getTo();
    }
}
