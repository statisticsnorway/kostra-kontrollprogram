package no.ssb.kostra.felles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Record {
    private static int lineCount;
    private final int line;
    private final String record;
    private final List<FieldDefinition> fieldDefinitions;
    private final Map<String, FieldDefinition> fieldDefinitionByName;
    private Map<String, String> valuesByName;

    public Record(final String record, final List<FieldDefinition> definitionList) {
        this.line = ++lineCount;
        this.record = record;
        this.fieldDefinitions = definitionList;
        Map<Integer, FieldDefinition> fieldDefinitionByNumber = definitionList.stream()
                .collect(Collectors.toMap(FieldDefinition::getNumber, FieldDefinition::getFieldDefinition));
        this.fieldDefinitionByName = definitionList.stream()
                .collect(Collectors.toMap(FieldDefinition::getName, FieldDefinition::getFieldDefinition));

        try {
            this.valuesByName = fieldDefinitionByNumber.entrySet().stream()
                    .distinct()
                    .collect(Collectors.toMap(
                            e -> e.getValue().getName(),
                            e -> this.record.substring(e.getValue().getFrom() - 1, e.getValue().getTo())));

        } catch (StringIndexOutOfBoundsException e) {
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
            return Integer.parseInt(getFieldAsTrimmedString(field));

        } catch (Exception e) {
            return null;
        }
    }

    public Integer getFieldAsIntegerDefaultEquals0(String field) {
        Integer value = getFieldAsInteger(field);

        return (value != null) ? value : 0;
    }

    public void setFieldAsInteger(String field, Integer i) {
        this.valuesByName.put(field, String.valueOf(i));
    }

    public FieldDefinition getFieldDefinitionByName(String name) {
        return this.fieldDefinitionByName.get(name);
    }

    public LocalDate getFieldAsLocalDate(String field) {
        try {
            FieldDefinition definition = getFieldDefinitionByName(field);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(definition.getDatePattern());
            return LocalDate.from(formatter.parse(getFieldAsString(field)));
        } catch (Exception e) {
            return null;
        }
    }

    public List<FieldDefinition> getFieldDefinitions() {
        return fieldDefinitions;
    }

    public List<String> getNames() {
        return new ArrayList<>(this.valuesByName.keySet());
    }

    @Override
    public String toString() {
        return this.valuesByName.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record1 = (Record) o;
        return line == record1.line &&
                Objects.equals(record, record1.record) &&
                Objects.equals(valuesByName, record1.valuesByName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, record, valuesByName);
    }
}
