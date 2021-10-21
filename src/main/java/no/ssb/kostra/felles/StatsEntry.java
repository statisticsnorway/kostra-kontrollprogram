package no.ssb.kostra.felles;

public class StatsEntry {
    private String id;
    private String value;

    public StatsEntry() {
    }

    public StatsEntry(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
