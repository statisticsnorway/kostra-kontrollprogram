package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.*;
import no.ssb.kostra.utils.Format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ControlFilbeskrivelse {
    private static String createLinenumber(Integer l, int line) {
        return "Linjenummer " + Format.sprintf("%0" + l + "d", line);
    }

    public static void doControl(List<Record> records, ErrorReport er) {
        er.incrementCount();
        int n = records.size();
        Integer l = String.valueOf(n).length();


        for (int i = 0; i < n; i++) {
            int line = i+1;
            Record r = records.get(i);

            r.getFieldDefinitions()
                    .forEach(fieldDefinition -> {
                        int from = fieldDefinition.getFrom();
                        int to = fieldDefinition.getTo();
                        int length = to - from + 1;
                        boolean mandatory = fieldDefinition.isMandatory();
                        String fieldName = fieldDefinition.getName();
                        Integer fieldNumber = fieldDefinition.getNumber();
                        String stringValue = r.getFieldAsString(fieldDefinition.getName());
                        String trimmedStringValue = r.getFieldAsTrimmedString(fieldDefinition.getName());
                        int trimmedStringLength = trimmedStringValue.length();
                        Integer integerValue = r.getFieldAsInteger(fieldDefinition.getName());
                        boolean hasIntegerValue = (integerValue != null);
                        List<Code> codeList = new ArrayList<>(fieldDefinition.getCodeList());
                        boolean hasCodeList = !codeList.isEmpty();
                        String datePattern = r.getFieldDefinitionByName(fieldDefinition.getName()).getDatePattern();
                        String dataType = fieldDefinition.getDataType();

                        // sjekker at feltdefinisjonen har antall tegn større enn 0
                        if (length < 1) {
                            er.addEntry(
                                    new ErrorReportEntry(
                                            "2. Filbeskrivelse"
                                            , createLinenumber(l, line)
                                            , " "
                                            , " "
                                            , "Kontroll 02 Filbeskrivelse, feltdefinisjonlengde"
                                            , "Korrigér felt " + fieldNumber + " / '" + fieldName + "', posisjon fra og med " + from + " til og med " + to + ", "
                                            + "sin feltdefinisjonlengde skal være større enn 0, men fant '" + length + "'."
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }

                        // sjekker at vi har en verdi
                        if (trimmedStringLength != 0) {
                            // verdi fins
                            // kontrollerer mot eventuell kodeliste
                            if (hasCodeList) {
                                if (codeList.stream().noneMatch(code -> code.getCode().equalsIgnoreCase(stringValue))) {
                                    er.addEntry(
                                            new ErrorReportEntry(
                                                    "2. Filbeskrivelse"
                                                    , createLinenumber(l, line)
                                                    , " "
                                                    , " "
                                                    , "Kontroll 02 Filbeskrivelse, feil kode i forhold til kodeliste"
                                                    , "Korrigér felt " + fieldNumber + " / '" + fieldName + "', posisjon fra og med " + from + " til og med " + to + ", "
                                                    + "sin kode '" + stringValue + "' fins ikke i " + codeList + "."
                                                    , Constants.CRITICAL_ERROR
                                            )
                                    );
                                }
                            }

                            switch (dataType) {
                                case "Integer":
                                    if (!hasIntegerValue) {
                                        er.addEntry(
                                                new ErrorReportEntry(
                                                        "2. Filbeskrivelse"
                                                        , createLinenumber(l, line)
                                                        , " "
                                                        , " "
                                                        , "Kontroll 02 Filbeskrivelse, feil i heltall-felt"
                                                        , "Korrigér felt " + fieldNumber + " / '" + fieldName + "', posisjon fra og med " + from + " til og med " + to + ", "
                                                        + "er et tallfelt, men inneholder '" + stringValue + "'."
                                                        , Constants.CRITICAL_ERROR
                                                )
                                        );
                                    }

                                    break;

                                case "Date":
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
                                    String defaultDate = "0".repeat(datePattern.length());

                                    try {
                                        if (!stringValue.equalsIgnoreCase(defaultDate)) {
                                            LocalDate.parse(stringValue, dtf);
                                        }
                                    } catch (Exception e) {
                                        er.addEntry(
                                                new ErrorReportEntry(
                                                        "2. Filbeskrivelse"
                                                        , createLinenumber(l, line)
                                                        , " "
                                                        , " "
                                                        , "Kontroll 02 Filbeskrivelse, feil i dato-felt"
                                                        , "Korrigér felt " + fieldNumber + " / '" + fieldName + "', posisjon fra og med " + from + " til og med " + to + ", "
                                                        + "er et datofelt med datomønster ('" + datePattern.toUpperCase() + "'), men inneholder '" + stringValue+ "'."
                                                        , Constants.CRITICAL_ERROR
                                                )
                                        );
                                    }
                                    break;
                            }


                        } else {
                            if (mandatory) {
                                // verdien kan ikke være blank
                                er.addEntry(
                                        new ErrorReportEntry(
                                                "2. Filbeskrivelse"
                                                , createLinenumber(l, line)
                                                , " "
                                                , " "
                                                , "Kontroll 02 Filbeskrivelse, mangler obligatorisk verdi"
                                                , "Korrigér felt " + fieldNumber + " / '" + fieldName + "', posisjon fra og med " + from + " til og med " + to + ", "
                                                + "er obligatorisk, men mangler verdi."
                                                , Constants.CRITICAL_ERROR
                                        )
                                );
                            }
                        }
                    });
        }
    }
}
