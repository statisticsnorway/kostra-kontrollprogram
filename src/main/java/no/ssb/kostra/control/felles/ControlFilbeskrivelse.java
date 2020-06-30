package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.utils.Format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ControlFilbeskrivelse {
    private static String createLinenumber(Integer l, int line) {
        return "Linjenummer " + Format.sprintf("%0" + l + "d", line);
    }

    public static void doControl(Record r, ErrorReport er, int noOfRecordsLength) {
        er.incrementCount();

        r.getFieldDefinitions()
                .forEach(fieldDefinition -> {
                    int length = fieldDefinition.getTo() - fieldDefinition.getFrom() + 1;
                    boolean mandatory = fieldDefinition.isMandatory();
                    String fieldName = fieldDefinition.getName();
                    String stringValue = r.getFieldAsString(fieldDefinition.getName());
                    String trimmedStringValue = r.getFieldAsTrimmedString(fieldDefinition.getName());
                    int trimmedStringLength = trimmedStringValue.length();
                    Integer integerValue = r.getFieldAsInteger(fieldDefinition.getName());
                    boolean hasIntegerValue = integerValue != null;
                    List<Code> codeList = new ArrayList<>(fieldDefinition.getCodeList());
                    boolean hasCodeList = !codeList.isEmpty();
                    String datePattern = r.getFieldDefinitionByName(fieldDefinition.getName()).getDatePattern();
                    String dataType = fieldDefinition.getDataType();

                    // sjekker at feltdefinisjonen har antall tegn større enn 0
                    if (length < 1) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        "Filuttrekk"
                                        , createLinenumber(noOfRecordsLength, r.getLine())
                                        , " "
                                        , " "
                                        , "Kontroll 02 Filbeskrivelse"
                                        , "Korreksjon: Felt '" + fieldName + "' sin feltdefinisjonlengde skal være større enn 0 "
                                        + ", fant '" + length + "'"
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
                                                "Filuttrekk"
                                                , createLinenumber(noOfRecordsLength, r.getLine())
                                                , " "
                                                , " "
                                                , "Kontroll 02 Filbeskrivelse"
                                                , "Korreksjon: Felt '" + fieldName + "' sin verdi '" + stringValue + "' fins ikke i " + codeList
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
                                                    "Filuttrekk"
                                                    , createLinenumber(noOfRecordsLength, r.getLine())
                                                    , " "
                                                    , " "
                                                    , "Kontroll 02 Filbeskrivelse"
                                                    , "Korreksjon: Felt '" + fieldName
                                                    + "' er et tallfelt, men inneholder '" + stringValue + "'"
                                                    , Constants.CRITICAL_ERROR
                                            )
                                    );
                                }

                                break;

                            case "Date":
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);

                                try {
                                    LocalDate.parse(stringValue, dtf);
                                } catch (Exception e) {
                                    er.addEntry(
                                            new ErrorReportEntry(
                                                    "Filuttrekk"
                                                    , createLinenumber(noOfRecordsLength, r.getLine())
                                                    , " "
                                                    , " "
                                                    , "Kontroll 02 Filbeskrivelse"
                                                    , "Korreksjon: Felt '" + fieldDefinition.getName()
                                                    + "' er et datofelt med datomønster ('"
                                                    + datePattern.toUpperCase()
                                                    + "'), men inneholder '" + stringValue + "'"
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
                                            "Filuttrekk"
                                            , createLinenumber(noOfRecordsLength, r.getLine())
                                            , " "
                                            , " "
                                            , "Kontroll 02 Filbeskrivelse"
                                            , "Korreksjon: Felt '" + fieldName + "' er obligatorisk, men mangler verdi"
                                            , Constants.CRITICAL_ERROR
                                    )
                            );
                        }
                    }
                });
    }
}
