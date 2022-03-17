package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.*;
import no.ssb.kostra.utils.Format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("SpellCheckingInspection")
public class ControlFilbeskrivelse {

    private static String createLinenumber(final Integer l, final int line) {
        return "Linjenummer " + Format.sprintf("%0" + l + "d", line);
    }

    public static boolean doControl(
            final List<KostraRecord> records, final ErrorReport errorReport) {

        final var hasErrors = new AtomicBoolean(false);
        errorReport.incrementCount();

        final var n = records.size();
        final var l = String.valueOf(n).length();

        for (var i = 0; i < n; i++) {
            final var line = i + 1;
            final var record = records.get(i);

            record.getFieldDefinitions()
                    .forEach(fieldDefinition -> {
                        final var from = fieldDefinition.getFrom();
                        final var to = fieldDefinition.getTo();
                        final var length = to - from + 1;
                        final var mandatory = fieldDefinition.isMandatory();
                        final var fieldName = fieldDefinition.getName();
                        final var fieldNumber = fieldDefinition.getNumber();
                        final var stringValue = record.getFieldAsString(fieldDefinition.getName());
                        final var trimmedStringValue = record.getFieldAsTrimmedString(fieldDefinition.getName());
                        final var trimmedStringLength = trimmedStringValue.length();
                        final var integerValue = record.getFieldAsInteger(fieldDefinition.getName());
                        final var hasIntegerValue = (integerValue != null);
                        final var codeList = new ArrayList<>(fieldDefinition.getCodeList());
                        final var hasCodeList = !codeList.isEmpty();
                        final var datePattern = record.getFieldDefinitionByName(fieldDefinition.getName()).getDatePattern();
                        final var dataType = fieldDefinition.getDataType();

                        // sjekker at feltdefinisjonen har antall tegn større enn 0
                        if (length < 1) {
                            errorReport.addEntry(
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
                            hasErrors.set(true);
                        }

                        // sjekker at vi har en verdi
                        if (trimmedStringLength != 0) {
                            // verdi fins
                            // kontrollerer mot eventuell kodeliste
                            if (hasCodeList
                                    && codeList.stream().noneMatch(code -> code.getCode().equalsIgnoreCase(stringValue))) {

                                errorReport.addEntry(
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
                                hasErrors.set(true);
                            }

                            switch (dataType) {
                                case "Integer":
                                    if (!hasIntegerValue) {
                                        errorReport.addEntry(
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
                                        hasErrors.set(true);
                                    }
                                    break;

                                case "Date":
                                    final var dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
                                    final var defaultDate = "0".repeat(datePattern.length());

                                    try {
                                        if (!stringValue.equalsIgnoreCase(defaultDate)) {
                                            LocalDate.parse(stringValue, dateTimeFormatter);
                                        }
                                    } catch (Exception e) {
                                        errorReport.addEntry(
                                                new ErrorReportEntry(
                                                        "2. Filbeskrivelse"
                                                        , createLinenumber(l, line)
                                                        , " "
                                                        , " "
                                                        , "Kontroll 02 Filbeskrivelse, feil i dato-felt"
                                                        , "Korrigér felt " + fieldNumber + " / '" + fieldName + "', posisjon fra og med " + from + " til og med " + to + ", "
                                                        + "er et datofelt med datomønster ('" + datePattern.toUpperCase() + "'), men inneholder '" + stringValue + "'."
                                                        , Constants.CRITICAL_ERROR
                                                )
                                        );
                                        hasErrors.set(true);
                                    }
                                    break;
                            }
                        } else {
                            if (mandatory) {
                                // verdien kan ikke være blank
                                errorReport.addEntry(
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
                                hasErrors.set(true);
                            }
                        }
                    });
        }
        return hasErrors.get();
    }
}
