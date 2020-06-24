package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.*;

import java.util.ArrayList;
import java.util.List;

public class ControlFilbeskrivelse {
    public static void doControl(Record r, ErrorReport er) {
        er.incrementCount();

        r.getFieldDefinitions()
                .forEach(fieldDefinition -> {
                    int length = fieldDefinition.getTo() - fieldDefinition.getFrom() + 1;

                    // sjekk mot eventuell kodeliste
                    if (!fieldDefinition.getCodeList().isEmpty()) {
                        List<Code> codeList = new ArrayList<>(fieldDefinition.getCodeList());

                        if (!fieldDefinition.isMandatory()) {
                            codeList.add(new Code(" ".repeat(length), "Uoppgitt"));
                        }

                        if (codeList.stream().noneMatch(code -> code.getCode().equalsIgnoreCase(r.getFieldAsString(fieldDefinition.getName())))) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            "Filuttrekk"
                                            , "Linjenummer " + r.getLine()
                                            , " "
                                            , " "
                                            , "Kontroll 02 Filbeskrivelse"
                                            , "Korreksjon: Felt '" + fieldDefinition.getName() + "' sin verdi skal være 1 av "
                                            + codeList + ", men fant '" + r.getFieldAsString(fieldDefinition.getName()) + "'"
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }
                    }

                    // sjekk hvis feltet er obligatorisk
                    if (fieldDefinition.isMandatory()) {
                        if (r.getFieldAsString(fieldDefinition.getName()).equalsIgnoreCase(" ".repeat(length))) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            "Filuttrekk"
                                            , "Linjenummer " + r.getLine()
                                            , " "
                                            , " "
                                            , "Kontroll 02 Filbeskrivelse"
                                            , "Korreksjon: Felt '" + fieldDefinition.getName() + "' er obligatorisk, men mangler verdi"
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }
                    }

                    switch (fieldDefinition.getDataType()) {
                        case "Integer":
                            try {
                                r.getFieldAsInteger(fieldDefinition.getName());
                            } catch (NumberFormatException e) {
                                er.addEntry(
                                        new ErrorReportEntry(
                                                "Filuttrekk"
                                                , "Linjenummer " + r.getLine()
                                                , " "
                                                , " "
                                                , "Kontroll 02 Filbeskrivelse"
                                                , "Korreksjon: Felt '" + fieldDefinition.getName()
                                                + "' er et tallfelt, men inneholder '" + r.getFieldAsString(fieldDefinition.getName()) + "'"
                                                , Constants.CRITICAL_ERROR
                                        )
                                );
                            } catch (NullPointerException e){
                            }
                            break;

//                case "Date":
//                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(r.getFieldDefinitionByName(fieldDefinition.getName()).getDatePattern());
//
//                    try {
//                        LocalDate.parse(r.getFieldAsString(fieldDefinition.getName()), dtf);
//                    } catch (Exception e) {
//                        er.addEntry(
//                                new ErrorReportEntry(
//                                        "Filuttrekk"
//                                        , "Linjenummer " + r.getLine()
//                                        , " "
//                                        , " "
//                                        , "Kontroll 02 Filbeskrivelse"
//                                        , "Korreksjon: Felt '" + fieldDefinition.getName()
//                                        + "' er et datofelt med datomønster ('"
//                                        + r.getFieldDefinitionByName(fieldDefinition.getName()).getDatePattern()
//                                        + "'), men inneholder '" + r.getFieldAsString(fieldDefinition.getName()) + "'"
//                                        , Constants.CRITICAL_ERROR
//                                )
//                        );
//                    }
//                    break;
//
                        default:
                            break;
                    }
                });
    }
}
