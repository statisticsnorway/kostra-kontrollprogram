package no.ssb.kostra.test.control.regnskap.regn0C;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import no.ssb.kostra.control.regnskap.regn0C.ControlSkatteinntekter;
import org.junit.Before;
import org.junit.Test;

public class ControlSkatteinntekterTest {
	ControlSkatteinntekter t;
	String ok1;
	String ok2;
	String f_1;
	String f_2;

	@Before
	public void beforeTest() {
		t = new ControlSkatteinntekter();
		// ----000000000000000000000000000000000000000000000000
		// ----000000000111111111122222222223333333333444444444
		// ----123456789012345678901234567890123456789012345678
		ok1 = "0A2015 030100                  1800 870        5";
		f_1 = "0A2015 094100                  1800 870        0";
		f_2 = "0A2015 010100                  1400 075        5";
	}

	@Test
	public void testSkatteinntekterOK() {
		t.doControl(ok1, 1, "     ", "");
		assertFalse(t.foundError());
	}

	@Test
	public void testSkatteinntekterFail1() {
		t.doControl(f_1, 1, "     ", "");
		assertTrue(t.foundError());
	}

	@Test
	public void testSkatteinntekterFail2() {
		t.doControl(f_2, 1, "     ", "");
		assertTrue(t.foundError());
	}
}
