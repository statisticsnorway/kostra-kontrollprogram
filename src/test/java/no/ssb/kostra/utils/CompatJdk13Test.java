package no.ssb.kostra.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import no.ssb.kostra.utils.CompatJdk13;
import org.junit.Test;


public class CompatJdk13Test {
	@Test
	public void testIsNumerical() {
		assertTrue(CompatJdk13.isNumerical("12345"));
		assertTrue(CompatJdk13.isNumerical("01234"));
		assertFalse(CompatJdk13.isNumerical(" 01234"));
		assertFalse(CompatJdk13.isNumerical("A"));
	}

	@Test
	public void testIsNumericalWithSpace() {
		assertTrue(CompatJdk13.isNumericalWithSpace("12345"));
		assertTrue(CompatJdk13.isNumericalWithSpace("01234"));
		assertTrue(CompatJdk13.isNumericalWithSpace(" 01234"));
		assertFalse(CompatJdk13.isNumericalWithSpace("A"));
	}
	
	@Test
	public void testRemoveSpace() {
		assertEquals(CompatJdk13.removeSpace("12345"), "12345");
		assertEquals(CompatJdk13.removeSpace(" 12345"), "12345");
		assertEquals(CompatJdk13.removeSpace("1 2 3 4 5"), "12345");
	}

	@Test
	public void testIsFloat() {
		assertTrue(CompatJdk13.isFloat("123,45", 2));
		assertTrue(CompatJdk13.isFloat(" 123,45", 2));
		assertFalse(CompatJdk13.isFloat("123,45", 1));
		assertFalse(CompatJdk13.isFloat("123,4", 2));
		assertFalse(CompatJdk13.isFloat(" ", 2));

	}

	@Test
	public void testIsInt() {
		assertTrue(CompatJdk13.isInt("123"));
		assertTrue(CompatJdk13.isInt(" 123"));
		assertFalse(CompatJdk13.isInt("023"));
		assertFalse(CompatJdk13.isInt(" "));

	}

}