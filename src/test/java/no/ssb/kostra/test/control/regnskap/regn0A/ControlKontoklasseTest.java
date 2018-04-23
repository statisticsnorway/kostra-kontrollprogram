package no.ssb.kostra.test.control.regnskap.regn0A;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import no.ssb.kostra.control.regnskap.regn0A.ControlKontoklasse;
import org.junit.Before;
import org.junit.Test;

public class ControlKontoklasseTest {
	ControlKontoklasse t;
	String ok1;
	String ok2;
	String f_1;
	String f_2;

	@Before
	public void beforeTest() {
		t = new ControlKontoklasse();
		// ----000000000000000000000000000000000000000000000000
		// ----000000000111111111122222222223333333333444444444
		// ----123456789012345678901234567890123456789012345678
		ok1 = "                               0                ";
		ok2 = "                               1                ";
		f_1 = "                               2                ";
		f_2 = "                               3                ";
	}

	@Test
	public void testControlKontoklasse0() {
		assertFalse(t.doControl(ok1, 1, "      ", ""));
	}

	@Test
	public void testControlKontoklasse1() {
		assertFalse(t.doControl(ok1, 1, "      ", ""));
	}

	@Test
	public void testControlKontoklasse2() {
		assertTrue(t.doControl(f_1, 1, "      ", ""));
	}

	@Test
	public void testControlKontoklasse3() {
		assertTrue(t.doControl(f_2, 1, "      ", ""));
	}

}