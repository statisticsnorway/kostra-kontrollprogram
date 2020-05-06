package no.ssb.kostra.control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Record {
    private static int lineCount;
    private final int line;
    private final String record;
    Map<Integer, FieldDefinition> fieldDefinitionByNumber;
    Map<String, FieldDefinition> fieldDefinitionByName;
    Map<Integer, String> valuesByNumber;
    Map<String, String> valuesByName;

    public Record(final String record, final List<FieldDefinition> definitionList) {
        this.line = ++lineCount;
        this.record = record;
        this.fieldDefinitionByNumber = definitionList.stream()
                .collect(Collectors.toMap(FieldDefinition::getNumber, FieldDefinition::getFieldDefinition));
        this.fieldDefinitionByName = definitionList.stream()
                .collect(Collectors.toMap(FieldDefinition::getName, FieldDefinition::getFieldDefinition));

        try {
            this.valuesByNumber = this.fieldDefinitionByNumber.entrySet().stream()
                    .distinct()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> this.record.substring(e.getValue().getFrom() - 1, e.getValue().getTo())));

            this.valuesByName = this.fieldDefinitionByNumber.entrySet().stream()
                    .distinct()
                    .collect(Collectors.toMap(
                            e -> e.getValue().getName(),
                            e -> this.record.substring(e.getValue().getFrom() - 1, e.getValue().getTo())));

        } catch (StringIndexOutOfBoundsException e){
            this.valuesByNumber = new HashMap<>();
            this.valuesByName = new HashMap<>();
        }
    }

    public int getLine() {
        return line;
    }


    public String getRecord() {
        return record;
    }

    public String getFieldAsString(String field) {
        return valuesByName.getOrDefault(field, "");
    }

    public String getFieldAsTrimmedString(String field) {
        return getFieldAsString(field).trim();
    }

    public Integer getFieldAsInteger(String field) {
        try {
            return Integer.valueOf(getFieldAsTrimmedString(field));

        } catch (NumberFormatException e){
            return null;
        }
    }

    public FieldDefinition getFieldDefinitionByNumber(Integer number){
        return this.fieldDefinitionByNumber.get(number);
    }

    public FieldDefinition getFieldDefinitionByName(String name){
        return this.fieldDefinitionByName.get(name);
    }

    public LocalDate getFieldAsLocalDate(String field, String dateTimePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return LocalDate.from(formatter.parse(getFieldAsString(field)));
    }

    public boolean validate(){
        return this.fieldDefinitionByNumber
                .values()
                .stream()
                .allMatch(f -> {
                    String value = this.getFieldAsString(f.getName());
                    String trimmedValue = this.getFieldAsTrimmedString(f.getName());

                    switch (f.getDataType()){
                        case "String":
                            return !f.getCodeList().isEmpty()
                                    && f.codeList.stream().noneMatch(c -> c.getValue().equalsIgnoreCase(value));

                        case "Integer":
                            return !trimmedValue.equalsIgnoreCase("")
                                    && this.getFieldAsInteger(f.getName()) == null;

                        case "date":
                            if (!trimmedValue.equalsIgnoreCase("")) {
                                try {
                                    this.getFieldAsLocalDate(f.getName(), f.getDatePattern());
                                    return false;
                                } catch (DateTimeParseException e) {
                                    return true;
                                }
                            }

                            return false;
                    }

                    return false;
                });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record1 = (Record) o;
        return line == record1.line &&
                Objects.equals(record, record1.record) &&
                Objects.equals(fieldDefinitionByNumber, record1.fieldDefinitionByNumber) &&
                Objects.equals(valuesByNumber, record1.valuesByNumber) &&
                Objects.equals(valuesByName, record1.valuesByName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, record, fieldDefinitionByNumber, valuesByNumber, valuesByName);
    }
}
