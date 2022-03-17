package no.ssb.kostra.control.famvern.s52b;

import no.ssb.kostra.felles.Code;
import no.ssb.kostra.felles.FieldDefinition;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class FieldDefinitions {
    public static List<FieldDefinition> getFieldDefinitions() {
        return List.of(
                new FieldDefinition(1, "REGION_NR_B",
                        "String",
                        "textBox",
                        1, 6
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(2, "KONTOR_NR_B",
                        "String",
                        "textBox",
                        7, 9
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(3, "GRUPPE_NR_B",
                        "String",
                        "textBox",
                        10, 15
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(4, "GRUPPE_NAVN_B",
                        "String",
                        "textBox",
                        16, 45
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(5, "DATO_GRSTART_B",
                        "Date",
                        "textBox",
                        46, 53
                        , List.of(),
                        "ddMMyyyy",
                        false),
                new FieldDefinition(6, "STRUKTUR_GR_B",
                        "Integer",
                        "dropDownList",
                        54, 54
                        , List.of(
                        new Code("1", "Par")
                        , new Code("2", "Barn (under 18 år)")
                        , new Code("3", "Individ")
                        , new Code("4", "Familie")
                        , new Code("5", "Foreldre")),
                        "",
                        false),
                new FieldDefinition(7, "HOVEDI_GR_B",
                        "String",
                        "dropDownList",
                        55, 56
                        , List.of(
                        new Code("01", "Samlivskurs")
                        , new Code("02", "Samlivsbrudd")
                        , new Code("03", "Samarbeid om barn etter brudd")
                        , new Code("04", "Barn som har opplevd brudd i familien")
                        , new Code("05", "Vold/overgrep")
                        , new Code("06", "Sinnemestring")
                        , new Code("07", "Kultur-/Minoritetsspørsmål")
                        , new Code("08", "Foreldreveiledning")
                        , new Code("09", "Foreldre som har mistet omsorgen for egne barn")
                        , new Code("10", "Andre alvorlige hendelser")
                        , new Code("11", "Annet, spesifiser")),
                        "",
                        false),
                new FieldDefinition(81, "ANTMOTERTOT_IARET_B",
                        "Integer",
                        "textBox",
                        57, 59
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(82, "ANTMOTERTOT_OPPR_B",
                        "Integer",
                        "textBox",
                        60, 62
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(91, "TIMERTOT_IARET_B",
                        "Integer",
                        "textBox",
                        63, 65
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(92, "TIMERTOT_OPPR_B",
                        "Integer",
                        "textBox",
                        66, 68
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(101, "ANTDELT_IARET_B",
                        "Integer",
                        "textBox",
                        69, 71
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(102, "ANTDELT_OPPR_B",
                        "Integer",
                        "textBox",
                        72, 74
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(11, "ANTTER_GRUPPEB_B",
                        "Integer",
                        "textBox",
                        75, 76
                        , List.of(),
                        "",
                        false),
                new FieldDefinition(12, "TOLK_B",
                        "Integer",
                        "dropDownList",
                        77, 77
                        , List.of(
                        new Code("1", "Ja")
                        , new Code("2", "Nei")),
                        "",
                        false),
                new FieldDefinition(13, "STATUS_ARETSSL_B",
                        "Integer",
                        "dropDownList",
                        78, 78
                        , List.of(
                        new Code("1", "Gruppebehandlingen ikke avsluttet i inneværende år")
                        , new Code("2", "Avsluttet")),
                        "",
                        false),
                new FieldDefinition(14, "DATO_GRAVSLUTN_B",
                        "Date",
                        "textBox",
                        79, 86
                        , List.of(),
                        "ddMMyyyy",
                        false)
        );
    }

    public static Integer getFieldLength() {
        return getFieldDefinitions().get(getFieldDefinitions().size() - 1).getTo();
    }
}
