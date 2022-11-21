package no.ssb.kostra.felles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodeList;

public class KostraRecord {
    private static int lineCount;
    private final int line;
    private final String record;
    private final List<FieldDefinition> fieldDefinitions;
    private final Map<String, FieldDefinition> fieldDefinitionByName;
    private Map<String, String> valuesByName = new HashMap<>();

    public KostraRecord(String record, List<FieldDefinition> definitionList) {
        this.line = ++lineCount;
        this.record = record;
        this.fieldDefinitions = definitionList;

        final var fieldDefinitionByNumber = definitionList.stream()
                .collect(Collectors.toMap(FieldDefinition::getNumber, FieldDefinition::getFieldDefinition));

        this.fieldDefinitionByName = definitionList.stream()
                .collect(Collectors.toMap(FieldDefinition::getName, FieldDefinition::getFieldDefinition));

        try {
            final var immutableValuesByName = fieldDefinitionByNumber.entrySet().stream()
                    .distinct()
                    .collect(Collectors.toMap(
                            e -> e.getValue().getName(),
                            e -> this.record.substring(e.getValue().getFrom() - 1, e.getValue().getTo())));

            this.valuesByName.putAll(immutableValuesByName);
        } catch (StringIndexOutOfBoundsException e) {
            this.valuesByName = new HashMap<>();
        }
    }

    public KostraRecord(final Map<String, String> immutableValuesByName, final List<FieldDefinition> definitionList) {
        this.line = ++lineCount;
        this.fieldDefinitions = definitionList;
        this.fieldDefinitionByName = definitionList.stream()
                .collect(Collectors.toMap(FieldDefinition::getName, FieldDefinition::getFieldDefinition));

        this.valuesByName.putAll(immutableValuesByName);

        this.record = definitionList.stream()
                .map(fieldDefinition -> {
                    final var width = fieldDefinition.getTo() - fieldDefinition.getFrom() + 1;

                    if (isCodeInCodeList(fieldDefinition.getDataType(), List.of("Integer"))) {
                        return String.format("%1$" + width + "s", valuesByName.getOrDefault(fieldDefinition.getName(), ""));
                    } else {
                        return String.format("%1$-" + width + "s", valuesByName.getOrDefault(fieldDefinition.getName(), ""));
                    }
                })
                .collect(Collectors.joining(""));
    }

    public static void resetLineCount() {
        lineCount = 0;
    }

    public int getLine() {
        return line;
    }

    public String getRecord() {
        return record;
    }

    public String getFieldAsString(final String field) {
        return valuesByName.getOrDefault(field, "");
    }

    public String getFieldAsTrimmedString(final String field) {
        return getFieldAsString(field).trim();
    }

    public Integer getFieldAsInteger(final String field) {
        try {
            return Integer.parseInt(getFieldAsTrimmedString(field));
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getFieldAsIntegerDefaultEquals0(final String field) {
        final var value = getFieldAsInteger(field);
        return value != null
                ? value
                : 0;
    }

    public void setFieldAsInteger(final String field, final Integer i) {
        this.valuesByName.put(field, String.valueOf(i));
    }

    public FieldDefinition getFieldDefinitionByName(final String name) {
        return this.fieldDefinitionByName.get(name);
    }

    public void setFieldDefinitionByName(final String name, final FieldDefinition fieldDefinition) {
        this.fieldDefinitionByName.put(name, fieldDefinition);
    }

    public LocalDate getFieldAsLocalDate(final String field) {
        try {
            final var definition = getFieldDefinitionByName(field);
            final var formatter = DateTimeFormatter.ofPattern(definition.getDatePattern());
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
        return this.valuesByName.toString() + "\n" + this.fieldDefinitionByName.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final var record1 = (KostraRecord) o;
        return line == record1.line &&
                Objects.equals(record, record1.record) &&
                Objects.equals(valuesByName, record1.valuesByName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, record, valuesByName);
    }
}
