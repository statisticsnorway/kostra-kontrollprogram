package no.ssb.kostra.felles;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class FieldDefinition {
    Integer number;
    String name;
    String dataType;
    String viewType;
    Integer from;
    Integer to;
    List<Code> codeList;
    String datePattern;
    boolean mandatory;

    public FieldDefinition(
            int number,
            String name,
            String dataType,
            String viewType,
            int from,
            int to,
            List<Code> codeList,
            String datePattern,
            boolean mandatory
    ) {
        this.number = number;
        this.name = name;
        this.dataType = dataType;
        this.viewType = viewType;
        this.from = from;
        this.to = to;
        this.codeList = codeList;
        this.datePattern = datePattern;
        this.mandatory = mandatory;

        if (this.viewType == null || this.viewType.equalsIgnoreCase("")) {
            this.viewType = "inputbox";
        }

        if (this.viewType.equalsIgnoreCase("checkbox") && (this.codeList == null || this.codeList.isEmpty())) {
            this.codeList = Arrays.asList(new Code("", "uninitialzed"), new Code("0", "unchecked"), new Code("1", "checked"));
        }

        if (this.dataType.equalsIgnoreCase("date") && (this.datePattern == null || this.datePattern.length() == 0)) {
            this.datePattern = "ddMMyyyy";
        }
    }

    public FieldDefinition getFieldDefinition() {
        return new FieldDefinition(
                this.number,
                this.name,
                this.dataType,
                this.viewType,
                this.from,
                this.to,
                this.codeList,
                this.datePattern,
                this.mandatory
        );
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getViewType() {
        return viewType;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(final List<Code> codeList) {
        this.codeList = codeList;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public boolean isMandatory() {
        return mandatory;
    }
}
