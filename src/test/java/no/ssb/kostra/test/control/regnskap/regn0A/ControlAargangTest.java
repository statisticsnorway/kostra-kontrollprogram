package no.ssb.kostra.test.control.regnskap.regn0A;

import no.ssb.kostra.control.regnskap.regn0A.ControlAargang;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ControlAargangTest {
	ControlAargang t;
	String ok;
	String f1;
	String f2;
	
	@Before
	public void beforeTest() {
		t = new ControlAargang();
		// ---000000000000000000000000000000000000000000000000
		// ---000000000111111111122222222223333333333444444444
		// ---123456789012345678901234567890123456789012345678
		ok = "  2017                                          ";
		f1 = "  2016                                          ";
		f2 = "  2018                                          ";
	}

	@Test
	public void testControlAargangOK() {
		assertFalse(t.doControl(ok, 1, "      ", ""));
	}

	@Test
	public void testControlAargangIFjor() {
		assertTrue(t.doControl(f1, 1, "      ", ""));
	}

	@Test
	public void testControlAargangNesteAar() {
		assertTrue(t.doControl(f2, 1, "      ", ""));
	}
	
}
