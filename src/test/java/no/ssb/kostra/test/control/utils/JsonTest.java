package no.ssb.kostra.test.control.utils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class JsonTest {
    private List<String> kontoklasse;
    private String ok1;
    private String ok2;
    private String fail1;

    @Before
    public void beforeTest() {
        kontoklasse = new ArrayList<>();
        ok1 = "0";
        ok2 = "1";
        fail1 = "F";
    }

    @Test
//    @Ignore
    public void testValidFunksjon() {
        InputStream is = getClass().getResourceAsStream("/Regnskap_2017_0A_0C_kontoklasse.json");

        try (JsonReader rdr = Json.createReader(is)) {
            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("kontoklasse");
            for (JsonValue result : results.getValuesAs(JsonValue.class)) {
                kontoklasse.add(result.toString());
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        System.out.println(kontoklasse);
        Consumer<String> p;
        String b1 = kontoklasse.stream()
                .peek(System.out::print)
                .filter(line -> !ok1.equalsIgnoreCase(line))
                .peek(System.out::println)
                .findAny()
                .orElse(null);
        System.out.print(b1);
        try {
            assertTrue(0 < b1.length());
//        assertTrue(kontoklasse.stream().anyMatch(k -> k.equalsIgnoreCase(ok2)));
//
//        assertFalse(kontoklasse.stream().anyMatch(k -> k.equalsIgnoreCase(fail1)));
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
}
