package no.ssb.kostra.controlprogram;

import java.util.*;

import junit.framework.*;


public class GetOptTest extends TestCase {

    private final String[] goodArgs = {
            "-h", "-o", "outfile", "infile"
    };
    private final String[] goodLongArgs = {
            "-help", "-output-file", "outfile", "infile"
    };

    private final String badArgChars = "f1o";
    private final String[] badArgs = {
            "-h", "-o", "outfile", "infile"
    };

    private final GetOptDesc[] options = {
            new GetOptDesc('o', "output-file", true),
            new GetOptDesc('h', "help", false),
    };

    public void testBadArgChar() {
        String bad = "abc@";
        try {
            new GetOpt(bad);
            fail("GetOpt(" + bad + ") did not throw expected exception");
        } catch (IllegalArgumentException ex) {
            System.err.println("Caught expected exception " + ex);
        }
    }

    public void testOldwayGood() {
        String goodArgChars = "o:h";
        process1(goodArgChars, goodArgs);
        process2(goodArgChars, goodArgs, false);
    }

    public void testOldwayBadCharsGoodArgs() {
        process1(badArgChars, goodArgs);
        process2(badArgChars, goodArgs, true);
    }

    public void testOldwayBadCharsBadArgs() {
        process1(badArgChars, badArgs);
        process2(badArgChars, badArgs, true);
    }

    public void testNewWayShort() {
        GetOpt go = new GetOpt(options);
        var map = go.parseArguments(goodArgs);
        newWayInner(go, map);
    }

    public void testNewWayLong() {
        GetOpt go = new GetOpt(options);
        var map = go.parseArguments(goodLongArgs);
        newWayInner(go, map);
    }

    protected void newWayInner(GetOpt go, Map map) {
        assertFalse(map.size() == 0);
        if (map.size() == 0) {
            throw new IllegalArgumentException(
                    "Unexpected empty map");
        }
        for (var o : map.keySet()) {
            String key = (String) o;
            char c = key.charAt(0);
            String val = (String) map.get(key);
            switch (c) {
                case '?' ->
                case 'o' -> assertEquals(val, "outfile");
                case 'f', 'h', '1' -> assertNull(val);
                default -> throw new IllegalArgumentException(
                        "Unexpected c value " + c);
            }
        }
        assertEquals(1, go.getFilenameList().size());
        assertEquals("infile", go.getFilenameList().get(0));
    }

    void process1(String argChars, String[] args) {

        System.out.println("** START ** " + argChars);

        GetOpt go = new GetOpt(argChars);

        char c;
        while ((c = go.getopt(args)) != 0) {
            if (c == '?') {
                System.out.print("Bad option");
            } else {
                System.out.print("Found " + c);
                if (go.optarg() != null)
                    System.out.print("; Option " + go.optarg());
            }
            System.out.println();
        }

        // Process any filename-like arguments.
        for (int i = go.getOptInd(); i < args.length; i++) {
            System.out.println("Filename-like arg " + args[i]);
        }
    }

    void process2(String argChars, String[] args, boolean shouldFail) {
        int errs = 0;

        System.out.println("** START NEW WAY ** " + argChars);
        GetOpt go2 = new GetOpt(argChars);
        var m = go2.parseArguments(args);
        if (m.size() == 0)
            System.out.println("NO ARGS MATCHED");
        for (var key : m.keySet()) {
            char c = key.charAt(0);
            System.out.print("Found " + c);
            if (c == '?')
                errs++;
            String val = m.get(key);
            if (val == null || val.equals(""))
                System.out.print("; (no option)");
            else
                System.out.print("; Option " + val);
            System.out.println();
        }

        List<String> filenames = go2.getFilenameList();
        for (var filename : filenames) {
            System.out.println("Filename-like arg " + filename);
        }

        if (shouldFail) {
            if (errs != 0)
                System.out.println("Expected error(s) found");
            else
                System.out.println("** FAILURE ** Expected errors not found");
        } else {
            if (errs == 0)
                System.out.println("Expected error(s) found");
            else
                System.out.println("** FAILURE ** Expected errors not found");
        }
    }

    public void testConstructorArgs() {
        try {
            String bad = null;
            new GetOpt(bad);
            fail("GetOpt(null) did not throw expected exception");
        } catch (IllegalArgumentException ex) {
            System.err.println("Caught expected exception " + ex);
        }
        try {
            new GetOpt("::");
            fail("GetOpt(::) did not throw expected exception");
        } catch (IllegalArgumentException ex) {
            System.err.println("Caught expected exception " + ex);
        }
        new GetOpt("f:c:");        // this failed at one point - multiple : args
        new GetOpt("foo");        // multiple occurrences of same letter - ok?
    }
}