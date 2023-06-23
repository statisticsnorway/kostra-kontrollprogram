package no.ssb.kostra.utils;

import java.util.List;

public record TestStringListInputAndResult(List<String> stringList, boolean result) {

    @Override
    public String toString() {
        return "{" +
                "result=" + result +
                ", stringList=" + stringList +
                '}';
    }
}
