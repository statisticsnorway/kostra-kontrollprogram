package no.ssb.kostra.felles;

public record Code(String code, String value) {

    public String toString() {
        return code() + "=" + value();
    }
}
