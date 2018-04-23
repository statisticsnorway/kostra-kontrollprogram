package no.ssb.kostra.test.control.regnskap.regn0A;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import no.ssb.kostra.control.regnskap.regn0A.ControlDubletter;
import org.junit.Before;
import org.junit.Test;

public class ControlDubletterTest {
	ControlDubletter t;
	String ok1;
	String ok2;
	String f_1;

	@Before
	public void beforeTest() {
		t = new ControlDubletter();
		// ----000000000000000000000000000000000000000000000000
		// ----000000000111111111122222222223333333333444444444
		// ----123456789012345678901234567890123456789012345678
		ok1 = "0A2015 094100                  1172 075        5";
		ok2 = "0A2015 094100                  1172 075        5";
		f_1 = "0A2015 094100                  0172 075        5";
	}

	@Test
	public void testControlDubletterOK() {
		t.doControl(ok1, 1, "      ", "");
		t.doControl(ok1, 2, "      ", "");
		assertTrue(t.foundError());
	}

	@Test
	public void testControlDubletterFail() {
		t.doControl(ok1, 1, "      ", "");
		t.doControl(f_1, 2, "      ", "");
		assertFalse(t.foundError());
	}
}
