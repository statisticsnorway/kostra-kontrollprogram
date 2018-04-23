package no.ssb.kostra.test.control.regnskap.regn0C;

import no.ssb.kostra.control.regnskap.regn0C.ControlAvskrivinger;
import org.junit.Before;
import org.junit.Test;

public class ControlAvskrivingerTest {
	ControlAvskrivinger t;
	String ok1;
	String ok2;
	String f_1;
	String f_2;

	@Before
	public void beforeTest() {
		t = new ControlAvskrivinger();
		// ----000000000000000000000000000000000000000000000000
		// ----000000000111111111122222222223333333333444444444
		// ----123456789012345678901234567890123456789012345678
		ok1 = "0A2015 030100                  1400 075        5";
		ok2 = "0A2015 160100                  1400 075        5";
		f_1 = "0A2015 094100                  1400 075        5";
		f_2 = "0A2015 010100                  1400 075        5";
	}

	@Test
	public void testValidFunksjon() {
// TODO
	}

	@Test
	public void testIsSpecialProveordningFearre20000() {
// TODO
	}

}
