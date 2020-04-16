package no.ssb.kostra.control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Record {
    private static int lineCount;
    private final int line;
    private final String record;
    Map<Integer, FieldDefinition> fields;
    Map<Integer, String> valuesByNumber;
    Map<String, String> valuesByName;

    public Record(final String record, final List<FieldDefinition> definitionList) {
        this.line = ++lineCount;
        this.record = record;
        this.fields = definitionList.stream()
                .collect(Collectors.toMap(FieldDefinition::getNumber, FieldDefinition::getFieldDefinition));
        this.valuesByNumber = this.fields.entrySet().stream()
                .distinct()
                .peek(System.out::println)
                .collect(Collectors.toMap(e -> e.getKey(), e -> this.record.substring(e.getValue().getFrom(), e.getValue().getTo())));

        this.valuesByName = this.fields.entrySet().stream()
                .distinct()
                .peek(System.out::println)
                .collect(Collectors.toMap(e -> e.getValue().getName(), e -> this.record.substring(e.getValue().getFrom(), e.getValue().getTo())));

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
        return Integer.valueOf(getFieldAsString(field));
    }

    public LocalDate getFieldAsLocalDate(String field, String dataTimePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dataTimePattern);
        return LocalDate.from(formatter.parse(getFieldAsString(field)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record1 = (Record) o;
        return line == record1.line &&
                Objects.equals(record, record1.record) &&
                Objects.equals(fields, record1.fields) &&
                Objects.equals(valuesByNumber, record1.valuesByNumber) &&
                Objects.equals(valuesByName, record1.valuesByName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, record, fields, valuesByNumber, valuesByName);
    }
}
