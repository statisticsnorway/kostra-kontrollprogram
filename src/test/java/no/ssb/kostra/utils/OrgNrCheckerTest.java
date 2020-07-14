package no.ssb.kostra.utils;


import no.ssb.kostra.utils.OrgNrChecker;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 07.11.2018.
 */
@Ignore
public class OrgNrCheckerTest {
    @Test
    public void testOrgNummerOK1() {
        assertTrue(OrgNrChecker.validOrgNr("974760673"));
    }

    @Test
    public void testOrgNummerOK2() {
        assertTrue(OrgNrChecker.validOrgNr("999999999"));
    }

    @Test
    public void testOrgNummerOK3() {
        assertTrue(OrgNrChecker.validOrgNr("123456785"));
    }

    @Test
    public void testOrgNummerFail1() {
        assertFalse(OrgNrChecker.validOrgNr("0"));
    }

    @Test
    public void testOrgNummerFail2() {
        assertFalse(OrgNrChecker.validOrgNr("000000000"));
    }

}
