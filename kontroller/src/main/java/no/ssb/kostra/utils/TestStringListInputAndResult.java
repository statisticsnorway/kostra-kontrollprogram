package no.ssb.kostra.utils;

import java.util.List;

public class TestStringListInputAndResult {
    private final List<String> stringList;
    private final boolean result;

    public TestStringListInputAndResult(List<String> stringList, boolean result) {
        this.stringList = stringList;
        this.result = result;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public boolean isResult() {
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "result=" + result +
                ", stringList=" + stringList +
                '}';
    }
}
