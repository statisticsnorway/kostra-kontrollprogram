package no.ssb.kostra.felles;

public class Code {
    private final String code;
    private final String value;

    public Code(final String code, final String value) {
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
