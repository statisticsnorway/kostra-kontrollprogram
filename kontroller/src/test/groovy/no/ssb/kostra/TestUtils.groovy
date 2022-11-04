package no.ssb.kostra

class TestUtils {

    static String getResourceAsString(String resourceName) {
        new String(TestUtils.class.getClassLoader()
                .getResourceAsStream(resourceName).readAllBytes())
    }
}