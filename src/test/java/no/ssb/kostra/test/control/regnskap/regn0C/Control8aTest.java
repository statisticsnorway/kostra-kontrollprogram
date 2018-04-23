package no.ssb.kostra.test.control.regnskap.regn0C;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import no.ssb.kostra.control.regnskap.regn0C.Control8a;
import org.junit.Before;
import org.junit.Test;

public class Control8aTest {
	Control8a t;
	String ok1;
	String ok2;
	String ok3;
	String f_1;
	String f_2;
	String f_3;

	@Before
	public void beforeTest() {
		t = new Control8a();
		// ----000000000000000000000000000000000000000000000000
		// ----000000000111111111122222222223333333333444444444
		// ----123456789012345678901234567890123456789012345678
		ok1 = "0A2015 030100                  1800 075        5";
		ok2 = "0A2015 160100                  1840 075        5";
		ok3 = "0A2015 094100                  1860 075        5";
		f_1 = "0A2015 010100                  0800 075        5";
		f_2 = "0A2015 010100                  0840 075        5";
		f_3 = "0A2015 010100                  0860 075        5";
	}

	@Test
	public void testValidFunksjonOK() {
		assertFalse(t.doControl(ok1, 1, "      ", ""));
		assertFalse(t.doControl(ok2, 2, "      ", ""));
		assertFalse(t.doControl(ok3, 3, "      ", ""));
	}

	@Test
	public void testValidFunksjonFail() {
		assertTrue(t.doControl(f_1, 1, "      ", ""));
		assertTrue(t.doControl(f_2, 2, "      ", ""));
		assertTrue(t.doControl(f_3, 3, "      ", ""));
	}
}