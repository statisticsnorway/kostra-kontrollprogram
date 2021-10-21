package no.ssb.kostra.felles;

public class Code {
    private String code;
    private String value;

    public Code(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String toString(){
        return getCode() + "=" + getValue();
    }
}
