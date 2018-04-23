package no.ssb.kostra.test.control.regnskap.regn0A;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import no.ssb.kostra.control.regnskap.regn0A.ControlFunksjoner;
import org.junit.Before;
import org.junit.Test;

public class ControlFunksjonerTest {
	ControlFunksjoner t;
	String ok1;
	String ok2;
	String f_1;
	String f_2;
	String f_3;

	@Before
	public void beforeTest() {
		t = new ControlFunksjoner();
		// ----000000000000000000000000000000000000000000000000
		// ----000000000111111111122222222223333333333444444444
		// ----123456789012345678901234567890123456789012345678
		ok1 = "0A2016 030100                  1400 075        5";
		ok2 = "0A2016 160100                  1400 075        5";
		f_1 = "0A2016 094100                  1400 075        5";
		f_2 = "0A2016 010100                  1400 075        5";
		f_3 = "0A2016 060200                  1302 182        5";
	}

	@Test
	public void testValidFunksjon() {
		assertTrue(t.validFunksjon("302"));
		assertFalse(t.validFunksjon("ZZZ"));
	}

	@Test
	public void testIsSpecialProveordning() {
		assertTrue(t.isSpecialProveordning("060200"));
		assertFalse(t.isSpecialProveordning("999900"));
	}

	@Test
	public void testValidFunksjon30xOK() {
		assertFalse(t.validFunksjon("300"));
		assertTrue(t.validFunksjon("304"));
		assertTrue(t.validFunksjon("305"));
	}

	@Test
	public void testFylkeskommununaleFunksjonerOK1() {
		assertFalse(t.doControl(ok1, 1, "030100", ""));
	}

	@Test
	public void testFylkeskommununaleFunksjonerOK2() {
		assertFalse(t.doControl(ok2, 2, "160100", ""));
	}

	@Test
	public void testFylkeskommununaleFunksjonerFail1() {
		assertTrue(t.doControl(f_1, 3, "094100", ""));
	}

	@Test
	public void testFylkeskommununaleFunksjonerFail2() {
		assertTrue(t.doControl(f_2, 4, "010100", ""));
	}
}