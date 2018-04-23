package no.ssb.kostra.test.control.regnskap.regn0C;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import no.ssb.kostra.control.regnskap.regn0C.Control8b;
import org.junit.Before;
import org.junit.Test;

public class Control8bTest {
	Control8b t;
	String ok1;
	String f_1;

	@Before
	public void beforeTest() {
		t = new Control8b();
		// ----000000000000000000000000000000000000000000000000
		// ----000000000111111111122222222223333333333444444444
		// ----123456789012345678901234567890123456789012345678
		ok1 = "0A2015 030100                  0841 075        5";
		f_1 = "0A2015 010100                  1841 075        5";
	}

	@Test
	public void testValidFunksjonOK() {
		assertFalse(t.doControl(ok1, 1, "      ", ""));
	}

	@Test
	public void testValidFunksjonFail() {
		assertTrue(t.doControl(f_1, 1, "      ", ""));
	}
} 

